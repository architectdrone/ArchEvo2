package org.architectdrone.archevo.isa.asia;/*
 * Description
 * <p>
 * Copyrights 2020. Cerner Corporation.
 * @author Pharmacy Outpatient
 */

public enum ASIAOperationType {
    INCREMENT           (true,  0b000,1),
    DECREMENT           (true,  0b001,1),
    SHIFT_LEFT_LOGICAL  (true,  0b010,1),
    SHIFT_RIGHT_LOGICAL (true,  0b011,1),
    MOVE_REGISTER       (true,  0b100,2),
    SET_IF_LESS_THAN    (true,  0b101,2),
    SET_IF_GREATER_THAN (true,  0b110,2),
    SET_IF_EQUAL_TO     (true,  0b111,2),
    REPRODUCE           (false, 0b000),
    JUMP                (false, 0b001),
    JUMP_CONDITIONALLY  (false, 0b010,1),
    MOVE                (false, 0b011),
    UNASSIGNED          (false, 0b100),
    NOP_A               (false, 0b101),
    NOP_B               (false, 0b110),
    ATTACK              (false, 0b111);

    ASIAOperationType(boolean is_ROP, int binary)
    {
        this.is_ROP = is_ROP;
        this.number_of_registers = 0;
        this.binary = binary;
    }

    ASIAOperationType(boolean is_ROP, int binary, int number_of_registers)
    {
        this.is_ROP = is_ROP;
        this.number_of_registers = number_of_registers;
        this.binary = binary;
    }

    boolean is_ROP;
    int number_of_registers;
    int binary;
}