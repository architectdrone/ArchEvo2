package org.architectdrone.archevo.isa.asia;

import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASIAInstructionTest {
    ASIAOperation increment;
    ASIAOperation decrement;
    ASIAOperation shift_left_logical;
    ASIAOperation shift_right_logical;
    ASIAOperation move_register;
    ASIAOperation set_if_greater_than;
    ASIAOperation set_if_less_than;
    ASIAOperation set_if_equal_to;
    ASIAOperation reproduce;
    ASIAOperation jump;
    ASIAOperation jump_conditionally;
    ASIAOperation move;
    ASIAOperation unassigned;
    ASIAOperation nop_a;
    ASIAOperation nop_b;
    ASIAOperation attack;
    ASIARegister  reg_a;
    ASIARegister  reg_b;
    ASIARegister  i_reg_a;
    ASIARegister  i_reg_b;
    @BeforeEach
    void before_each () throws ParsingException {
        increment = new ASIAOperation(ASIAOperationType.INCREMENT);
        decrement = new ASIAOperation(ASIAOperationType.DECREMENT);
        shift_left_logical = new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL);
        shift_right_logical = new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL);
        move_register = new ASIAOperation(ASIAOperationType.MOVE_REGISTER);
        set_if_greater_than = new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN);
        set_if_less_than = new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN);
        set_if_equal_to = new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO);
        reproduce = new ASIAOperation(ASIAOperationType.REPRODUCE);
        jump = new ASIAOperation(ASIAOperationType.JUMP);
        jump_conditionally = new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY);
        move = new ASIAOperation(ASIAOperationType.MOVE);
        unassigned = new ASIAOperation(ASIAOperationType.UNASSIGNED);
        nop_a = new ASIAOperation(ASIAOperationType.NOP_A);
        nop_b = new ASIAOperation(ASIAOperationType.NOP_B);
        attack = new ASIAOperation(ASIAOperationType.ATTACK);

        reg_a = ASIARegister.fromString("REG_A");
        reg_b = ASIARegister.fromString("REG_B");
        i_reg_a = ASIARegister.fromString("I_REG_A");
        i_reg_b = ASIARegister.fromString("I_REG_B");
    }

    @Test
    void constructor_works() throws MalformedInstructionException {
        ASIAInstruction asiaInstruction = new ASIAInstruction(increment, reg_a, reg_b);
        assertEquals(increment, asiaInstruction.getOperation());
        assertEquals(reg_a, asiaInstruction.getRegister1());
        assertEquals(reg_b, asiaInstruction.getRegister2());
    }

    @Test
    void constructor_failsWhenGivenInvalidInstruction()
    {
        assertThrows(MalformedInstructionException.class, () -> new ASIAInstruction(increment, i_reg_a, reg_b));
    }

    @Test
    void toString_works() throws MalformedInstructionException {
        assertEquals("INCREMENT REG_A",                 new ASIAInstruction(increment,           reg_a, reg_b).toString());
        assertEquals("DECREMENT REG_A",                 new ASIAInstruction(decrement,           reg_a, reg_b).toString());
        assertEquals("SHIFT_LEFT_LOGICAL REG_A",        new ASIAInstruction(shift_left_logical,  reg_a, reg_b).toString());
        assertEquals("SHIFT_RIGHT_LOGICAL REG_A",       new ASIAInstruction(shift_right_logical, reg_a, reg_b).toString());
        assertEquals("MOVE_REGISTER REG_A REG_B",       new ASIAInstruction(move_register,       reg_a, reg_b).toString());
        assertEquals("SET_IF_LESS_THAN REG_A REG_B",    new ASIAInstruction(set_if_less_than,    reg_a, reg_b).toString());
        assertEquals("SET_IF_GREATER_THAN REG_A REG_B", new ASIAInstruction(set_if_greater_than, reg_a, reg_b).toString());
        assertEquals("SET_IF_EQUAL_TO REG_A REG_B",     new ASIAInstruction(set_if_equal_to,     reg_a, reg_b).toString());
        assertEquals("REPRODUCE",                       new ASIAInstruction(reproduce,         i_reg_a, reg_b).toString());
        assertEquals("JUMP",                            new ASIAInstruction(jump,              i_reg_a, reg_b).toString());
        assertEquals("JUMP_CONDITIONALLY REG_B",        new ASIAInstruction(jump_conditionally,i_reg_a, reg_b).toString());
        assertEquals("MOVE",                            new ASIAInstruction(move,              i_reg_a, reg_b).toString());
        assertEquals("UNASSIGNED",                      new ASIAInstruction(unassigned,        i_reg_a, reg_b).toString());
        assertEquals("NOP_A",                           new ASIAInstruction(nop_a,             i_reg_a, reg_b).toString());
        assertEquals("NOP_B",                           new ASIAInstruction(nop_b,             i_reg_a, reg_b).toString());
        assertEquals("ATTACK",                          new ASIAInstruction(attack,            i_reg_a, reg_b).toString());
    }

    @Test
    void fromString_works() throws MalformedInstructionException, ParsingException {
        assertEquals(ASIAInstruction.fromString("INCREMENT REG_A"),                 new ASIAInstruction(increment,           reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("DECREMENT REG_A"),                 new ASIAInstruction(decrement,           reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("SHIFT_LEFT_LOGICAL REG_A"),        new ASIAInstruction(shift_left_logical,  reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("SHIFT_RIGHT_LOGICAL REG_A"),       new ASIAInstruction(shift_right_logical, reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("MOVE_REGISTER REG_A REG_B"),       new ASIAInstruction(move_register,       reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("SET_IF_LESS_THAN REG_A REG_B"),    new ASIAInstruction(set_if_less_than,    reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("SET_IF_GREATER_THAN REG_A REG_B"), new ASIAInstruction(set_if_greater_than, reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("SET_IF_EQUAL_TO REG_A REG_B"),     new ASIAInstruction(set_if_equal_to,     reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("REPRODUCE"),                       new ASIAInstruction(reproduce,         i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("JUMP"),                            new ASIAInstruction(jump,              i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("JUMP_CONDITIONALLY REG_B"),        new ASIAInstruction(jump_conditionally,i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("MOVE"),                            new ASIAInstruction(move,              i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("UNASSIGNED"),                      new ASIAInstruction(unassigned,        i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("NOP_A"),                           new ASIAInstruction(nop_a,             i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("NOP_B"),                           new ASIAInstruction(nop_b,             i_reg_a, reg_b));
        assertEquals(ASIAInstruction.fromString("ATTACK"),                          new ASIAInstruction(attack,            i_reg_a, reg_b));
    }

    @Test
    void fromString_throwsMalformedInstructionException_whenGivenIncorrectNumberOfArguments() {
        assertThrows(MalformedInstructionException.class,() -> ASIAInstruction.fromString("NOP_A REG_A"));
    }

    @Test
    void fromString_throwsMalformedInstructionException_whenGivenAccessViolatingInstruction() {
        assertThrows(MalformedInstructionException.class,() -> ASIAInstruction.fromString("INCREMENT I_REG_A"));
    }

    @Test
    void fromBinary_works() {
        int binary = 0b10000011000;
        assertEquals(0b100,  ASIAInstruction.fromBinary(binary).getOperation().toBinary());
        assertEquals(0b1000, ASIAInstruction.fromBinary(binary).getRegister1().toBinary());
        assertEquals(0b0001, ASIAInstruction.fromBinary(binary).getRegister2().toBinary());
    }

    @Test
    void toBinary_works() throws MalformedInstructionException {
        int operation_binary = 0b100;
        int register1_binary = 0b0001;
        int register2_binary = 0b1000;
        int instruction_binary = 0b10010000001;
        ASIARegister register1 = ASIARegister.fromBinary(register1_binary);
        ASIARegister register2 = ASIARegister.fromBinary(register2_binary);
        ASIAOperation operation = ASIAOperation.fromBinaryAndRegister1(operation_binary, register1);

        assertEquals(instruction_binary, new ASIAInstruction(operation, register1, register2).toBinary());
    }
}