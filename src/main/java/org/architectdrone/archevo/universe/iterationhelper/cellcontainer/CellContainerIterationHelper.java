package org.architectdrone.archevo.universe.iterationhelper.cellcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.action.Attack;
import org.architectdrone.archevo.action.Move;
import org.architectdrone.archevo.action.Reproduce;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.combathandler.CombatHandler;
import org.architectdrone.archevo.combathandler.CombatResult;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.architectdrone.archevo.reproductionhandler.ReproductionHandler;
import org.architectdrone.archevo.universe.IterationExecutionMode;
import org.architectdrone.archevo.universe.cellcontainer.CellContainer;
import org.architectdrone.archevo.universe.cellcontainer.CellPosition;
import org.architectdrone.archevo.universe.cellcontainer.LinearContainer;
import org.architectdrone.archevo.universe.iterationhelper.cell.CellIterationHelper;
import org.architectdrone.archevo.universe.iterationhelper.cell.CellIterationResult;

public class CellContainerIterationHelper {
    public static CellContainer iterate(CellContainer cellContainer,
            ISA isa,
            IterationExecutionMode iterationExecutionMode,
            int move_cost,
            CombatHandler combatHandler,
            ReproductionHandler reproductionHandler) throws Exception {
        List<CellIterationResultAndPosition> cellIterationResultAndPositionList = cellContainer.getAllPositions()
                .stream()
                .map((cellPosition) -> {
                    CellIterationResult cellIterationResult = CellIterationHelper.iterate(
                            cellPosition.cell,
                            isa,
                            getOffsetToCell(cellPosition.x, cellPosition.y, cellContainer),
                            iterationExecutionMode);
                    return new CellIterationResultAndPosition(cellIterationResult, cellPosition);
                }).collect(Collectors.toList());

        Constructor constructor  = cellContainer.getClass().getConstructor(Integer.class);
        CellContainer newCellContainer = (CellContainer) constructor.newInstance(cellContainer.getSize());
        newCellContainer.load(cellIterationResultAndPositionList.stream().map((a) -> new CellPosition(a.cell, a.x, a.y)).collect(Collectors.toList()));

        cellIterationResultAndPositionList.forEach((a) -> {
            if (a.action != null) {
                if (a.action instanceof Move)
                {
                    Move move = (Move) a.action;
                    int new_x = a.x+move.getXOffset();
                    int new_y = a.y+move.getYOffset();

                    try {
                        a.cell.setRegister(0, a.cell.getRegister(0)-move_cost);
                        newCellContainer.set(new_x, new_y, a.cell);
                        newCellContainer.delete(a.x, a.y);
                    } catch (Exception e) {
                        //In this case we do nothing. The cell cannot move to the new location, because a cell is already there.
                    }
                }
                else if (a.action instanceof Attack)
                {
                    Attack attack = (Attack) a.action;
                    int attacking_x = a.x+attack.getXOffset();
                    int attacking_y = a.y+attack.getYOffset();
                    Cell defendingCell = newCellContainer.get(attacking_x, attacking_y);
                    CombatResult result = combatHandler.getResult(a.cell, defendingCell);
                    a.cell.setRegister(0, a.cell.getRegister(0)+ result.getAttackerEnergyChange());
                    if (newCellContainer.get(attacking_x, attacking_y) != null)
                    {
                        defendingCell.setRegister(0, defendingCell.getRegister(0)+ result.getDefenderEnergyChange());
                    }
                }
                else {
                    Reproduce reproduce = (Reproduce) a.action;
                    int reproducing_x = a.x+reproduce.getXOffset();
                    int reproducing_y = a.y+reproduce.getYOffset();

                    if (reproductionHandler.canReproduce(a.cell))
                    {
                        try {
                            newCellContainer.set(reproducing_x, reproducing_y, getBabyCell(a.cell, reproductionHandler.newCellEnergy(a.cell), isa));
                            a.cell.setRegister(0, a.cell.getRegister(0)-reproductionHandler.reproductionEnergyCost(a.cell));
                        } catch (Exception e) {
                            //If we encounter an exception, that means a cell is already in the place the parent wanted to reproduce in.
                        }
                    }
                }
            }
        });

        return newCellContainer;
    }

    private static Cell getBabyCell(Cell parent, int initialEnergy, ISA isa) {
        //TODO
        Cell baby = new Cell(parent.getGenome(), isa);
        baby.setRegister(0, initialEnergy);
        return baby;
    }

    private static OffsetToCell getOffsetToCell(int base_x, int base_y, CellContainer cellContainer) {
        return (x_offset, y_offset) -> cellContainer.get(base_x+x_offset, base_y+y_offset);
    }
}

class CellIterationResultAndPosition {
    public final Cell cell;
    public final int x;
    public final int y;
    public final Action action;

    public CellIterationResultAndPosition(CellIterationResult cellIterationResult, CellPosition cellPosition) {
        cell = cellIterationResult.new_state;
        action = cellIterationResult.external_state_changing_action;
        x = cellPosition.x;
        y = cellPosition.y;
    }
}