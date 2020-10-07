package org.architectdrone.archevo.isa.general;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a list of templates.
 */
public class TemplateGroup {
    private final List<Template> template_groups;
    private final List<Integer>  genome;

    /**
     * Constructor that converts a genome to a template.
     * @param genome The genome to convert.
     * @param geneToTemplateComponent A function that converts a an element of the genome into
     */
    public TemplateGroup(List<Integer> genome, GeneToTemplateComponent geneToTemplateComponent) {
        this.genome = genome.stream().map(geneToTemplateComponent::f).collect(Collectors.toList());
        this.template_groups = getNOPGroups();
    }

    private int wrapAroundGenome(int input)
    {
        return Math.floorMod(input, genome.size());
    }

    private List<Template> getNOPGroups() {
        List<Template> toPopulate = new ArrayList<>();
        // Find first component that is not a template component
        Integer first_non_template_index = null;
        for (int i = 0; i < this.genome.size(); i++)
        {
            if (genome.get(i) == 0)
            {
                first_non_template_index = i;
                break;
            }
        }

        if (first_non_template_index == null)
            throw new AssertionError();

        // Start at first non-template component.
        boolean in_template = false;
        Integer current_template_start_index = null;

        for (int i = 0; i < this.genome.size(); i++)
        {
            int current_index = wrapAroundGenome(first_non_template_index+ i);
            int current_template_element = genome.get(current_index);
            if (current_template_element != 0)
            {
                if (!in_template)
                {
                    current_template_start_index = current_index;
                    in_template = true;
                }
            }
            else
            {
                if (in_template)
                {
                    int current_template_end_index = wrapAroundGenome(current_index-1);
                    Template new_template = new Template(current_template_start_index, current_template_end_index);
                    toPopulate.add(new_template);
                    in_template = false;
                }
            }
        }

        if (in_template)
        {
            int current_template_end_index = first_non_template_index-1;
            Template new_template = new Template(current_template_start_index, current_template_end_index);
            toPopulate.add(new_template);
        }

        return toPopulate;
    }

    private float compareTwoGroups(Template original, Template current)
    {
        int points = 0;
        int offset = 0;

        while (true)
        {
            int element_in_first_group   = genome.get(wrapAroundGenome(original.first_index+offset));
            int element_in_current_group = genome.get(wrapAroundGenome(current.first_index+offset));
            if (element_in_current_group == element_in_first_group)
            {
                points++;
            }
            offset++;

            int original_length = original.length(genome.size());
            int current_length  =  current.length(genome.size());
            boolean offset_is_past_the_end_of_original = original_length < offset;
            boolean offset_is_past_the_end_of_current  =  current_length < offset;
            if (!offset_is_past_the_end_of_original && !offset_is_past_the_end_of_current) {
                continue;
            }
            break;
        }
        return (float) points / (float) original.length(genome.size()-1);
    }

    /**
     * converts the list of templates into a jump map.
     * A jump map is a hash map that associates a location on a genome with the resulting positions on the genome, if the jump is followed.
     * In our case, it associates the index directly before a template with the index at the end of the template that most closely resembles the original.
     * @return A jump map, as described above.
     */
    public HashMap<Integer, Integer> getJumpMap()
    {
        HashMap<Integer, Integer> to_return = new HashMap<Integer, Integer>();
        for (Template a : template_groups)
        {
            Integer best_distance = null;
            float best_score = 0.0f;
            Template best_template = null;
            for (Template b : template_groups)
            {
                if (b != a)
                {
                    float current_score = compareTwoGroups(a, b);
                    if (current_score > best_score)
                    {
                        best_score = current_score;
                        best_template = b;
                        best_distance = modular_length(a.last_index, b.first_index, genome.size());
                    }
                    else if (current_score == best_score)
                    {

                        int distance = modular_length(a.last_index, b.first_index, genome.size());
                        if (best_distance == null || distance < best_distance)
                        {
                            best_distance = distance;
                            best_template = b;
                        }
                    }
                }
            }
            if (best_template != null)
            {
                to_return.put(wrapAroundGenome(a.first_index-1), wrapAroundGenome(best_template.last_index+1));
            }
            else
            {
                to_return.put(wrapAroundGenome(a.first_index-1), wrapAroundGenome(a.last_index+1));
            }
        }
        return to_return;
    }

    /**
     * Converts the list of templates into a template end map.
     * A template end map is a map that associates the start of a template with the end of that template.
     * @return The template end map
     */
    public HashMap<Integer, Integer> getTemplateEndMap() {
        HashMap<Integer, Integer> toReturn = new HashMap<>();
        for (Template a : template_groups)
        {
            toReturn.put(Math.floorMod(a.first_index-1, genome.size()),Math.floorMod(a.last_index+1, genome.size()));
        }
        return toReturn;
    }

    public static int modular_length(int start, int end, int size)
    {
        if (end > start)
            return end - start;
        else
            return (size - start) + end;
    }
}

class Template {
    public final int first_index;
    public final int last_index;

    public Template(int first_index, int last_index)
    {
        this.first_index = first_index;
        this.last_index = last_index;
    }

    public int length(int genome_size)
    {
        return TemplateGroup.modular_length(first_index, last_index, genome_size);
    }
}