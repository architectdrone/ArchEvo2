package org.architectdrone.archevo.apps;

import java.util.ArrayList;
import java.util.List;
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
            System.out.println("I: "+ a.getNumberOfInterations() + " LIVING ORGANISMS: " + a.getCellContainer().getAll().size());
        }, 10000);

        Universe universe = new Universe(new ASIA(),
                50,
                IterationExecutionMode.RUN_UNTIL_STATE_CHANGE_OR_N,
                0,
                new CaptureTheFlag(),
                new SetCost(),
                0.001f,
                4,
                64,
                42069);

        List<Task> taskList = new ArrayList<>();
        taskList.add(update);
        UniverseRunner runner = new UniverseRunner(universe, taskList);

        runner.runForNIterations(1000000);
    }
}
