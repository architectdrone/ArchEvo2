package org.architectdrone.archevo.isa.asia;

import java.util.HashMap;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.cell.ISACachedData;
import org.architectdrone.archevo.cell.ISACachedDataGenerator;
import org.architectdrone.archevo.isa.general.GeneToTemplateComponent;
import org.architectdrone.archevo.isa.general.TemplateGroup;

public class ASIACachedDataGenerator implements ISACachedDataGenerator {
    @Override
    public ISACachedData getCachedData(final Cell cell) {
        GeneToTemplateComponent geneToTemplateComponent = (gene) -> {
            if      (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_A) return 1;
            else if (ASIAInstruction.fromBinary(gene).getOperation().getASIAOperationType() == ASIAOperationType.NOP_B) return 2;
            else return 0;
        };
        TemplateGroup templateGroup = new TemplateGroup(cell.getGenome(), geneToTemplateComponent);
        return new ASIACachedData(templateGroup.getJumpMap(), templateGroup.getTemplateEndMap());
    }
}
