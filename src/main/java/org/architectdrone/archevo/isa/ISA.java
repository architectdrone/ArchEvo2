package org.architectdrone.archevo.isa;

import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.cell.ISACachedDataGenerator;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

public interface ISA {
    /**
     * Returns the action associated with the current position of an instruction pointer.
     * @param currentCell  The cell to get the action for.
     * @param offsetToCell A function that converts an offset from the current cell's position to a cell that may be at that location. May be null.
     * @return The action that should be executed next.
     */
    public Action getAction(@NotNull Cell currentCell, @NotNull OffsetToCell offsetToCell );

    /**
     * Gets the number of bits in a single instruction.
     * @return the number of bits in a single instruction.
     */
    public int getNumberOfBitsPerInstruction();

    /**
     * Converts an instruction, written as a string, to binary.
     * @param instruction The instruction to convert
     * @return The bitset.
     * @throws ParsingException If a token in the instruction is not found.
     * @throws MalformedInstructionException If an instruction violates a rule about the ISA.
     */
    public Integer stringToBinary(@NotNull String instruction) throws ParsingException, MalformedInstructionException;

    /**
     * Converts binary into a string.
     * @param instruction the bitset
     * @return the resulting string
     */
    public String binaryToString(@NotNull Integer instruction);

    /**
     * Gets the cached data generator.
     * @return The cached data generator.
     */
    public ISACachedDataGenerator getISACachedDataGenerator();
}
