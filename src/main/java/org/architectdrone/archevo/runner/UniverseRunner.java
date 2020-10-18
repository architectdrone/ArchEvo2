package org.architectdrone.archevo.runner;

import java.util.List;
import org.architectdrone.archevo.universe.Universe;

public class UniverseRunner {
    List<Task> tasks;
    Universe universe;

    public UniverseRunner(Universe universe, List<Task> tasks) {
        this.tasks = tasks;
        this.universe = universe;
    }

    public void runForNIterations(int n) throws Exception {
        for (int i = 0; i < n; i++)
        {
            runIteration();
        }
    }

    public void runIteration() throws Exception {
        tasks.forEach((a) -> {
            if (a.frequency % universe.getNumberOfInterations() == 0)
            {
                a.consumer.accept(universe);
            }
        });
        universe.iterate();
    }
}
