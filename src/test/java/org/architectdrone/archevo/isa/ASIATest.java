package org.architectdrone.archevo.isa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.architectdrone.archevo.action.Attack;
import org.architectdrone.archevo.action.DoNothing;
import org.architectdrone.archevo.action.Move;
import org.architectdrone.archevo.action.MoveInstructionPointer;
import org.architectdrone.archevo.action.RegisterUpdate;
import org.architectdrone.archevo.action.Reproduce;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASIATest {

    ASIA ASIAInstance = new ASIA();

    @Test
    void stringToBinary_works() throws ParsingException, MalformedInstructionException {
        assertEquals(0b00000010001, new ASIA().stringToBinary("INCREMENT LOGO"));
        assertEquals(0b00100010001, new ASIA().stringToBinary("DECREMENT LOGO"));
        assertEquals(0b01000010001, new ASIA().stringToBinary("SHIFT_LEFT_LOGICAL LOGO"));
        assertEquals(0b01100010001, new ASIA().stringToBinary("SHIFT_RIGHT_LOGICAL LOGO"));
        assertEquals(0b10000000001, new ASIA().stringToBinary("MOVE_REGISTER LOGO ENERGY"));
        assertEquals(0b10100000001, new ASIA().stringToBinary("SET_IF_LESS_THAN LOGO ENERGY"));
        assertEquals(0b11000000001, new ASIA().stringToBinary("SET_IF_GREATER_THAN LOGO ENERGY"));
        assertEquals(0b11100000001, new ASIA().stringToBinary("SET_IF_EQUAL_TO LOGO ENERGY"));
        assertEquals(0b00000010000, new ASIA().stringToBinary("REPRODUCE"));
        assertEquals(0b00100010000, new ASIA().stringToBinary("JUMP"));
        assertEquals(0b01000000000, new ASIA().stringToBinary("JUMP_CONDITIONALLY ENERGY"));
        assertEquals(0b01100010000, new ASIA().stringToBinary("MOVE"));
        assertEquals(0b10000010000, new ASIA().stringToBinary("UNASSIGNED"));
        assertEquals(0b10100010000, new ASIA().stringToBinary("NOP_A"));
        assertEquals(0b11000010000, new ASIA().stringToBinary("NOP_B"));
        assertEquals(0b11100010000, new ASIA().stringToBinary("ATTACK"));
    }
    
    @Test
    void binaryToString()
    {
        assertEquals("INCREMENT LOGO", new ASIA().binaryToString(0b00000000001));
        assertEquals("DECREMENT LOGO", new ASIA().binaryToString(0b00100000001));
        assertEquals("SHIFT_LEFT_LOGICAL LOGO", new ASIA().binaryToString(0b01000000001));
        assertEquals("SHIFT_RIGHT_LOGICAL LOGO", new ASIA().binaryToString(0b01100000001));
        assertEquals("MOVE_REGISTER LOGO ENERGY", new ASIA().binaryToString(0b10000000001));
        assertEquals("SET_IF_LESS_THAN LOGO ENERGY", new ASIA().binaryToString(0b10100000001));
        assertEquals("SET_IF_GREATER_THAN LOGO ENERGY", new ASIA().binaryToString(0b11000000001));
        assertEquals("SET_IF_EQUAL_TO LOGO ENERGY", new ASIA().binaryToString(0b11100000001));
        assertEquals("REPRODUCE", new ASIA().binaryToString(0b00000000000));
        assertEquals("JUMP", new ASIA().binaryToString(0b00100000000));
        assertEquals("JUMP_CONDITIONALLY ENERGY", new ASIA().binaryToString(0b01000000000));
        assertEquals("MOVE", new ASIA().binaryToString(0b01100000000));
        assertEquals("UNASSIGNED", new ASIA().binaryToString(0b10000000000));
        assertEquals("NOP_A", new ASIA().binaryToString(0b10100000000));
        assertEquals("NOP_B", new ASIA().binaryToString(0b11000000000));
        assertEquals("ATTACK", new ASIA().binaryToString(0b11100000000));
    }

    @Nested
    class GetActionTest
    {
        OffsetToCell return_null = (x, y) -> null;
        @Test
        void getAction_withIncrementInstruction_incrementsR1_happyPath() {
            Cell test_cell = getCellWithInstructionAtIP0(0b00000000001);
            test_cell.setRegister(0b0001, 23);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);
            assertEquals(24, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withDecrementInstruction_decrementsR1_happyPath() {
            Cell test_cell = getCellWithInstructionAtIP0(0b00100000001);
            test_cell.setRegister(0b0001, 23);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);
            assertEquals(22, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withShiftLeftLogicalInstruction_shiftsR1Left_atHappyPath() {
            Cell test_cell = getCellWithInstructionAtIP0(0b01000000001);
            test_cell.setRegister(0b0001, 0b1000);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0b10000, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withShiftRightLogicalInstruction_shiftsR1Right_atHappyPath() {
            Cell test_cell = getCellWithInstructionAtIP0(0b01100000001);
            test_cell.setRegister(0b0001, 0b1000);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0b100, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withShiftRightLogicalInstruction_shiftsOutOfR1_withOneAtLSB() {
            Cell test_cell = getCellWithInstructionAtIP0(0b01100000001);
            test_cell.setRegister(0b0001, 0b1);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0b0, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withMoveRegisterInstruction_movesR2ToR1() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10000100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 42);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(42, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withMoveRegisterInstruction_withIPLOC_movesR2ToR1() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10010100001);
            test_cell.setRegister(0b0001, 16);
            Cell other_cell = new Cell(new ArrayList<>(), null);
            other_cell.setRegister(0b0010, 42);
            OffsetToCell returnOtherCell = (x, y) -> other_cell;
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, returnOtherCell);

            assertEquals(42, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfLessThanInstruction_setsR1ToFF_whenR1IsLessThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 42);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0xFF, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfLessThanInstruction_setsR1To00_whenR1IsEqualToR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 16);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }
        
        @Test
        void getAction_withSetIfLessThanInstruction_setsR1To00_whenR1IsGreaterThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 7);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfGreaterThanInstruction_setsR1To00_whenR1IsLessThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11000100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 42);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfGreaterThanInstruction_setsR1To00_whenR1IsEqualToR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11000100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 16);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfGreaterThanInstruction_setsR1ToFF_whenR1IsGreaterThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11000100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 7);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0xFF, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }
        
        @Test
        void getAction_withSetIfEqualToInstruction_setsR1To00_whenR1IsLessThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 42);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfEqualToInstruction_setsR1To00_whenR1IsGreaterThanR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 7);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0x00, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Test
        void getAction_withSetIfEqualToInstruction_setsR1ToFF_whenR1IsEqualToR2() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11100100001);
            test_cell.setRegister(0b0001, 16);
            test_cell.setRegister(0b0010, 16);
            RegisterUpdate result = (RegisterUpdate) new ASIA().getAction(test_cell, return_null);

            assertEquals(0xFF, result.getNewValue());
            assertEquals(0b0001, result.getRegisterToChange());
        }

        @Nested
        class ReproduceTest {
            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit7Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b10000000);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit6Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b01000000);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit5Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00100000);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit4Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00010000);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit3Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00001000);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit2Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00000100);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit1Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00000010);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_reproducesAtCorrectIPLOCLocation_whenBit0Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0b00000001);
                Reproduce result = (Reproduce) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withReproduceInstruction_doesNothing_whenIPLOCIs0() {
                Cell test_cell = getCellWithInstructionAtIP0(0b00000000000);
                test_cell.setRegister(0b111, 0);
                DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
            }
        }

        @Nested
        class JumpAndJumpConditionallyTest {
            //Since TemplateGroup and ASIAJumpHandler already have pretty good test coverage, I am just testing that the correct values are passed to the Action.
            //If a jump instruction is at 0,
            //  Following the jump should put the IP at 14
            //  Not following it   should put the IP at 3
            List<Integer> genome;

            @BeforeEach
            void beforeEach() {
                Integer nop_a      = 0b10100000000;
                Integer nop_b      = 0b11000000000;
                Integer unassigned = 0b10000000000;

                //Create Genome
                genome = new ArrayList<Integer>(Collections.nCopies(16, unassigned));;

                //Put a jump at 0, followed by nop_a and nop_b
                genome.set(1, nop_a);
                genome.set(2, nop_b);

                //Put the other template at 12.
                genome.set(12, nop_a);
                genome.set(13, nop_b);
            }

            @Test
            void getAction_withJumpInstruction_jumpsToEndOfMatchingTemplate() {
                genome.set(0, 0b00100000000);
                Cell test_cell = new Cell(genome, new ASIA());
                MoveInstructionPointer result = (MoveInstructionPointer) new ASIA().getAction(test_cell, return_null);

                assertEquals(14, result.getNewInstructionPointerLocation());
            }

            @Test
            void getAction_withJumpConditionallyInstruction_jumpsToEndOfMatchingTemplate_whenR2IsFF() {
                genome.set(0, 0b01000000000);
                Cell test_cell = new Cell(genome, new ASIA());
                test_cell.setRegister(0b0000, 0xFF);
                MoveInstructionPointer result = (MoveInstructionPointer) new ASIA().getAction(test_cell, return_null);

                assertEquals(14, result.getNewInstructionPointerLocation());
            }

            @Test
            void getAction_withJumpConditionallyInstruction_jumpsToEndOfMatchingTemplate_whenR2IsNotFF() {
                genome.set(0, 0b01000000000);
                Cell test_cell = new Cell(genome, new ASIA());
                test_cell.setRegister(0b0000, 0x00);
                MoveInstructionPointer result = (MoveInstructionPointer) new ASIA().getAction(test_cell, return_null);

                assertEquals(3, result.getNewInstructionPointerLocation());
            }
        }

        @Nested
        class MoveTest {

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit7Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b10000000);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit6Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b01000000);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit5Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00100000);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit4Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00010000);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit3Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00001000);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit2Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00000100);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit1Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00000010);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_movesToCorrectIPLOCLocation_whenBit0Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0b00000001);
                Move result = (Move) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withMoveInstruction_doesNothing_whenIPLOCIs0() {
                Cell test_cell = getCellWithInstructionAtIP0(0b01100000000);
                test_cell.setRegister(0b111, 0);
                DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
            }
        }

        @Test
        void getAction_withUnassignedInstruction_doesNothing() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10000000000);
            DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
        }

        @Test
        void getAction_withNOPAInstruction_doesNothing() {
            Cell test_cell = getCellWithInstructionAtIP0(0b10100000000);
            DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
        }

        @Test
        void getAction_withNOPBInstruction_doesNothing() {
            Cell test_cell = getCellWithInstructionAtIP0(0b11000000000);
            DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
        }

        @Nested
        class AttackTest {

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit7Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b10000000);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit6Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b01000000);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit5Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00100000);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit4Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00010000);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit3Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00001000);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(0, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit2Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00000100);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit1Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00000010);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(0, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_attacksCorrectIPLOCLocation_whenBit0Is1() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0b00000001);
                Attack result = (Attack) new ASIA().getAction(test_cell, return_null);
                assertEquals(-1, result.getXOffset());
                assertEquals(-1, result.getYOffset());
            }

            @Test
            void getAction_withAttackInstruction_doesNothing_whenIPLOCIs0() {
                Cell test_cell = getCellWithInstructionAtIP0(0b11100000000);
                test_cell.setRegister(0b111, 0);
                DoNothing result = (DoNothing) new ASIA().getAction(test_cell, return_null);
            }
        }

        Cell getCellWithInstructionAtIP0(Integer instruction)
        {
            List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));
            genome.set(0, instruction);

            return new Cell(genome, null);
        }
    }
}