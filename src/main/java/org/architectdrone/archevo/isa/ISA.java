package org.architectdrone.archevo.isa;

import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.misc.OffsetToCell;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

public interface ISA {
    /**
     * Returns the action associated with the current position of an instruction pointer.
     * @return
     */
    public Action getAction(@NotNull Cell currentCell, @NotNull OffsetToCell offsetToCell );

    /**
     * Gets the number of bits in a single instruction.
     * @return the number of bits in a single instruction.
     */
    public int getNumberOfBitsPerInstruction();

    /**
     * Converts a string representing an instruction into a bitset.
     * @param instruction The instruction to convert
     * @return The bitset
     */
    public BitSet stringToBits(@NotNull String instruction);

    /**
     * Converts a bitset into a string.
     * @param instruction the bitset
     * @return the resulting string
     */
    public String bitsToString(@NotNull BitSet instruction);
}
