package org.architectdrone.archevo.apps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.architectdrone.archevo.combathandler.CaptureTheFlag;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.reproductionhandler.SetCost;
import org.architectdrone.archevo.runner.Task;
import org.architectdrone.archevo.runner.UniverseRunner;
import org.architectdrone.archevo.universe.IterationExecutionMode;
import org.architectdrone.archevo.universe.Universe;

public class CLI {
    public static void main(String[] args) throws Exception {
        Task update = new Task((a) -> {
            List<Integer> lineages = a.getCellContainer().getAll().stream().map((cell) -> cell.cellStats.lineage).sorted().collect(Collectors.toList());
            Integer longest_lineage = null;
            if (lineages.size() > 0)
            {
                longest_lineage = lineages.get(lineages.size()-1);
            }
            List<Integer> ages = a.getCellContainer().getAll().stream().map((cell) -> cell.cellStats.age).sorted().collect(Collectors.toList());
            Integer eldest = null;
            if (ages.size() > 0)
            {
                eldest = ages.get(ages.size()-1);
            }
            List<Integer> virilities = a.getCellContainer().getAll().stream().map((cell) -> cell.cellStats.virility).sorted().collect(Collectors.toList());
            Integer most_virile = null;
            if (virilities.size() > 0)
            {
                most_virile = virilities.get(virilities.size()-1);
            }
            System.out.println(
                    "I: "+ a.getNumberOfInterations() +
                    " ORG: " + a.getCellContainer().getAll().size() +
                    " LIN: "+longest_lineage +
                    " AGE: " +eldest +
                    " VIR: " +most_virile);
        }, 10000);

        Universe universe = new Universe(new ASIA(),
                10,
                IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION,
                0,
                new CaptureTheFlag(),
                new SetCost(),
                0.001f,
                5,
                64,
                42069);
        List<Task> taskList = new ArrayList<>();
        taskList.add(update);
        UniverseRunner runner = new UniverseRunner(universe, taskList);

        runner.runForNIterations(1000000);
    }
}
