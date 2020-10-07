package org.architectdrone.archevo.cell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

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
    void onInitialization_IPIsZero() {
        assertEquals(0, test_cell.getIP());
    }

    @Test
    void onInitialization_allRegistersAreZero() {
        assertEquals(0, test_cell.getRegister(0));
        assertEquals(0, test_cell.getRegister(1));
        assertEquals(0, test_cell.getRegister(2));
        assertEquals(0, test_cell.getRegister(3));
        assertEquals(0, test_cell.getRegister(4));
        assertEquals(0, test_cell.getRegister(5));
        assertEquals(0, test_cell.getRegister(6));
        assertEquals(0, test_cell.getRegister(7));
    }

    @Test
    void onInitialization_genomeIsWhatIsPassedIntoConstructor() {
        assertTrue(test_genome.equals(test_cell.getGenome()));
    }

    @Test
    void gettingNegativeRegister_throwsAssertationError() {
        assertThrows(AssertionError.class, () -> test_cell.getRegister(-1));
    }

    @Test
    void gettingRegisterGreaterThanSeven_throwsAssertationError() {
        assertThrows(AssertionError.class, () -> test_cell.getRegister(8));
    }

    @Test
    void settingARegister_works() {
        int reg_to_set = 2;
        int new_value = 4;
        test_cell.setRegister(reg_to_set, new_value);
        assertEquals(test_cell.getRegister(reg_to_set), new_value);
    }

    @Test
    void settingARegisterWhenNegativeIsGiven_wrapsCorrectly() {
        int reg_to_set = 2;
        int new_value = -1;
        test_cell.setRegister(reg_to_set, new_value);
        assertEquals(0xFF, test_cell.getRegister(reg_to_set));
    }

    @Test
    void settingARegisterWhenOverMaxIsGiven_wrapsCorrectly() {
        int reg_to_set = 2;
        int new_value = (0xFF)+1;
        test_cell.setRegister(reg_to_set, new_value);
        assertEquals(0, test_cell.getRegister(reg_to_set));
    }

    @Test
    void settingNegativeRegister_throwsAssertationError() {
        assertThrows(AssertionError.class, () -> test_cell.setRegister(-1, 5));
    }

    @Test
    void settingRegisterGreaterThanSeven_throwsAssertationError() {
        assertThrows(AssertionError.class, () -> test_cell.setRegister(8, 5));
    }

    @Test
    void settingIP_works() {
        test_cell.setIP(5);
        assertEquals(5, test_cell.getIP());
    }

    @Test
    void settingIPWhenLessThanZero_wrapsCorrectly() {
        test_cell.setIP(-1);
        assertEquals(test_genome.size()-1, test_cell.getIP());
    }

    @Test
    void settingIPWhenGreaterThanListSize_wrapsCorrectly() {
        test_cell.setIP(test_genome.size());
        assertEquals(0, test_cell.getIP());
    }
}