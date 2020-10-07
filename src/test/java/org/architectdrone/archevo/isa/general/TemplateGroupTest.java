package org.architectdrone.archevo.isa.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateGroupTest {
    GeneToTemplateComponent dummyGeneToTemplateComponent = (gene) -> gene;

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoIdenticalTemplates() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // 1 -> 2, 2 -> 1

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(12, 1);
        genome.set(13, 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(14, result.get(7));
        assertEquals(10, result.get(11));
    }

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoIdenticalTemplates_oneOnTheEnd() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // 1 -> 2, 2 -> 1

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(15, 1);
        genome.set(0 , 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(1, result.get(7));
        assertEquals(10, result.get(14));
    }

    @Test
    void toJumpMap_correctMapsToNearestNeighbor_givenThreeIdenticalGenomes() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // 1 -> 2, 2 -> 3, 3 -> 1

        //Group 1
        genome.set(3, 1);
        genome.set(4, 2);

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(12, 1);
        genome.set(13, 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(10, result.get(2));
        assertEquals(14, result.get(7));
        assertEquals(5, result.get(11));
    }

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoDifferentTemplates() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(12, 2);
        genome.set(13, 1);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(14, result.get(7));
        assertEquals(10, result.get(11));
    }

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoDifferentOneSame() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // Tests that it effectively prioritizes obviously high scoring groups.

        // 1 -> 3, 2 -> 3, 3 -> 1

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(12, 2);
        genome.set(13, 1);

        //Group 3
        genome.set(3, 1);
        genome.set(4, 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(5, result.get(7));
        assertEquals(5, result.get(11));
        assertEquals(10, result.get(2));
    }

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoMostlyDifferentOneSame() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // Tests that it effectively prioritizes not so obviously high scoring groups.

        // 1 -> 3, 2 -> 3, 3 -> 1

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(12, 1);
        genome.set(13, 1);

        //Group 3
        genome.set(3, 1);
        genome.set(4, 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(5, result.get(7));
        assertEquals(5, result.get(11));
        assertEquals(10, result.get(2));
    }

    @Test
    void toJumpMap_correctMapsInputToOutput_givenTwoIdenticalExceptForLengthAndOneDifferent() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        // 1 -> 3, 2 -> 3, 3 -> 1

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);
        genome.set(10, 1);

        //Group 2
        genome.set(12, 1);
        genome.set(13, 1);

        //Group 3
        genome.set(3, 1);
        genome.set(4, 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getJumpMap();
        assertEquals(5, result.get(7));
        assertEquals(5, result.get(11));
        assertEquals(11, result.get(2));
    }

    @Test
    void toEndMap_worksCorrectly() {
        List<Integer> genome = new ArrayList<>(Collections.nCopies(16, 0));

        //Group 1
        genome.set(8, 1);
        genome.set(9, 2);

        //Group 2
        genome.set(15, 1);
        genome.set(0 , 2);

        HashMap<Integer, Integer> result = new TemplateGroup(genome, dummyGeneToTemplateComponent).getTemplateEndMap();
        assertEquals(10, result.get(7));
        assertEquals(1, result.get(14));
    }
}