package org.architectdrone.archevo.universe.iterationhelper.cellcontainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.architectdrone.archevo.action.Attack;
import org.architectdrone.archevo.action.Move;
import org.architectdrone.archevo.action.RegisterUpdate;
import org.architectdrone.archevo.action.Reproduce;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.combathandler.CaptureTheFlag;
import org.architectdrone.archevo.combathandler.CombatHandler;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.isa.asia.ASIARegister;
import org.architectdrone.archevo.reproductionhandler.ReproductionHandler;
import org.architectdrone.archevo.reproductionhandler.SetCost;
import org.architectdrone.archevo.universe.IterationExecutionMode;
import org.architectdrone.archevo.universe.cellcontainer.CellContainer;
import org.architectdrone.archevo.universe.cellcontainer.LinearContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellContainerIterationHelperTest {
    ISA isa;
    CombatHandler combatHandler;
    int size;
    IterationExecutionMode iterationExecutionMode;
    ReproductionHandler reproductionHandler;
    CellContainer cellContainer;
    Cell cell;

    @BeforeEach
    void beforeEach() {
        isa = new ASIA();
        combatHandler = new CaptureTheFlag();
        size = 20;
        reproductionHandler = new SetCost();

    }

    @Nested
    class CorrectResponseToActionProducedByISA {
        @BeforeEach
        void beforeEach() {
            iterationExecutionMode = IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION;
            cellContainer = new LinearContainer(size);
        }

        @Test
        void RegisterUpdateAction() throws Exception {
            Integer binary_instruction = isa.stringToBinary("INCREMENT REG_A");
            Integer register_number = ASIARegister.fromString("REG_A").toBinary();

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(register_number, 5);

            RegisterUpdate isa_result = (RegisterUpdate) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);
            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
            Cell resultant_cell = newCellContainer.get(1, 1);

            assertEquals(isa_result.getNewValue(), resultant_cell.getRegister(isa_result.getRegisterToChange()));
            assertEquals(1, resultant_cell.getIP());
        }

        @Test
        void MoveAction () throws Exception {
            Integer binary_instruction = isa.stringToBinary("MOVE");

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(0b111, 0b10000000);

            Move isa_result = (Move) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);

            int new_x = isa_result.getXOffset() + 1;
            int new_y = isa_result.getYOffset() + 1;

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            assertNotEquals(null, newCellContainer.get(new_x, new_y));
            assertEquals(null, newCellContainer.get(1, 1));
        }

        @Test
        void MoveAction_whenCellAlreadyExistsInDesiredLocation () throws Exception {
            Integer binary_instruction = isa.stringToBinary("MOVE");

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(0b111, 0b10000000);

            Move isa_result = (Move) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);

            int new_x = isa_result.getXOffset() + 1;
            int new_y = isa_result.getYOffset() + 1;

            Cell blocking_cell = new Cell(new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED"))));

            cellContainer.set(new_x, new_y, blocking_cell);

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            assertEquals(cell.getGenome(), newCellContainer.get(1, 1).getGenome());
            assertEquals(blocking_cell.getGenome(), newCellContainer.get(new_x, new_y).getGenome());
        }

        @Test
        void ReproductionAction_whenEnoughEnergy () throws Exception {
            Integer binary_instruction = isa.stringToBinary("REPRODUCE");

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(0b111, 0b10000000);
            cell.setRegister(0b000, reproductionHandler.reproductionEnergyCost(cell)+1);

            Reproduce isa_result = (Reproduce) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);

            int new_cell_x = isa_result.getXOffset() + 1;
            int new_cell_y = isa_result.getYOffset() + 1;

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            assertNotEquals(null, newCellContainer.get(new_cell_x, new_cell_y));
            assertEquals(reproductionHandler.newCellEnergy(cell), newCellContainer.get(new_cell_x, new_cell_y).getRegister(0));
            assertNotEquals(null, newCellContainer.get(1, 1));
            assertEquals(1, newCellContainer.get(1,1).getRegister(0));
        }

        @Test
        void ReproductionAction_whenNotEnoughEnergy () throws Exception {
            Integer binary_instruction = isa.stringToBinary("REPRODUCE");

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(0b111, 0b10000000);
            cell.setRegister(0b000, reproductionHandler.reproductionEnergyCost(cell)-1);

            Reproduce isa_result = (Reproduce) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);

            int new_cell_x = isa_result.getXOffset() + 1;
            int new_cell_y = isa_result.getYOffset() + 1;

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            assertEquals(null, newCellContainer.get(new_cell_x, new_cell_y));
            assertNotEquals(null, newCellContainer.get(1, 1));
            assertEquals(reproductionHandler.reproductionEnergyCost(cell)-1, newCellContainer.get(1, 1).getRegister(0) );
        }

        @Test
        void ReproductionAction_whenCellIsWhereItWantedToReproduce () throws Exception {
            Integer binary_instruction = isa.stringToBinary("REPRODUCE");

            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            genome.set(0, binary_instruction);

            cell = new Cell(genome);
            cell.setRegister(0b111, 0b10000000);
            cell.setRegister(0b000, reproductionHandler.reproductionEnergyCost(cell)+1);

            Reproduce isa_result = (Reproduce) isa.getAction(cell, (x, y) -> null);

            cellContainer.set(1, 1, cell);

            int new_cell_x = isa_result.getXOffset() + 1;
            int new_cell_y = isa_result.getYOffset() + 1;

            Cell blocking_cell = new Cell(new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED"))));
            cellContainer.set(new_cell_x, new_cell_y, blocking_cell);

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            assertEquals(blocking_cell.getGenome(), newCellContainer.get(new_cell_x, new_cell_y).getGenome());
            assertEquals(cell.getGenome(), newCellContainer.get(1, 1).getGenome());
            assertEquals(reproductionHandler.reproductionEnergyCost(cell)+1, newCellContainer.get(1, 1).getRegister(0) );
        }

        @Test
        void AttackAction () throws Exception {
            Integer binary_instruction = isa.stringToBinary("ATTACK");

            int attacker_initial_energy = 16;
            int defender_initial_energy = 16;

            int logo  = 0b11111111;
            int guess = 0b11111110;

            List<Integer> defender_genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));;
            List<Integer> attacker_genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED")));
            attacker_genome.set(0, binary_instruction);

            Cell attacker_cell = new Cell(attacker_genome);
            Cell defender_cell = new Cell(defender_genome);
            attacker_cell.setRegister(0b111, 0b10000000);
            attacker_cell.setRegister(0b000, attacker_initial_energy);
            defender_cell.setRegister(0b000, defender_initial_energy);
            Attack isa_result = (Attack) isa.getAction(attacker_cell, (x, y) -> null);
            int attacking_x = isa_result.getXOffset() + 1;
            int attacking_y = isa_result.getYOffset() + 1;

            cellContainer.set(1, 1, attacker_cell);
            cellContainer.set(attacking_x, attacking_y, defender_cell);

            CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);

            int gained_energy = combatHandler.getResult(attacker_cell, defender_cell).getAttackerEnergyChange();
            int lost_energy = combatHandler.getResult(attacker_cell, defender_cell).getDefenderEnergyChange();

            assertEquals(attacker_initial_energy+gained_energy, newCellContainer.get(1,1).getRegister(0b000));
            assertEquals(defender_initial_energy+lost_energy, newCellContainer.get(attacking_x,attacking_y).getRegister(0b000));
        }
    }

    @Test
    void instructionByInstructionMode_incrementsIPEachTimeItRuns() throws Exception {
        iterationExecutionMode = IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION;
        cellContainer = new LinearContainer(size);
        cell = new Cell(new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("UNASSIGNED"))));
        cellContainer.set(1, 1, cell);
        CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(1, newCellContainer.get(1, 1).getIP());
        newCellContainer = CellContainerIterationHelper.iterate(newCellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(2, newCellContainer.get(1, 1).getIP());
    }

    @Test
    void untilStateChangeOrN_runsUntilStateChange() throws Exception {
        iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N;
        cellContainer = new LinearContainer(size);
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
        genome.set(4, isa.stringToBinary("ATTACK"));
        cell = new Cell(genome);
        cell.setRegister(0b111, 0b00010000);
        cellContainer.set(1, 1, cell);
        CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(4, newCellContainer.get(1, 1).getRegister(3));
        assertEquals(0, newCellContainer.get(1, 1).getIP());
    }

    @Test
    void untilStateChangeOrN_runsUntilN() throws Exception {
        iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N;
        cellContainer = new LinearContainer(size);
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
        cell = new Cell(genome);
        cellContainer.set(1, 1, cell);
        CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(16, newCellContainer.get(1, 1).getRegister(3));
        assertEquals(0, newCellContainer.get(1, 1).getIP());
    }

    @Test
    void untilStateChangeOrNSaveIP_runsUntilStateChange() throws Exception {
        iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N_SAVE_IP;
        cellContainer = new LinearContainer(size);
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
        genome.set(4, isa.stringToBinary("ATTACK"));
        cell = new Cell(genome);
        cell.setRegister(0b111, 0b00010000);
        cellContainer.set(1, 1, cell);
        CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(4, newCellContainer.get(1, 1).getRegister(3));
        assertEquals(5, newCellContainer.get(1, 1).getIP());
    }

    @Test
    void untilStateChangeOrNSaveIP_runsUntilN() throws Exception {
        iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N_SAVE_IP;
        cellContainer = new LinearContainer(size);
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
        cell = new Cell(genome);
        cellContainer.set(1, 1, cell);
        CellContainer newCellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
        assertEquals(16, newCellContainer.get(1, 1).getRegister(3));
        assertEquals(0, newCellContainer.get(1, 1).getIP());
    }

//    @Test
//    void speedTest_IterationByIteration() throws Exception {
//        iterationExecutionMode = IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION;
//        CellContainer cellContainer = new LinearContainer(50);
//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            try {
//                cellContainer.set(random.nextInt(50), random.nextInt(50), getRandomCell());
//            } catch (Exception e) {
//            }
//        }
//        for (int i = 0; i < 1000000; i++) {
//            cellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
//        }
//    }

//    @Test
//    void speedTest_RunUntilNoSaveIP() throws Exception {
//        iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N;
//        CellContainer cellContainer = new LinearContainer(50);
//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            try {
//                cellContainer.set(random.nextInt(50), random.nextInt(50), getRandomCell());
//            } catch (Exception e) {
//            }
//        }
//
//        for (int i = 0; i < 1000000; i++) cellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, 0, combatHandler, reproductionHandler);
//    }

    Cell getRandomCell() {
        Random random = new Random();
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            genome.add(random.nextInt((int) Math.pow(2, 11)));
        }
        return new Cell(genome);
    }
}