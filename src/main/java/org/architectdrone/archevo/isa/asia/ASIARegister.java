package org.architectdrone.archevo.isa.asia;

import lombok.EqualsAndHashCode;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.ParsingException;

@EqualsAndHashCode
public class ASIARegister {
    private final int register_number;
    private final boolean virtual_register;

    /**
     * Constructor for an ASIA register.
     * @param register_number The register number (Must be between 0 and 7, inclusive)
     * @param virtual_register Whether or not it is virtual.
     */
    public ASIARegister(int register_number, boolean virtual_register)
    {
        if (register_number < 0)
            throw new AssertionError();
        if (register_number > 7)
            throw new AssertionError();
        this.register_number = register_number;
        this.virtual_register = virtual_register;
    }

    /**
     * Converts an integer to a register. The integer is interpreted as binary, with the first bit indicating whether or not it is virtual.
     * @param binaryRegister the register,in binary, as an integer
     * @return The ASIARegister
     */
    public static ASIARegister fromBinary(int binaryRegister)
    {
        if (binaryRegister >= 16 || binaryRegister < 0)
            throw new AssertionError();
        return new ASIARegister(binaryRegister & 0b0111, ((binaryRegister >> 3) & 0b1) == 1);
    }

    /**
     * Returns an ASIARegister that is a physical version of the current. Throws error if current ASIARegister is not virtual.
     * @return A physical ASIARegister.
     */
    public ASIARegister convertToPhysical()
    {
        assert virtual_register;
        return new ASIARegister(register_number, false);
    }

    /**
     * Gets the value of a register in a cell with the current register's number. Must not be virtual.
     * @param physicalCell The cell to get the value of the register of.
     * @return The value of the register.
     */
    public int getValue(Cell physicalCell)
    {
        assert !virtual_register;
        return physicalCell.getRegister(register_number);
    }

    /**
     * Converts the register back into it's binary interpretation.
     * @return The binary interpretation of the register.
     */
    public int toBinary() {
        if (virtual_register)
        {
            return 0b1000 | register_number;
        }
        else
        {
            return register_number;
        }
    }

    /**
     * Converts a string to an ASIA Register
     * @param register_string The string to convert
     * @return The register
     * @throws ParsingException If the string could not be converted to a register.
     */
    public static ASIARegister fromString(String register_string) throws ParsingException {
        switch (register_string) {
            case "ENERGY":
                return new ASIARegister(0b000, false);
            case "LOGO":
                return new ASIARegister(0b001, false);
            case "GUESS":
                return new ASIARegister(0b010, false);
            case "REG_A":
                return new ASIARegister(0b011, false);
            case "REG_B":
                return new ASIARegister(0b100, false);
            case "REG_C":
                return new ASIARegister(0b101, false);
            case "REG_D":
                return new ASIARegister(0b110, false);
            case "IPLOC":
                return new ASIARegister(0b111, false);
            case "I_ENERGY":
                return new ASIARegister(0b000, true);
            case "I_LOGO":
                return new ASIARegister(0b001, true);
            case "I_GUESS":
                return new ASIARegister(0b010, true);
            case "I_REG_A":
                return new ASIARegister(0b011, true);
            case "I_REG_B":
                return new ASIARegister(0b100, true);
            case "I_REG_C":
                return new ASIARegister(0b101, true);
            case "I_REG_D":
                return new ASIARegister(0b110, true);
            case "I_IPLOC":
                return new ASIARegister(0b111, true);
        }

        throw new ParsingException(register_string);
    }

    /**
     * Converts an ASIA Register to a String
     * @return the string representation
     */
    public String toString() {
        String physicalRegisterName;
        if (register_number == 0b000) physicalRegisterName = "ENERGY";
        else if (register_number == 0b001) physicalRegisterName = "LOGO";
        else if (register_number == 0b010) physicalRegisterName = "GUESS";
        else if (register_number == 0b011) physicalRegisterName = "REG_A";
        else if (register_number == 0b100) physicalRegisterName = "REG_B";
        else if (register_number == 0b101) physicalRegisterName = "REG_C";
        else if (register_number == 0b110) physicalRegisterName = "REG_D";
        else                               physicalRegisterName = "IPLOC";

        if (virtual_register)
        {
            return "I_"+physicalRegisterName;
        }
        else
        {
            return physicalRegisterName;
        }
    }

    public boolean hasWritePermissions() {
        return !(virtual_register || register_number == 0);
    }

    public int getRegisterNumber() {
        return register_number;
    }

    public boolean isVirtualRegister() {
        return virtual_register;
    }
}
