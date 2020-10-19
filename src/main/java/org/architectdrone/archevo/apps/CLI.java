package org.architectdrone.archevo.apps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.combathandler.CaptureTheFlag;
import org.architectdrone.archevo.combathandler.CaptureTheFlagPercentage;
import org.architectdrone.archevo.combathandler.CombatHandler;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.architectdrone.archevo.reproductionhandler.ReproductionHandler;
import org.architectdrone.archevo.reproductionhandler.SetCost;
import org.architectdrone.archevo.runner.Task;
import org.architectdrone.archevo.runner.UniverseRunner;
import org.architectdrone.archevo.universe.IterationExecutionMode;
import org.architectdrone.archevo.universe.Universe;

public class CLI {
    public static int INFLUX_RATE = 1;
    public static float MUTATION_RATE = 0.001f;
    public static int WORLD_SIZE = 20;
    public static int RANDOM_CELL_INITIAL_ENERGY = 64;
    public static int MOVE_COST = 0;
    public static int ITERATION_COST = 0;

    public static IterationExecutionMode ITERATION_EXECUTION_MODE = IterationExecutionMode.INSTRUCTION_BY_INSTRUCTION;
    public static CombatHandler COMBAT_HANDLER = new CaptureTheFlagPercentage();
    public static ReproductionHandler REPRODUCTION_HANDLER = new SetCost(RANDOM_CELL_INITIAL_ENERGY);
    public static ISA ISA_TO_USE = new ASIA();

    public static void main(String[] args) throws Exception {
        Task update = new Task((a) -> {
            Cell longest_lineage_cell = getLongestLineageCell(a.getCellContainer().getAll());
            Cell oldest_cell = getOldestCell(a.getCellContainer().getAll());
            Cell most_viral_cell = getMostViralCell(a.getCellContainer().getAll());

            if (oldest_cell != null && longest_lineage_cell != null && most_viral_cell != null)
            {
                System.out.println(
                        "I: "+ a.getNumberOfInterations() +
                                " ORG: " + a.getCellContainer().getAll().size() +
                                " LIN: "+ longest_lineage_cell.cellStats.lineage +
                                " AGE: " +oldest_cell.cellStats.age +
                                " VIR: " +most_viral_cell.cellStats.virility);

                if (longest_lineage_cell.cellStats.lineage >= 2)
                {
                    System.out.println("The cell with the longest lineage has the following genome:");
                    printCell(most_viral_cell);
                }
            }

        }, 10000);

        Universe universe = new Universe(
                ISA_TO_USE,
                WORLD_SIZE,
                ITERATION_EXECUTION_MODE,
                MOVE_COST,
                ITERATION_COST,
                COMBAT_HANDLER,
                REPRODUCTION_HANDLER,
                MUTATION_RATE,
                INFLUX_RATE,
                RANDOM_CELL_INITIAL_ENERGY,
                42069);
        List<Task> taskList = new ArrayList<>();
        taskList.add(update);
        UniverseRunner runner = new UniverseRunner(universe, taskList);

        runner.runForNIterations(100000000);
    }

    public static Cell getMostViralCell(List<Cell> cells)
    {
        List<Integer> virilities = cells.stream().map((cell) -> cell.cellStats.virility).sorted().collect(Collectors.toList());
        final Integer most_virile;
        if (virilities.size() > 0)
        {
            most_virile = virilities.get(virilities.size()-1);
        }
        else
        {
            most_virile = null;
        }

        if (most_virile == null)
        {
            return null;
        }
        else
        {
            return cells.stream().filter((a) -> a.cellStats.virility == most_virile).collect(Collectors.toList()).get(0);
        }
    }

    public static Cell getOldestCell(List<Cell> cells)
    {
        List<Integer> ages = cells.stream().map((cell) -> cell.cellStats.age).sorted().collect(Collectors.toList());
        final Integer highest_age;
        if (ages.size() > 0)
        {
            highest_age = ages.get(ages.size()-1);
        }
        else
        {
            highest_age = null;
        }

        if (highest_age == null)
        {
            return null;
        }
        else
        {
            return cells.stream().filter((a) -> a.cellStats.age == highest_age).collect(Collectors.toList()).get(0);
        }
    }

    public static Cell getLongestLineageCell(List<Cell> cells)
    {
        List<Integer> lineages = cells.stream().map((cell) -> cell.cellStats.lineage).sorted().collect(Collectors.toList());
        final Integer longest_lineage;
        if (lineages.size() > 0)
        {
            longest_lineage = lineages.get(lineages.size()-1);
        }
        else
        {
            longest_lineage = null;
        }

        if (longest_lineage == null)
        {
            return null;
        }
        else
        {
            return cells.stream().filter((a) -> a.cellStats.lineage == longest_lineage).collect(Collectors.toList()).get(0);
        }
    }

    public static void printCell(Cell cell) {
        for (Integer gene : cell.getGenome())
        {
            System.out.println(new ASIA().binaryToString(gene));
        }
    }
}
