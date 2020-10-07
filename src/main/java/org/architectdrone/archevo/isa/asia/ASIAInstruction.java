package org.architectdrone.archevo.isa.asia;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.ParsingException;

public class ASIAInstruction {
    static final int UNWRITABLE_REGISTER = 0b0000; //ENERGY
    static final int   WRITABLE_REGISTER = 0b0001; //GUESS

    private final ASIARegister register1;
    private final ASIARegister register2;
    private final ASIAOperation operation;

    /**
     * Constructor for the ASIAInstruction class
     * @param operation The operation
     * @param register1 The first register
     * @param register2 The second register
     */
    public ASIAInstruction(final ASIAOperation operation, final ASIARegister register1, final ASIARegister register2) throws MalformedInstructionException {
        if (operation.isROP() && !register1.hasWritePermissions()) throw new MalformedInstructionException("ROP instructions require that a writable instruction be in the R1 position");
        this.register1 = register1;
        this.register2 = register2;
        this.operation = operation;
    }

    /**
     * Converts from binary to an opreation.
     * @param binary The binary to convert
     * @return The new operation
     */
    public static ASIAInstruction fromBinary(int binary) {
        int operation_binary = (0b11100000000 & binary) >> 8;
        int register2_binary = (0b00011110000 & binary) >> 4;
        int register1_binary = (0b00000001111 & binary);

        ASIARegister register1  = ASIARegister.fromBinary(register1_binary);
        ASIARegister register2  = ASIARegister.fromBinary(register2_binary);
        ASIAOperation operation = ASIAOperation.fromBinaryAndRegister1(operation_binary, register1);
        try
        {
            return new ASIAInstruction(operation, register1, register2);
        } catch (MalformedInstructionException e) { //This should never happen, because every series of 11 bits should correspond with a well-formed instruction. If this is not so, ASIA as a whole is broken.
            e.printStackTrace();
            return null;
        }
    }

    public int toBinary() {
        int operation_mask = (operation.toBinary() << 8);
        int register1_mask = (register1.toBinary());
        int register2_mask = (register2.toBinary() << 4);

        return operation_mask | register1_mask | register2_mask;

    }

    public static ASIAInstruction fromString(String string) throws ParsingException, MalformedInstructionException {
        List<String> split_string = Arrays.asList(string.split(" "));
        String operation_string = split_string.get(0);
        ASIAOperation parsed_operation = ASIAOperation.fromString(operation_string);
        if (parsed_operation.getNumberOfRegisters() != split_string.size() - 1) //Not enougn registers are provided
            throw new MalformedInstructionException(operation_string, parsed_operation.getNumberOfRegisters(), split_string.size() - 1);

        ASIARegister parsed_register1;
        ASIARegister parsed_register2;

        if (parsed_operation.getNumberOfRegisters() == 2)
        {
            parsed_register1 = ASIARegister.fromString(split_string.get(1));
            parsed_register2 = ASIARegister.fromString(split_string.get(2));
        }
        else if (parsed_operation.getNumberOfRegisters() == 1)
        {
            if (parsed_operation.isROP())
            {
                parsed_register1 = ASIARegister.fromString(split_string.get(1));
                parsed_register2 = ASIARegister.fromBinary(WRITABLE_REGISTER);
            }
            else
            {
                parsed_register1 = ASIARegister.fromBinary(UNWRITABLE_REGISTER);
                parsed_register2 = ASIARegister.fromString(split_string.get(1));
            }
        }
        else
        {
            parsed_register1 = ASIARegister.fromBinary(UNWRITABLE_REGISTER);
            parsed_register2 =   ASIARegister.fromBinary(WRITABLE_REGISTER);
        }

        if (!parsed_register1.hasWritePermissions() && parsed_operation.isROP())
        {
            throw new MalformedInstructionException("ROP instructions require that a writable instruction be in the R1 position");
        }

        return new ASIAInstruction(parsed_operation, parsed_register1, parsed_register2);
    }

    public String toString() {
        if (operation.getNumberOfRegisters() == 2)
        {
            return operation.toString() + " " + register1.toString() + " " + register2.toString();
        }
        else if (operation.getNumberOfRegisters() == 1)
        {
            if (operation.isROP())
                return operation.toString() + " " + register1.toString();
            else
                return operation.toString() + " " + register2.toString();
        }
        else
        {
            return operation.toString();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final ASIAInstruction that = (ASIAInstruction) o;

        //First, compare the two operations.
        if (!operation.equals(that.getOperation())) return false;

        if (operation.getNumberOfRegisters() == 2)
        {
            if (!(register1.equals(that.getRegister1())) || !(register2.equals(that.getRegister2()))) return false;
        }
        else if (operation.getNumberOfRegisters() == 1)
        {
            if (operation.isROP()  && !register1.equals(that.getRegister1())) return false;
            if (!operation.isROP() && !register2.equals(that.getRegister2())) return false;
        }

        return true;

    }

    public ASIARegister getRegister1() {
        return register1;
    }

    public ASIARegister getRegister2() {
        return register2;
    }

    public ASIAOperation getOperation() {
        return operation;
    }
}
