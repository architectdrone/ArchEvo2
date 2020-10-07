package org.architectdrone.archevo.isa.general;

import org.architectdrone.archevo.isa.MalformedInstructionException;

/**
 * Function that converts a gene into a component of a template.
 * If a gene is not a member of a template, this should return a 0.
 * Otherwise, it should be given the same value as other genes that you want it to group with.
 */
public interface GeneToTemplateComponent {
    public int f(int gene);
}
