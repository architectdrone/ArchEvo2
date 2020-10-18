package org.architectdrone.archevo.runner;

import java.util.function.Consumer;
import org.architectdrone.archevo.universe.Universe;

public class Task {
    final Consumer<Universe> consumer;
    final int frequency;

    public Task(Consumer<Universe> consumer, int frequency)
    {
        this.consumer = consumer;
        this.frequency = frequency;
    }
}
