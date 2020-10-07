package org.architectdrone.archevo.isa.asia;

import org.architectdrone.archevo.isa.ParsingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASIAOperationTest {

    @Test
    void constructor_works() {
        assertEquals(ASIAOperationType.INCREMENT, new ASIAOperation(ASIAOperationType.INCREMENT).getASIAOperationType());
    }

    @Test
    void fromBinaryAndRegister1_worksCorrectly() throws ParsingException {
        ASIARegister writable   = ASIARegister.fromString("REG_A");
        ASIARegister unwritable = ASIARegister.fromString("ENERGY");

        assertEquals(ASIAOperationType.INCREMENT,          ASIAOperation.fromBinaryAndRegister1(0b000, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.DECREMENT,          ASIAOperation.fromBinaryAndRegister1(0b001, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.SHIFT_LEFT_LOGICAL, ASIAOperation.fromBinaryAndRegister1(0b010, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.SHIFT_RIGHT_LOGICAL,ASIAOperation.fromBinaryAndRegister1(0b011, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.MOVE_REGISTER,      ASIAOperation.fromBinaryAndRegister1(0b100, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_LESS_THAN,   ASIAOperation.fromBinaryAndRegister1(0b101, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_GREATER_THAN,ASIAOperation.fromBinaryAndRegister1(0b110, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_EQUAL_TO,    ASIAOperation.fromBinaryAndRegister1(0b111, writable)  .getASIAOperationType());
        assertEquals(ASIAOperationType.REPRODUCE,          ASIAOperation.fromBinaryAndRegister1(0b000, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.JUMP,               ASIAOperation.fromBinaryAndRegister1(0b001, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.JUMP_CONDITIONALLY, ASIAOperation.fromBinaryAndRegister1(0b010, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.MOVE,               ASIAOperation.fromBinaryAndRegister1(0b011, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.UNASSIGNED,         ASIAOperation.fromBinaryAndRegister1(0b100, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.NOP_A,              ASIAOperation.fromBinaryAndRegister1(0b101, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.NOP_B,              ASIAOperation.fromBinaryAndRegister1(0b110, unwritable).getASIAOperationType());
        assertEquals(ASIAOperationType.ATTACK,             ASIAOperation.fromBinaryAndRegister1(0b111, unwritable).getASIAOperationType());
    }

    @Test
    void isROP_works()  {
        assertEquals(true,  new ASIAOperation(ASIAOperationType.INCREMENT)          .isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.DECREMENT)          .isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL) .isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL).isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.MOVE_REGISTER)      .isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN)   .isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN).isROP());
        assertEquals(true,  new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO)    .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.REPRODUCE)          .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.JUMP)               .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY) .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.MOVE)               .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.UNASSIGNED)         .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.NOP_A)              .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.NOP_B)              .isROP());
        assertEquals(false, new ASIAOperation(ASIAOperationType.ATTACK)             .isROP());
    }

    @Test
    void toBinary_works()  {
        assertEquals(0b000, new ASIAOperation(ASIAOperationType.INCREMENT)          .toBinary());
        assertEquals(0b001, new ASIAOperation(ASIAOperationType.DECREMENT)          .toBinary());
        assertEquals(0b010, new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL) .toBinary());
        assertEquals(0b011, new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL).toBinary());
        assertEquals(0b100, new ASIAOperation(ASIAOperationType.MOVE_REGISTER)      .toBinary());
        assertEquals(0b101, new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN)   .toBinary());
        assertEquals(0b110, new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN).toBinary());
        assertEquals(0b111, new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO)    .toBinary());
        assertEquals(0b000, new ASIAOperation(ASIAOperationType.REPRODUCE)          .toBinary());
        assertEquals(0b001, new ASIAOperation(ASIAOperationType.JUMP)               .toBinary());
        assertEquals(0b010, new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY) .toBinary());
        assertEquals(0b011, new ASIAOperation(ASIAOperationType.MOVE)               .toBinary());
        assertEquals(0b100, new ASIAOperation(ASIAOperationType.UNASSIGNED)         .toBinary());
        assertEquals(0b101, new ASIAOperation(ASIAOperationType.NOP_A)              .toBinary());
        assertEquals(0b110, new ASIAOperation(ASIAOperationType.NOP_B)              .toBinary());
        assertEquals(0b111, new ASIAOperation(ASIAOperationType.ATTACK)             .toBinary());
    }

    @Test
    void numberOfRegisters_works()  {
        assertEquals(1, new ASIAOperation(ASIAOperationType.INCREMENT)          .getNumberOfRegisters());
        assertEquals(1, new ASIAOperation(ASIAOperationType.DECREMENT)          .getNumberOfRegisters());
        assertEquals(1, new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL) .getNumberOfRegisters());
        assertEquals(1, new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL).getNumberOfRegisters());
        assertEquals(2, new ASIAOperation(ASIAOperationType.MOVE_REGISTER)      .getNumberOfRegisters());
        assertEquals(2, new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN)   .getNumberOfRegisters());
        assertEquals(2, new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN).getNumberOfRegisters());
        assertEquals(2, new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO)    .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.REPRODUCE)          .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.JUMP)               .getNumberOfRegisters());
        assertEquals(1, new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY) .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.MOVE)               .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.UNASSIGNED)         .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.NOP_A)              .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.NOP_B)              .getNumberOfRegisters());
        assertEquals(0, new ASIAOperation(ASIAOperationType.ATTACK)             .getNumberOfRegisters());
    }

    @Test
    void toString_works() {
        assertEquals("INCREMENT",           new ASIAOperation(ASIAOperationType.INCREMENT)          .toString());
        assertEquals("DECREMENT",           new ASIAOperation(ASIAOperationType.DECREMENT)          .toString());
        assertEquals("SHIFT_LEFT_LOGICAL",  new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL) .toString());
        assertEquals("SHIFT_RIGHT_LOGICAL", new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL).toString());
        assertEquals("MOVE_REGISTER",       new ASIAOperation(ASIAOperationType.MOVE_REGISTER)      .toString());
        assertEquals("SET_IF_LESS_THAN",    new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN)   .toString());
        assertEquals("SET_IF_GREATER_THAN", new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN).toString());
        assertEquals("SET_IF_EQUAL_TO",     new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO)    .toString());
        assertEquals("REPRODUCE",           new ASIAOperation(ASIAOperationType.REPRODUCE)          .toString());
        assertEquals("JUMP",                new ASIAOperation(ASIAOperationType.JUMP)               .toString());
        assertEquals("JUMP_CONDITIONALLY",  new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY) .toString());
        assertEquals("MOVE",                new ASIAOperation(ASIAOperationType.MOVE)               .toString());
        assertEquals("UNASSIGNED",          new ASIAOperation(ASIAOperationType.UNASSIGNED)         .toString());
        assertEquals("NOP_A",               new ASIAOperation(ASIAOperationType.NOP_A)              .toString());
        assertEquals("NOP_B",               new ASIAOperation(ASIAOperationType.NOP_B)              .toString());
        assertEquals("ATTACK",              new ASIAOperation(ASIAOperationType.ATTACK)             .toString());
    }

    @Test
    void fromString_works() throws ParsingException {
        assertEquals(ASIAOperationType.INCREMENT,           ASIAOperation.fromString("INCREMENT").getASIAOperationType());
        assertEquals(ASIAOperationType.DECREMENT,           ASIAOperation.fromString("DECREMENT").getASIAOperationType());
        assertEquals(ASIAOperationType.SHIFT_LEFT_LOGICAL,  ASIAOperation.fromString("SHIFT_LEFT_LOGICAL").getASIAOperationType());
        assertEquals(ASIAOperationType.SHIFT_RIGHT_LOGICAL, ASIAOperation.fromString("SHIFT_RIGHT_LOGICAL").getASIAOperationType());
        assertEquals(ASIAOperationType.MOVE_REGISTER,       ASIAOperation.fromString("MOVE_REGISTER").getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_LESS_THAN,    ASIAOperation.fromString("SET_IF_LESS_THAN").getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_GREATER_THAN, ASIAOperation.fromString("SET_IF_GREATER_THAN").getASIAOperationType());
        assertEquals(ASIAOperationType.SET_IF_EQUAL_TO,     ASIAOperation.fromString("SET_IF_EQUAL_TO").getASIAOperationType());
        assertEquals(ASIAOperationType.REPRODUCE,           ASIAOperation.fromString("REPRODUCE").getASIAOperationType());
        assertEquals(ASIAOperationType.JUMP,                ASIAOperation.fromString("JUMP").getASIAOperationType());
        assertEquals(ASIAOperationType.JUMP_CONDITIONALLY,  ASIAOperation.fromString("JUMP_CONDITIONALLY").getASIAOperationType());
        assertEquals(ASIAOperationType.MOVE,                ASIAOperation.fromString("MOVE").getASIAOperationType());
        assertEquals(ASIAOperationType.UNASSIGNED,          ASIAOperation.fromString("UNASSIGNED").getASIAOperationType());
        assertEquals(ASIAOperationType.NOP_A,               ASIAOperation.fromString("NOP_A").getASIAOperationType());
        assertEquals(ASIAOperationType.NOP_B,               ASIAOperation.fromString("NOP_B").getASIAOperationType());
        assertEquals(ASIAOperationType.ATTACK,              ASIAOperation.fromString("ATTACK").getASIAOperationType());
    }
}