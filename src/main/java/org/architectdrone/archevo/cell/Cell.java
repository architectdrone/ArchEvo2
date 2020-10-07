package org.architectdrone.archevo.cell;

import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.action.MoveInstructionPointer;
import org.architectdrone.archevo.action.RegisterUpdate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;


public class Cell {
    final private int MAX_REGISTER_VALUE = 0xFF;

    private final List<Integer> registers; //There should only be 8 of these
    private int IP = 0; //Instruction pointer
    private final List<Integer> genome;

    public Cell(@NotNull List<Integer> genome) {
        this.registers = new ArrayList<Integer>(Collections.nCopies(8, 0));
        this.genome = genome;
    }

    private Cell(@NotNull List<Integer> genome, @NotNull List<Integer> registers, int IP)
    {
        this.registers = registers;
        this.IP = IP;
        this.genome = genome;
    }

    /**
     * Returns the register with the given registerNumber
     * @param registerNumber The register to get. Must not be less than zero or greater than seven.
     * @return the register
     */
    public int getRegister(int registerNumber)
    {
        assert registerNumber >= 0;
        assert registerNumber <= 7;
        return registers.get(registerNumber);
    }

    /**
     * Sets the register with the given registerNumber
     * @param registerNumber The register of the number to change. Must not be less than zero or greater than seven.
     * @param newRegisterValue The new value of the register. Will be wrapped in the range from 0x00 to 0xFF.
     */
    public void setRegister(int registerNumber, int newRegisterValue) {
        assert registerNumber >= 0;
        assert registerNumber <= 7;
        registers.set(registerNumber, Math.floorMod(newRegisterValue, MAX_REGISTER_VALUE+1));
    }

    /**
     * Gets the genome
     * @return The genome
     */
    public List<Integer> getGenome() {
        return genome;
    }

    /**
     * Gets the instruction pointer
     * @return the instruction pointer
     */
    public int getIP() {
        return IP;
    }

    /**
     * Sets the instruction pointer
     * @param IP the new instruction pointer. Wraps to the range 0 to length of genome
     */
    public void setIP(int IP) {
        this.IP = Math.floorMod(IP, genome.size());
    }
}
