package org.architectdrone.archevo.universe;

import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import org.architectdrone.archevo.combathandler.CaptureTheFlag;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.reproductionhandler.SetCost;
import org.architectdrone.archevo.universe.cellcontainer.CellPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EqualsAndHashCode
class UniverseTest {
    @Test
    void influxRate_isCorrect() throws Exception {
        Universe universe = new Universe(new ASIA(),
                50,
                IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION,
                1,
                new CaptureTheFlag(),
                new SetCost(),
                0.3f,
                3,
                42069);
        universe.iterate();
        assertEquals(3, universe.getCellContainer().getAll().size());
    }
}