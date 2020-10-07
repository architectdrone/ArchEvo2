package org.architectdrone.archevo.isa.asia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.junit.jupiter.api.Assertions.*;

class ASIAJumpHandlerTest {

    int jump;
    int nop_a;
    int nop_b;
    int unassigned;
    @BeforeEach
    void beforeEach() throws ParsingException, MalformedInstructionException {
        jump = ASIAInstruction.fromString("JUMP").toBinary();
        nop_a = ASIAInstruction.fromString("NOP_A").toBinary();
        nop_b = ASIAInstruction.fromString("NOP_B").toBinary();
        unassigned = ASIAInstruction.fromString("UNASSIGNED").toBinary();

    }

    @Test
    void whenOneOriginalTemplate_andOneOtherTemplate_match_jumpToEndOfOtherTemplate() throws ParsingException, MalformedInstructionException {
        //Create Genome
        List<Integer> genome = getEmptyGenome(16);

        //Put a jump at 8, followed by nop_a and nop_b
        genome.set(8 , jump);
        genome.set(9 , nop_a);
        genome.set(10, nop_b);

        //Put the other template at 12.
        genome.set(12, nop_a);
        genome.set(13, nop_b);

        //If we jump to the end of the second template, we should end up at 14.
        assertEquals(14, ASIAJumpHandler.getBestJumpLocation(genome, 8));
    }

    @Test
    void whenOneOriginalTemplate_andOneOtherTemplate_doNotMatch_jumpToEndOfOtherTemplate() throws ParsingException, MalformedInstructionException {
        //Create Genome
        List<Integer> genome = getEmptyGenome(16);

        //Put a jump at 8, followed by nop_a and nop_b
        genome.set(8 , jump);
        genome.set(9 , nop_a);
        genome.set(10, nop_b);

        //Put the other template at 12.
        genome.set(12, nop_b);
        genome.set(13, nop_a);

        //If we jump to the end of the second template, we should end up at 14.
        assertEquals(14, ASIAJumpHandler.getBestJumpLocation(genome, 8));
    }

    @Test
    void whenOneOriginalTemplate_andOneOtherTemplateThatIsTheSameButDividedBetweenStartAndEndOfGenome_match_jumpToEndOfOtherTemplate() throws ParsingException, MalformedInstructionException {
        //Create Genome
        List<Integer> genome = getEmptyGenome(16);

        //Put a jump at 8, followed by nop_a and nop_b
        genome.set(8 , jump);
        genome.set(9 , nop_a);
        genome.set(10, nop_b);

        //Put the other template at 15 and 0.
        genome.set(15, nop_a);
        genome.set(0 , nop_b);

        //If we jump to the end of the second template, we should end up at 14.
        assertEquals(1, ASIAJumpHandler.getBestJumpLocation(genome, 8));
    }

    @Test
    void whenOneOriginalTemplate_andTwoOtherTemplates_exist_prioritizeBetterMatch() throws ParsingException, MalformedInstructionException {
        //Create Genome
        List<Integer> genome = getEmptyGenome(16);

        //Put a jump at 8, followed by nop_a and nop_b
        genome.set(8 , jump);
        genome.set(9 , nop_a);
        genome.set(10, nop_b);

        //Put the first template at 12 and 13.
        genome.set(12, nop_a);
        genome.set(13 , nop_a);

        //Put the other (better match) template at 15 and 0.
        genome.set(15, nop_a);
        genome.set(0 , nop_b);

        //If we jump to the end of the second template, we should end up at 14.
        assertEquals(1, ASIAJumpHandler.getBestJumpLocation(genome, 8));
    }

    private List<Integer> getEmptyGenome(int size) throws ParsingException {
        return new ArrayList<Integer>(Collections.nCopies(size, unassigned));
    }
}