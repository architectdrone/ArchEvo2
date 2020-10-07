package org.architectdrone.archevo.isa.asia;

import org.architectdrone.archevo.action.*;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.ParsingException;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.jetbrains.annotations.NotNull;

public class ASIA implements ISA {
    @Override
    public Action getAction(@NotNull Cell currentCell, @NotNull OffsetToCell offsetToCell) {
        Integer instruction_binary = currentCell.getGenome().get(currentCell.getIP());
        ASIAInstruction instruction = ASIAInstruction.fromBinary(instruction_binary);
        ASIAOperation operation = instruction.getOperation();
        ASIARegister register_1 = instruction.getRegister1();
        ASIARegister register_2 = instruction.getRegister2();

        if (operation.getASIAOperationType() == ASIAOperationType.INCREMENT)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)+1);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.DECREMENT)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)-1);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.SHIFT_LEFT_LOGICAL)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)<<1);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.SHIFT_RIGHT_LOGICAL)
        {
            return createRegisterUpdate(register_1, getRegisterValue(currentCell, register_1)>>>1);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.MOVE_REGISTER)
        {
            return  createRegisterUpdate(register_1, getRegisterValue(currentCell, register_2));
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.SET_IF_LESS_THAN)
        {
            if (getRegisterValue(currentCell, register_1) < getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.SET_IF_GREATER_THAN)
        {
            if (getRegisterValue(currentCell, register_1) > getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.SET_IF_EQUAL_TO)
        {
            if (getRegisterValue(currentCell, register_1) == getRegisterValue(currentCell, register_2))
            {
                return createRegisterUpdate(register_1, 0xFF);
            }
            return createRegisterUpdate(register_1, 0x00);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.REPRODUCE)
        {
            Offset offset = iplocToOffset(getRegisterValue(currentCell, 0b111));
            if (offset.x == 0 && offset.y == 0)
            {
                return new DoNothing();
            }
            return new Reproduce(offset.x, offset.y);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.MOVE)
        {
            Offset offset = iplocToOffset(getRegisterValue(currentCell, 0b111));
            if (offset.x == 0 && offset.y == 0)
            {
                return new DoNothing();
            }
            return new Move(offset.x, offset.y);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.ATTACK)
        {
            Offset offset = iplocToOffset(getRegisterValue(currentCell, 0b111));
            if (offset.x == 0 && offset.y == 0)
            {
                return new DoNothing();
            }
            return new Attack(offset.x, offset.y);
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.JUMP)
        {
            return new MoveInstructionPointer(ASIAJumpHandler.getBestJumpLocation(currentCell.getGenome(), currentCell.getIP()));
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.JUMP_CONDITIONALLY)
        {
            if (getRegisterValue(currentCell, register_2) == 0xFF)
            {
                return new MoveInstructionPointer(ASIAJumpHandler.getBestJumpLocation(currentCell.getGenome(), currentCell.getIP()));
            }
            else
            {
                return new MoveInstructionPointer(ASIAJumpHandler.getEndOfTemplate(currentCell.getGenome(), currentCell.getIP()));
            }
        }
        else if (operation.getASIAOperationType() == ASIAOperationType.UNASSIGNED || operation.getASIAOperationType() == ASIAOperationType.NOP_A || operation.getASIAOperationType() == ASIAOperationType.NOP_B)
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
    public Integer stringToBinary(@NotNull final String instruction) throws ParsingException, MalformedInstructionException {
        return ASIAInstruction.fromString(instruction).toBinary();
    }

    @Override
    public String binaryToString(@NotNull final Integer instruction) {
        return ASIAInstruction.fromBinary(instruction).toString();
    }

    private RegisterUpdate createRegisterUpdate(ASIARegister register, int new_value)
    {
        return new RegisterUpdate(new_value, register.getRegisterNumber());
    }

    private int getRegisterValue(Cell cell, ASIARegister register)
    {
        return cell.getRegister(register.getRegisterNumber());
    }

    private int getRegisterValue(Cell cell, int registerNumber)
    {
        return cell.getRegister(registerNumber);
    }

    private Offset iplocToOffset(int iploc) {
        if (iploc >>> 7 == 1) {
            return new Offset(1, 1);
        } else if (iploc >>> 6 == 1) {
            return new Offset(0, 1);
        } else if (iploc >>> 5 == 1) {
            return new Offset(-1, 1);
        } else if (iploc >>> 4 == 1) {
            return new Offset(1, 0);
        } else if (iploc == 0) {
            return new Offset(0, 0);
        } else if (iploc >>> 3 == 1) {
            return new Offset(-1, 0);
        } else if (iploc >>> 2 == 1) {
            return new Offset(1, -1);
        } else if (iploc >>> 1 == 1) {
            return new Offset(0, -1);
        } else if (iploc       == 1) {
            return new Offset(-1, -1);
        }
        return new Offset(-1, -1);
    }
}

class Offset {
    public int x;
    public int y;

    public Offset(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}