package org.architectdrone.archevo.universe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.action.DoNothing;
import org.architectdrone.archevo.action.Move;
import org.architectdrone.archevo.action.MoveInstructionPointer;
import org.architectdrone.archevo.action.RegisterUpdate;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.ParsingException;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.architectdrone.archevo.universe.iterationhelper.cell.CellIterationHelper;
import org.architectdrone.archevo.universe.iterationhelper.cell.CellIterationResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellIterationHelperTest {
    ISA isa = new ASIA();
    OffsetToCell offsetToCell = (x, y) -> null;

    @Nested
    class InstructionByInstruction {
        IterationExecutionMode iterationExecutionMode = IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION;

        @Nested
        class nonWorldUpdatingInstructions {
            @Test
            void registerUpdate() throws ParsingException, MalformedInstructionException {
                //Use INC A
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("INCREMENT REG_A"));

                Cell original_cell = new Cell(genome, null);
                RegisterUpdate action_to_execute = (RegisterUpdate) isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);
                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;
                assertNull(resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //Only register 3 (REG_A) should change.
                assertEquals(original_cell.getRegister(0), new_cell.getRegister(0));
                assertEquals(original_cell.getRegister(1), new_cell.getRegister(1));
                assertEquals(original_cell.getRegister(2), new_cell.getRegister(2));
                assertEquals(action_to_execute.getNewValue()         , new_cell.getRegister(3));
                assertEquals(original_cell.getRegister(4), new_cell.getRegister(4));
                assertEquals(original_cell.getRegister(5), new_cell.getRegister(5));
                assertEquals(original_cell.getRegister(6), new_cell.getRegister(6));
                assertEquals(original_cell.getRegister(7), new_cell.getRegister(7));
                //Finally, the IP should increment by one.
                assertEquals(original_cell.getIP()+1, new_cell.getIP());
            }

            @Test
            void moveInstructionPointerUpdate() throws ParsingException, MalformedInstructionException {
                //Use JUMP
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("JUMP"));
                genome.set(1, isa.stringToBinary("NOP_A"));
                genome.set(9, isa.stringToBinary("NOP_A"));

                Cell original_cell = new Cell(genome, new ASIA());
                MoveInstructionPointer action_to_execute = (MoveInstructionPointer) isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);

                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertNull(resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //No cells should change.
                assertEquals(original_cell.getRegisters(), new_cell.getRegisters());
                //Finally, the IP should move to the correct location.
                assertEquals(action_to_execute.getNewInstructionPointerLocation(), new_cell.getIP());
            }

            @Test
            void doNothingUpdate() throws ParsingException, MalformedInstructionException {
                //Use UNASSIGNED
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("UNASSIGNED"));

                Cell original_cell = new Cell(genome, null);
                DoNothing action_to_execute = (DoNothing) isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);

                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertNull(resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //No cells should change.
                assertEquals(original_cell.getRegisters(), new_cell.getRegisters());
                //Finally, the IP should increment.
                assertEquals(original_cell.getIP()+1, new_cell.getIP());
            }
        }

        @Nested
        class worldUpdatingInstructions {
            @Test
            void attackInstruction() throws ParsingException, MalformedInstructionException {
                //Use ATTACK
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("ATTACK"));

                Cell original_cell = new Cell(genome, null);
                original_cell.setRegister(0b111, 4);
                Action action_to_execute = isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);

                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertEquals(action_to_execute, resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //No cells should change.
                assertEquals(original_cell.getRegisters(), new_cell.getRegisters());
                //Finally, the IP should increment.
                assertEquals(original_cell.getIP()+1, new_cell.getIP());
            }

            @Test
            void moveInstruction() throws ParsingException, MalformedInstructionException {
                //Use Move
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("MOVE"));

                Cell original_cell = new Cell(genome, null);
                original_cell.setRegister(0b111, 4);
                Action action_to_execute = isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);

                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertEquals(action_to_execute, resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //No cells should change.
                assertEquals(original_cell.getRegisters(), new_cell.getRegisters());
                //Finally, the IP should increment.
                assertEquals(original_cell.getIP()+1, new_cell.getIP());
            }

            @Test
            void reproduceInstruction() throws ParsingException, MalformedInstructionException {
                //Use REPRODUCE
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("REPRODUCE"));

                Cell original_cell = new Cell(genome, null);
                original_cell.setRegister(0b111, 4);
                Action action_to_execute = isa.getAction(original_cell, offsetToCell);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);

                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertEquals(action_to_execute, resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //No cells should change.
                assertEquals(original_cell.getRegisters(), new_cell.getRegisters());
                //Finally, the IP should increment.
                assertEquals(original_cell.getIP()+1, new_cell.getIP());
            }
        }
    }

    @Nested
    class RunUntilStateChangeOrN
    {
        @Nested
        class NoSaveIP {
            IterationExecutionMode iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N;
            @Test
            void untilStateChange() throws ParsingException, MalformedInstructionException {
                //We want to make sure that at each time the instruction is being executed, it is truly being executed. We ensure this with increment, since it increments by 1.
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(1, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(2, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(3, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(4, isa.stringToBinary("MOVE"));
                Cell original_cell = new Cell(genome, null);
                original_cell.setRegister(0b111, 4);

                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);
                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertEquals(resultant_action.getClass(), Move.class);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //Only register 3 (REG_A) should change.
                assertEquals(original_cell.getRegister(0), new_cell.getRegister(0));
                assertEquals(original_cell.getRegister(1), new_cell.getRegister(1));
                assertEquals(original_cell.getRegister(2), new_cell.getRegister(2));
                assertEquals(4                               , new_cell.getRegister(3));
                assertEquals(original_cell.getRegister(4), new_cell.getRegister(4));
                assertEquals(original_cell.getRegister(5), new_cell.getRegister(5));
                assertEquals(original_cell.getRegister(6), new_cell.getRegister(6));
                assertEquals(original_cell.getRegister(7), new_cell.getRegister(7));
                //Finally, the IP should be 0
                assertEquals(0, new_cell.getIP());
            }

            @Test
            void untilN() throws ParsingException, MalformedInstructionException {
                //Final value should be 16, since N is 16.
                List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
                Cell original_cell = new Cell(genome, null);

                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);
                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertNull(resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //Only register 3 (REG_A) should change.
                assertEquals(original_cell.getRegister(0), new_cell.getRegister(0));
                assertEquals(original_cell.getRegister(1), new_cell.getRegister(1));
                assertEquals(original_cell.getRegister(2), new_cell.getRegister(2));
                assertEquals(16                              , new_cell.getRegister(3));
                assertEquals(original_cell.getRegister(4), new_cell.getRegister(4));
                assertEquals(original_cell.getRegister(5), new_cell.getRegister(5));
                assertEquals(original_cell.getRegister(6), new_cell.getRegister(6));
                assertEquals(original_cell.getRegister(7), new_cell.getRegister(7));
                //Finally, the IP should be 0
                assertEquals(0, new_cell.getIP());
            }
        }

        @Nested
        class SaveIP {
            IterationExecutionMode iterationExecutionMode = IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N_SAVE_IP;
            @Test
            void untilStateChange() throws ParsingException, MalformedInstructionException {
                //We want to make sure that at each time the instruction is being executed, it is truly being executed. We ensure this with increment, since it increments by 1.
                List<Integer> genome = getEmptyGenome();
                genome.set(0, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(1, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(2, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(3, isa.stringToBinary("INCREMENT REG_A"));
                genome.set(4, isa.stringToBinary("MOVE"));
                Cell original_cell = new Cell(genome, null);
                original_cell.setRegister(0b111, 4);
                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);
                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertEquals(resultant_action.getClass(), Move.class);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //Only register 3 (REG_A) should change.
                assertEquals(original_cell.getRegister(0), new_cell.getRegister(0));
                assertEquals(original_cell.getRegister(1), new_cell.getRegister(1));
                assertEquals(original_cell.getRegister(2), new_cell.getRegister(2));
                assertEquals(4                               , new_cell.getRegister(3));
                assertEquals(original_cell.getRegister(4), new_cell.getRegister(4));
                assertEquals(original_cell.getRegister(5), new_cell.getRegister(5));
                assertEquals(original_cell.getRegister(6), new_cell.getRegister(6));
                assertEquals(original_cell.getRegister(7), new_cell.getRegister(7));
                //Finally, the IP should be 5
                assertEquals(5, new_cell.getIP());
            }

            @Test
            void untilN() throws ParsingException, MalformedInstructionException {
                //Final value should be 16, since N is 16.
                List<Integer> genome = new ArrayList<>(Collections.nCopies(16, isa.stringToBinary("INCREMENT REG_A")));
                Cell original_cell = new Cell(genome, null);

                CellIterationResult cellIterationResult = CellIterationHelper.iterate(original_cell, isa, offsetToCell, iterationExecutionMode);
                Cell new_cell = cellIterationResult.new_state;
                Action resultant_action = cellIterationResult.external_state_changing_action;

                assertNull(resultant_action);
                //Genome should ALWAYS stay the same.
                assertEquals(original_cell.getGenome(), new_cell.getGenome());
                //Only register 3 (REG_A) should change.
                assertEquals(original_cell.getRegister(0), new_cell.getRegister(0));
                assertEquals(original_cell.getRegister(1), new_cell.getRegister(1));
                assertEquals(original_cell.getRegister(2), new_cell.getRegister(2));
                assertEquals(16                              , new_cell.getRegister(3));
                assertEquals(original_cell.getRegister(4), new_cell.getRegister(4));
                assertEquals(original_cell.getRegister(5), new_cell.getRegister(5));
                assertEquals(original_cell.getRegister(6), new_cell.getRegister(6));
                assertEquals(original_cell.getRegister(7), new_cell.getRegister(7));
                //Finally, the IP should be 0
                assertEquals(0, new_cell.getIP());
            }
        }
    }

    List<Integer> getEmptyGenome() throws ParsingException, MalformedInstructionException {
        return new ArrayList<>(Collections.nCopies(16,isa.stringToBinary("UNASSIGNED")));
    }
}