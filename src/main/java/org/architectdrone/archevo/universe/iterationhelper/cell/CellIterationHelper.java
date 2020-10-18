package org.architectdrone.archevo.universe.iterationhelper.cell;

import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.architectdrone.archevo.universe.IterationExecutionMode;

public class CellIterationHelper {
    public static CellIterationResult iterate(final Cell cell, ISA isa, OffsetToCell offsetToCell, IterationExecutionMode iterationExecutionMode)
    {
        Cell new_cell;
        Action action_to_return = null;
        if (iterationExecutionMode == IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION)
        {
            Action action = isa.getAction(cell, offsetToCell);
            new_cell = new Cell(cell, action);
            if (action.has_external_effect())
            {
                action_to_return = action;
            }

        }
        else // both of the others
        {
            Cell intermediate_cell = cell;
            for (int i = 0; i < cell.getGenome().size(); i++) {
                Action action = isa.getAction(intermediate_cell, offsetToCell);
                intermediate_cell = new Cell(intermediate_cell, action);
                if (action.has_external_effect())
                {
                    action_to_return = action;
                    new_cell = intermediate_cell;
                    break;
                }
            }
            new_cell = intermediate_cell;
            if (iterationExecutionMode != IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N_SAVE_IP)
            {
                new_cell.setIP(0);
            }
        }
        new_cell.cellStats = cell.cellStats;
        return new CellIterationResult(new_cell, action_to_return);
    }
}
