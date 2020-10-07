package org.architectdrone.archevo.isa.asia;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASIARegisterTest {
    List<Integer> test_genome;
    Cell test_cell;
    int test_number_of_genes = 32;

    @BeforeEach
    void init() {
        test_genome = new ArrayList<>();
        for (int i = 0; i < test_number_of_genes; i++)
        {
            test_genome.add(0b11101111);
        }

        test_cell = new Cell(test_genome);
    }

    @Test
    void constructor_works() {
        ASIARegister test_ASIA_register = new ASIARegister(3, true);
        assertEquals(3, test_ASIA_register.getRegisterNumber());
        assertEquals(true, test_ASIA_register.isVirtualRegister());
    }

    @Test
    void constructor_whenGivenNegativeRegister_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new ASIARegister(-1, true));
    }

    @Test
    void constructor_whenGivenRegisterGreaterThanSeven_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new ASIARegister(8, true));
    }

    @Test
    void bitsToRegister_whenGivenGreaterThan15_throwsAssertationError() {
        int more_than_4_bits = 0b10000;
        assertThrows(AssertionError.class, () -> ASIARegister.fromBinary(more_than_4_bits));
    }

    @Test
    void bitsToRegister_whenGivenLessThan0_throwsAssertationError() {
        int more_than_4_bits = 0b10000;
        assertThrows(AssertionError.class, () -> ASIARegister.fromBinary(more_than_4_bits));
    }

    @Test
    void bitsToRegister_whenGivenPhysicalRegister_works(){
        int physical_register = 0b0110;
        ASIARegister asiaRegister = ASIARegister.fromBinary(physical_register);
        assertEquals(false, asiaRegister.isVirtualRegister());
        assertEquals(0b110 , asiaRegister.getRegisterNumber());
    }

    @Test
    void bitsToRegister_correctlyIdentifiesVirtualRegister() {
        assertEquals(false, ASIARegister.fromBinary(0b0000).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0001).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0010).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0011).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0100).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0101).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0110).isVirtualRegister());
        assertEquals(false, ASIARegister.fromBinary(0b0111).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1000).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1001).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1010).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1011).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1100).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1101).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1110).isVirtualRegister());
        assertEquals(true,  ASIARegister.fromBinary(0b1111).isVirtualRegister());
    }

    @Test
    void bitsToRegister_correctlyGetsRegisterNumber() {
        assertEquals(0b000, ASIARegister.fromBinary(0b0000).getRegisterNumber() );
        assertEquals(0b001, ASIARegister.fromBinary(0b0001).getRegisterNumber() );
        assertEquals(0b010, ASIARegister.fromBinary(0b0010).getRegisterNumber() );
        assertEquals(0b011, ASIARegister.fromBinary(0b0011).getRegisterNumber() );
        assertEquals(0b100, ASIARegister.fromBinary(0b0100).getRegisterNumber() );
        assertEquals(0b101, ASIARegister.fromBinary(0b0101).getRegisterNumber() );
        assertEquals(0b110, ASIARegister.fromBinary(0b0110).getRegisterNumber() );
        assertEquals(0b111, ASIARegister.fromBinary(0b0111).getRegisterNumber() );
        assertEquals(0b000, ASIARegister.fromBinary(0b1000).getRegisterNumber() );
        assertEquals(0b001, ASIARegister.fromBinary(0b1001).getRegisterNumber() );
        assertEquals(0b010, ASIARegister.fromBinary(0b1010).getRegisterNumber() );
        assertEquals(0b011, ASIARegister.fromBinary(0b1011).getRegisterNumber() );
        assertEquals(0b100, ASIARegister.fromBinary(0b1100).getRegisterNumber() );
        assertEquals(0b101, ASIARegister.fromBinary(0b1101).getRegisterNumber() );
        assertEquals(0b110, ASIARegister.fromBinary(0b1110).getRegisterNumber() );
        assertEquals(0b111, ASIARegister.fromBinary(0b1111).getRegisterNumber() );
    }

    @Test
    void convertToVirtual_throwsErrorIfAlreadyPhysical() {
        ASIARegister asiaRegister = new ASIARegister(3, false);
        assertThrows(AssertionError.class, asiaRegister::convertToPhysical);
    }

    @Test
    void convertToVirtual_ConvertsToPhysical() {
        ASIARegister asiaRegister = new ASIARegister(3, true).convertToPhysical();
        assertEquals(false, asiaRegister.isVirtualRegister());
        assertEquals(3, asiaRegister.getRegisterNumber());
    }

    @Test
    void getValue_throwsAnErrorIfRegisterIsVirtual(){
        ASIARegister asiaRegister = new ASIARegister(3, true);
        assertThrows(AssertionError.class, () -> asiaRegister.getValue(test_cell));
    }

    @Test
    void getValue_returnsCorrectValue(){
        ASIARegister asiaRegister = new ASIARegister(3, false);
        test_cell.setRegister(3, 5);
        assertEquals(5, asiaRegister.getValue(test_cell));
    }

    @Test
    void getBinary_returnsTheCorrectBinaryInterpretation() {
        assertEquals(0b0000, ASIARegister.fromBinary(0b0000).toBinary());
        assertEquals(0b0001, ASIARegister.fromBinary(0b0001).toBinary());
        assertEquals(0b0010, ASIARegister.fromBinary(0b0010).toBinary());
        assertEquals(0b0011, ASIARegister.fromBinary(0b0011).toBinary());
        assertEquals(0b0100, ASIARegister.fromBinary(0b0100).toBinary());
        assertEquals(0b0101, ASIARegister.fromBinary(0b0101).toBinary());
        assertEquals(0b0110, ASIARegister.fromBinary(0b0110).toBinary());
        assertEquals(0b0111, ASIARegister.fromBinary(0b0111).toBinary());
        assertEquals(0b1000, ASIARegister.fromBinary(0b1000).toBinary());
        assertEquals(0b1001, ASIARegister.fromBinary(0b1001).toBinary());
        assertEquals(0b1010, ASIARegister.fromBinary(0b1010).toBinary());
        assertEquals(0b1011, ASIARegister.fromBinary(0b1011).toBinary());
        assertEquals(0b1100, ASIARegister.fromBinary(0b1100).toBinary());
        assertEquals(0b1101, ASIARegister.fromBinary(0b1101).toBinary());
        assertEquals(0b1111, ASIARegister.fromBinary(0b1111).toBinary());
    }

    @Test
    void fromString_getsTheCorrectRegisterNumber() throws ParsingException {
        assertEquals(0b000, ASIARegister.fromString("ENERGY").getRegisterNumber());
        assertEquals(0b001, ASIARegister.fromString("LOGO").getRegisterNumber());
        assertEquals(0b010, ASIARegister.fromString("GUESS").getRegisterNumber());
        assertEquals(0b011, ASIARegister.fromString("REG_A").getRegisterNumber());
        assertEquals(0b100, ASIARegister.fromString("REG_B").getRegisterNumber());
        assertEquals(0b101, ASIARegister.fromString("REG_C").getRegisterNumber());
        assertEquals(0b110, ASIARegister.fromString("REG_D").getRegisterNumber());
        assertEquals(0b111, ASIARegister.fromString("IPLOC").getRegisterNumber());
        assertEquals(0b000, ASIARegister.fromString("I_ENERGY").getRegisterNumber());
        assertEquals(0b001, ASIARegister.fromString("I_LOGO").getRegisterNumber());
        assertEquals(0b010, ASIARegister.fromString("I_GUESS").getRegisterNumber());
        assertEquals(0b011, ASIARegister.fromString("I_REG_A").getRegisterNumber());
        assertEquals(0b100, ASIARegister.fromString("I_REG_B").getRegisterNumber());
        assertEquals(0b101, ASIARegister.fromString("I_REG_C").getRegisterNumber());
        assertEquals(0b110, ASIARegister.fromString("I_REG_D").getRegisterNumber());
        assertEquals(0b111, ASIARegister.fromString("I_IPLOC").getRegisterNumber());
    }

    @Test
    void fromString_getsTheCorrectValueOfIsVirtual() throws ParsingException {
        assertEquals(false, ASIARegister.fromString("ENERGY").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("LOGO").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("GUESS").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("REG_A").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("REG_B").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("REG_C").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("REG_D").isVirtualRegister());
        assertEquals(false, ASIARegister.fromString("IPLOC").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_ENERGY").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_LOGO").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_GUESS").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_REG_A").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_REG_B").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_REG_C").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_REG_D").isVirtualRegister());
        assertEquals(true, ASIARegister.fromString("I_IPLOC").isVirtualRegister());
    }

    @Test
    void fromString_throwsErrorIfStringDoesNotCorrespondToRegister() throws ParsingException {
        assertThrows(ParsingException.class, () -> ASIARegister.fromString("NOT_AN_ACTUAL_REGISTER"));
    }

    @Test
    void toString_getsTheCorrectString() {
        assertEquals("ENERGY",   new ASIARegister(0b000, false).toString());
        assertEquals("LOGO",     new ASIARegister(0b001, false).toString());
        assertEquals("GUESS",    new ASIARegister(0b010, false).toString());
        assertEquals("REG_A",    new ASIARegister(0b011, false).toString());
        assertEquals("REG_B",    new ASIARegister(0b100, false).toString());
        assertEquals("REG_C",    new ASIARegister(0b101, false).toString());
        assertEquals("REG_D",    new ASIARegister(0b110, false).toString());
        assertEquals("IPLOC",    new ASIARegister(0b111, false).toString());
        assertEquals("I_ENERGY", new ASIARegister(0b000, true).toString());
        assertEquals("I_LOGO",   new ASIARegister(0b001, true).toString());
        assertEquals("I_GUESS",  new ASIARegister(0b010, true).toString());
        assertEquals("I_REG_A",  new ASIARegister(0b011, true).toString());
        assertEquals("I_REG_B",  new ASIARegister(0b100, true).toString());
        assertEquals("I_REG_C",  new ASIARegister(0b101, true).toString());
        assertEquals("I_REG_D",  new ASIARegister(0b110, true).toString());
        assertEquals("I_IPLOC",  new ASIARegister(0b111, true).toString());
    }

    @Test
    void hasWritePermissions_returnsCorrectly() {
        assertEquals(false, ASIARegister.fromBinary(0b0000).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0001).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0010).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0011).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0100).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0101).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0110).hasWritePermissions() );
        assertEquals(true,  ASIARegister.fromBinary(0b0111).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1000).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1001).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1010).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1011).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1100).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1101).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1110).hasWritePermissions() );
        assertEquals(false, ASIARegister.fromBinary(0b1111).hasWritePermissions() );
    }
}