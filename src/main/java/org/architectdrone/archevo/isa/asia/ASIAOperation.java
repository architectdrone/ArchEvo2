package org.architectdrone.archevo.isa.asia;

import lombok.EqualsAndHashCode;
import org.architectdrone.archevo.isa.ParsingException;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode
public class ASIAOperation {
    private final ASIAOperationType asiaOperationType;

    public ASIAOperation(@NotNull ASIAOperationType asiaOperationType)
    {
        this.asiaOperationType = asiaOperationType;
    }

    /**
     * Convert binary into operation, using register 1 as a switch between COP and ROP.
     * @param binary The binary of the operation
     * @param register1 The register that controls the switch
     * @return the ASIAOperation.
     */
    public static ASIAOperation fromBinaryAndRegister1(int binary, @NotNull ASIARegister register1) {
        if (register1.hasWritePermissions()) //ROP
        {
            if      (binary == 0b000) return new ASIAOperation(ASIAOperationType.INCREMENT);
            else if (binary == 0b001) return new ASIAOperation(ASIAOperationType.DECREMENT);
            else if (binary == 0b010) return new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL);
            else if (binary == 0b011) return new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL);
            else if (binary == 0b100) return new ASIAOperation(ASIAOperationType.MOVE_REGISTER);
            else if (binary == 0b101) return new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN);
            else if (binary == 0b110) return new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN);
            else                      return new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO);
        }
        else //COP
        {
            if      (binary == 0b000) return new ASIAOperation(ASIAOperationType.REPRODUCE);
            else if (binary == 0b001) return new ASIAOperation(ASIAOperationType.JUMP);
            else if (binary == 0b010) return new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY);
            else if (binary == 0b011) return new ASIAOperation(ASIAOperationType.MOVE);
            else if (binary == 0b100) return new ASIAOperation(ASIAOperationType.UNASSIGNED);
            else if (binary == 0b101) return new ASIAOperation(ASIAOperationType.NOP_A);
            else if (binary == 0b110) return new ASIAOperation(ASIAOperationType.NOP_B);
            else                      return new ASIAOperation(ASIAOperationType.ATTACK);
        }
    }

    public boolean isROP() {
        return asiaOperationType.is_ROP;
    }

    public int getNumberOfRegisters() {
        return asiaOperationType.number_of_registers;
    }

    public int toBinary() {
        return asiaOperationType.binary;
    }

    @Override
    public String toString() {
        return asiaOperationType.toString();
    }
    
    public static ASIAOperation fromString(String string) throws ParsingException {
        switch (string) {
            case "INCREMENT":
                return new ASIAOperation(ASIAOperationType.INCREMENT);
            case "DECREMENT":
                return new ASIAOperation(ASIAOperationType.DECREMENT);
            case "SHIFT_LEFT_LOGICAL":
                return new ASIAOperation(ASIAOperationType.SHIFT_LEFT_LOGICAL);
            case "SHIFT_RIGHT_LOGICAL":
                return new ASIAOperation(ASIAOperationType.SHIFT_RIGHT_LOGICAL);
            case "MOVE_REGISTER":
                return new ASIAOperation(ASIAOperationType.MOVE_REGISTER);
            case "SET_IF_LESS_THAN":
                return new ASIAOperation(ASIAOperationType.SET_IF_LESS_THAN);
            case "SET_IF_GREATER_THAN":
                return new ASIAOperation(ASIAOperationType.SET_IF_GREATER_THAN);
            case "SET_IF_EQUAL_TO":
                return new ASIAOperation(ASIAOperationType.SET_IF_EQUAL_TO);
            case "REPRODUCE":
                return new ASIAOperation(ASIAOperationType.REPRODUCE);
            case "JUMP":
                return new ASIAOperation(ASIAOperationType.JUMP);
            case "JUMP_CONDITIONALLY":
                return new ASIAOperation(ASIAOperationType.JUMP_CONDITIONALLY);
            case "MOVE":
                return new ASIAOperation(ASIAOperationType.MOVE);
            case "UNASSIGNED":
                return new ASIAOperation(ASIAOperationType.UNASSIGNED);
            case "NOP_A":
                return new ASIAOperation(ASIAOperationType.NOP_A);
            case "NOP_B":
                return new ASIAOperation(ASIAOperationType.NOP_B);
            case "ATTACK":
                return new ASIAOperation(ASIAOperationType.ATTACK);
        }
        throw new ParsingException(string);
    }

    public ASIAOperationType getASIAOperationType() {
        return asiaOperationType;
    }
}
