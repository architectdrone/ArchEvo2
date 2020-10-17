package org.architectdrone.archevo.isa.asia;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import org.architectdrone.archevo.isa.MalformedInstructionException;
import org.architectdrone.archevo.isa.general.GeneToTemplateComponent;
import org.architectdrone.archevo.isa.general.TemplateGroup;

public class ASIAJumpHandler {
    public static int getBestJumpLocation(ASIACachedData asiaCachedData, int start_location) {

        Integer move_instruction_pointer_location = asiaCachedData.jumpMap.get(start_location);
        if (move_instruction_pointer_location == null)
        {
            return start_location+1;
        }
        return move_instruction_pointer_location;
    }

    public static int getEndOfTemplate(ASIACachedData asiaCachedData, int start_location) {
        Integer move_instruction_pointer_location = asiaCachedData.endOfTemplateMap.get(start_location);
        if (move_instruction_pointer_location == null)
        {
            return start_location+1;
        }
        return move_instruction_pointer_location;
    }
}

