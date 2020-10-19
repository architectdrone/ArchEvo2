package org.architectdrone.archevo.universe;

import lombok.EqualsAndHashCode;
import org.architectdrone.archevo.combathandler.CaptureTheFlag;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.reproductionhandler.SetCost;
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
                0,
                new CaptureTheFlag(),
                new SetCost(64),
                0.3f,
                3,
                32,
                16, 42069);
        universe.iterate();
        assertEquals(3, universe.getCellContainer().getAll().size());
    }
}