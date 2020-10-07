package org.architectdrone.archevo.isa;

import org.architectdrone.archevo.action.*;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.function.Function;

public class ASIA implements ISA {

    @Override
    public Action getAction(@NotNull Cell currentCell, @NotNull OffsetToCell offsetToCell) {
        BitSet instruction = currentCell.getGenome().get(currentCell.getIP());
        register register_1 = getReg1(instruction);
        register register_2 = getReg2(instruction);
        operation the_operation = getOperation(instruction, register_1);

        if (the_operation == operation.INCREMENT)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)+1);
        }
        else if (the_operation == operation.DECREMENT)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)-1);
        }
        else if (the_operation == operation.SHIFT_LEFT_LOGICAL)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)<<1);
        }
        else if (the_operation == operation.SHIFT_RIGHT_LOGICAL)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)>>>1);
        }
        else if (the_operation == operation.MOVE_REGISTER)
        {
            return  createRegisterUpdate(register_1, getRegisterValue(currentCell, register_2));
        }
        else if (the_operation == operation.SET_IF_LESS_THAN)
        {
            if (getRegisterValue(currentCell, register_1) < getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (the_operation == operation.SET_IF_GREATER_THAN)
        {
            if (getRegisterValue(currentCell, register_1) > getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (the_operation == operation.SET_IF_EQUAL_TO)
        {
            if (getRegisterValue(currentCell, register_1) == getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (the_operation == operation.REPRODUCE)
        {
            Offset offset = iplocToOffset(getRegisterValue(currentCell, register.IPLOC));
            return new Reproduce(offset.x, offset.y);
        }
        else if (the_operation == operation.JUMP)
        {
            return new MoveInstructionPointer(getBestJumpLocation(currentCell));
        }
        else if (the_operation == operation.JUMP_CONDITIONALLY)
        {
            if (getRegisterValue(currentCell, register_2) == 0xFF)
            {
                return new MoveInstructionPointer(getBestJumpLocation(currentCell));
            }
        }
        else if (the_operation == operation.UNASSIGNED || the_operation == operation.NOP_A || the_operation == operation.NOP_B)
        {
            return new DoNothing();
        }
        return null;
    }

    @Override
    public int getNumberOfBitsPerInstruction() {
        return 11;
    }

    @Override
    public BitSet stringToBits(@NotNull String instruction) {
        List<String> split_instruction = Arrays.asList(instruction.split(" "));
        String operation_string = split_instruction.get(0);
        operation specified_operation = stringToOperation(operation_string);

        if (specified_operation.is_ROP)
        {

        }
    }

    private operation stringToOperation(@NotNull String operation_string)
    {
        operation specified_operation = operation.INCREMENT;
        switch (operation_string) {
            case "INCREMENT":
                specified_operation = operation.INCREMENT;
                break;
            case "DECREMENT":
                specified_operation = operation.DECREMENT;
                break;
            case "SHIFT_LEFT_LOGICAL":
                specified_operation = operation.SHIFT_LEFT_LOGICAL;
                break;
            case "SHIFT_RIGHT_LOGICAL":
                specified_operation = operation.SHIFT_RIGHT_LOGICAL;
                break;
            case "MOVE_REGISTER":
                specified_operation = operation.MOVE_REGISTER;
                break;
            case "SET_IF_LESS_THAN":
                specified_operation = operation.SET_IF_LESS_THAN;
                break;
            case "SET_IF_GREATER_THAN":
                specified_operation = operation.SET_IF_GREATER_THAN;
                break;
            case "SET_IF_EQUAL_TO":
                specified_operation = operation.SET_IF_EQUAL_TO;
                break;
            case "REPRODUCE":
                specified_operation = operation.REPRODUCE;
                break;
            case "JUMP":
                specified_operation = operation.JUMP;
                break;
            case "JUMP_CONDITIONALLY":
                specified_operation = operation.JUMP_CONDITIONALLY;
                break;
            case "MOVE":
                specified_operation = operation.MOVE;
                break;
            case "UNASSIGNED":
                specified_operation = operation.UNASSIGNED;
                break;
            case "NOP_A":
                specified_operation = operation.NOP_A;
                break;
            case "NOP_B":
                specified_operation = operation.NOP_B;
                break;
            case "ATTACK":
                specified_operation = operation.ATTACK;
                break;
        }
        return specified_operation;
    }

    private register registerToOperation(@NotNull String register_string)
    {
        "ENERGY"
        "GUESS"
        "LOGO"
        REG_A
        REG_B
        REG_C
        REG_D
        IPLOC
        I_ENERGY
        I_GUESS
        I_LOGO
        I_REG_A
        I_REG_B
        I_REG_C
        I_REG_D
        I_IPLOC
    }

    @Override
    public String bitsToString(@NotNull BitSet instruction) {
        return null;
    }

    private RegisterUpdate createRegisterUpdate(register the_register, int new_value)
    {
        return new RegisterUpdate(new_value, the_register.register_number);
    }

    private int getBestJumpLocation(Cell currentCell)
    {
        List<BitSet> genome = currentCell.getGenome();
        Function<Integer, Integer> offset_to_index = (input) -> Math.floorMod(input, genome.size());
        int original_template_length = 0;
        boolean in_original_template = false;
        boolean in_template = false;
        boolean in_template_but_comparison_ended = false;
        boolean no_original_template = false;
        int best_index = 0;
        int current_template_points = 0;
        int current_template_offset = 0;
        float best_score = 0.0f;
        for (int offset = 0; offset < genome.size(); offset++) {
            int current_index = offset_to_index.apply(offset);
            BitSet current_instruction = genome.get(current_index);
            operation current_operation = getOperation(current_instruction, getReg1(current_instruction));
            boolean current_operation_is_nop_a = current_operation == operation.NOP_A;
            boolean current_operation_is_nop_b = current_operation == operation.NOP_B;
            boolean current_operation_is_nop = current_operation_is_nop_a || current_operation_is_nop_b;

            if (offset == 1) //Right after the JMP instruction.
            {
                if (!current_operation_is_nop)
                {
                    no_original_template = true;
                }
                else
                {
                    in_original_template = true;
                }
            }
            else
            {
                if (current_operation_is_nop)
                {
                    if (in_original_template)
                    {
                        original_template_length++;
                    }
                    else if (!in_template_but_comparison_ended && !no_original_template)
                    {
                        if (!in_template)
                        {
                            in_template = true;
                        }

                        if (current_template_offset >= original_template_length) //If the current template is equal to or has exceeded the original template's size
                        {
                            float current_template_total_score = (float)current_template_points /(float)original_template_length;
                            if (current_template_total_score > best_score) {
                                best_score = current_template_total_score;
                                best_index = current_index;
                            }
                            current_template_points = 0;
                            current_template_offset = 0;
                            in_template_but_comparison_ended = true;
                        }
                        else
                        {
                            int original_template_index_at_offset = offset_to_index.apply(current_template_offset);
                            BitSet original_template_instruction_at_offset = genome.get(original_template_index_at_offset);
                            operation original_template_operation_at_offset = getOperation(original_template_instruction_at_offset, getReg1(original_template_instruction_at_offset));
                            assert (original_template_operation_at_offset == operation.NOP_A || original_template_operation_at_offset == operation.NOP_B); //Sanity check.
                            if (original_template_instruction_at_offset == current_instruction)
                            {
                                current_template_points++;
                            }
                            current_template_offset++;
                        }
                    }

                }
                else
                {
                    if (in_original_template)
                    {
                        in_original_template = false;
                    }
                    if (in_template)
                    {
                        if (no_original_template)
                        {
                            return current_index;
                        }
                        else
                        {
                            if (!in_template_but_comparison_ended)
                            {
                                float current_template_total_score = (float)current_template_points /(float)original_template_length;
                                if (current_template_total_score > best_score) {
                                    best_score = current_template_total_score;
                                    best_index = current_index;
                                }
                                current_template_points = 0;
                                current_template_offset = 0;
                            }
                            in_template = false;
                            in_template_but_comparison_ended = false;
                        }
                    }

                }
            }
        }
        return best_index;
    }

    private Offset iplocToOffset(int iploc) {
        if (iploc >>> 8 == 1)
        {
            return new Offset(1, 1);
        }
        else if (iploc >>> 7 == 1)
        {
            return new Offset(0, 1);
        }
        else if (iploc >>> 6 == 1)
        {
            return new Offset(-1, 1);
        }
        else if (iploc >>> 5 == 1)
        {
            return new Offset(1, 0);
        }
        else if (iploc == 0)
        {
            return new Offset(0, 0);
        }
        else if (iploc >>> 4 == 1)
        {
            return new Offset(-1, 0);
        }
        else if (iploc >>> 3 == 1)
        {
            return new Offset(1, -1);
        }
        else if (iploc >>> 2 == 1)
        {
            return new Offset(0, -1);
        }
        else if (iploc >>> 1 == 1)
        {
            return new Offset(-1, -1);
        }
        return null;
    }

    private int getRegisterValue(Cell cell, register the_register)
    {
        return cell.getRegister(the_register.register_number);
    }

    public operation getOperation(BitSet instruction, register reg_1)
    {
        int opcode = Bits.convert(instruction.get(0, 2));
        if (hasWritePermissions(reg_1))
        {
            return getROP(opcode);
        }
        else
        {
            return getCOP(opcode);
        }
    }

    public register getReg1(BitSet instruction)
    {
        return getRegisterFromRegisterNumber(Bits.convert(instruction.get(7, 10)));
    }

    public register getReg2(BitSet instruction)
    {
        return getRegisterFromRegisterNumber(Bits.convert(instruction.get(3, 6)));
    }

    public register getRegisterFromRegisterNumber(int register_number)
    {
        if (register_number == 0x0)
        {
            return register.ENERGY;
        }
        else if (register_number == 0x1)
        {
            return register.GUESS;
        }
        else if (register_number == 0x2)
        {
            return register.LOGO;
        }
        else if (register_number == 0x3)
        {
            return register.REG_A;
        }
        else if (register_number == 0x4)
        {
            return register.REG_B;
        }
        else if (register_number == 0x5)
        {
            return register.REG_C;
        }
        else if (register_number == 0x6)
        {
            return register.REG_D;
        }
        else if (register_number == 0x7)
        {
            return register.IPLOC;
        }
        else if (register_number == 0x8)
        {
            return register.I_ENERGY;
        }
        else if (register_number == 0x9)
        {
            return register.I_GUESS;
        }
        else if (register_number == 0xA)
        {
            return register.I_LOGO;
        }
        else if (register_number == 0xB)
        {
            return register.I_REG_A;
        }
        else if (register_number == 0xC)
        {
            return register.I_REG_B;
        }
        else if (register_number == 0xD)
        {
            return register.I_REG_C;
        }
        else if (register_number == 0xE)
        {
            return register.I_REG_D;
        }
        return register.I_IPLOC;
    }

    public boolean hasWritePermissions(register registerToCheck)
    {
        return registerToCheck == register.GUESS || registerToCheck == register.LOGO || registerToCheck == register.REG_A || registerToCheck == register.REG_B || registerToCheck == register.REG_C || registerToCheck == register.REG_D || registerToCheck == register.IPLOC;
    }

    public operation getCOP(int opcode) {
        if (opcode == 0x0)
        {
            return operation.INCREMENT;
        }
        if (opcode == 0x1)
        {
            return operation.DECREMENT;
        }
        if (opcode == 0x2)
        {
            return operation.SHIFT_LEFT_LOGICAL;
        }
        if (opcode == 0x3)
        {
            return operation.SHIFT_RIGHT_LOGICAL;
        }
        if (opcode == 0x4)
        {
            return operation.MOVE_REGISTER;
        }
        if (opcode == 0x5)
        {
            return operation.SET_IF_LESS_THAN;
        }
        if (opcode == 0x6)
        {
            return operation.SET_IF_GREATER_THAN;
        }
        return operation.SET_IF_EQUAL_TO;
    }

    public operation getROP(int opcode) {
        if (opcode == 0x0)
        {
            return operation.REPRODUCE;
        }
        if (opcode == 0x1)
        {
            return operation.JUMP;
        }
        if (opcode == 0x2)
        {
            return operation.JUMP_CONDITIONALLY;
        }
        if (opcode == 0x3)
        {
            return operation.MOVE;
        }
        if (opcode == 0x4)
        {
            return operation.UNASSIGNED;
        }
        if (opcode == 0x5)
        {
            return operation.NOP_A;
        }
        if (opcode == 0x6)
        {
            return operation.NOP_B;
        }
        return operation.ATTACK;
    }
}

class Offset {
    public int x;
    public int y;
    public Offset(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

enum register {
    ENERGY   (0b0000,0),
    GUESS    (0b0001,1),
    LOGO     (0b0010,2),
    REG_A    (0b0011,3),
    REG_B    (0b0100,4),
    REG_C    (0b0101,5),
    REG_D    (0b0110,6),
    IPLOC    (0b0111,7),
    I_ENERGY (0b1000),
    I_GUESS  (0b1001),
    I_LOGO   (0b1010),
    I_REG_A  (0b1011),
    I_REG_B  (0b1100),
    I_REG_C  (0b1101),
    I_REG_D  (0b1110),
    I_IPLOC  (0b1111);

    register(int code, Integer register_number)
    {
        this.register_number = register_number;
    }

    register(int code)
    {
        this.register_number = null;
    }

    Integer register_number;
    int code;
}

enum operation {
    INCREMENT           (0b000,1),
    DECREMENT           (0b001,1),
    SHIFT_LEFT_LOGICAL  (0b010,1),
    SHIFT_RIGHT_LOGICAL (0b011,1),
    MOVE_REGISTER       (0b100,2),
    SET_IF_LESS_THAN    (0b101,2),
    SET_IF_GREATER_THAN (0b110,2),
    SET_IF_EQUAL_TO     (0b111,2),
    REPRODUCE           (0b000),
    JUMP                (0b001),
    JUMP_CONDITIONALLY  (0b010),
    MOVE                (0b011),
    UNASSIGNED          (0b100),
    NOP_A               (0b101),
    NOP_B               (0b110),
    ATTACK              (0b111);

    operation(int opcode)
    {
        this.is_ROP = false;
        this.number_of_registers = 0;
        this.opcode = opcode;
    }

    operation(int opcode, int number_of_registers)
    {
        if (number_of_registers != 0)
        {
            is_ROP = true;
        }
        else
        {
            is_ROP = false;
        }
        this.number_of_registers = number_of_registers;
        this.opcode = opcode;
    }

    boolean is_ROP;
    int number_of_registers;
    int opcode;
}

//From https://stackoverflow.com/a/2473719
class Bits {

    public static BitSet convert(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static int convert(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1 << i) : 0;
        }
        return value;
    }
}