package org.architectdrone.archevo.isa.asia;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.general.GeneToTemplateComponent;
import org.architectdrone.archevo.isa.general.TemplateGroup;

public class ASIAJumpHandler {
    public static int getBestJumpLocation(List<Integer> genome, int start_location) {
        GeneToTemplateComponent geneToTemplateComponent = (gene) -> {
            if      (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_A) return 1;
            else if (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_B) return 2;
            else return 0;
        };
        Integer move_instruction_pointer_location = new TemplateGroup(genome, geneToTemplateComponent).getJumpMap().get(start_location);
        if (move_instruction_pointer_location == null)
        {
            return start_location+1;
        }
        return move_instruction_pointer_location;
    }

    public static int getEndOfTemplate(List<Integer> genome, int start_location) {
        GeneToTemplateComponent geneToTemplateComponent = (gene) -> {
            if      (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_A) return 1;
            else if (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_B) return 2;
            else return 0;
        };

        Integer move_instruction_pointer_location = new TemplateGroup(genome, geneToTemplateComponent).getTemplateEndMap().get(start_location);
        if (move_instruction_pointer_location == null)
        {
            return start_location+1;
        }
        return move_instruction_pointer_location;
    }
}

