package org.architectdrone.archevo.universe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.combathandler.CombatHandler;
import org.architectdrone.archevo.isa.ISA;
import org.architectdrone.archevo.reproductionhandler.ReproductionHandler;
import org.architectdrone.archevo.cellcontainer.CellContainer;
import org.architectdrone.archevo.cellcontainer.LinearContainer;
import org.architectdrone.archevo.universe.iterationhelper.cellcontainer.CellContainerIterationHelper;

public class Universe {

    @Getter
    private CellContainer cellContainer;
    public final ISA isa;
    public final IterationExecutionMode iterationExecutionMode;
    public final int move_cost;
    public final ReproductionHandler reproductionHandler;
    public final CombatHandler combatHandler;
    public final int initial_energy;
    public final int seed;
    private final Random randomness;
    public final float mutation_chance;
    public final int influx_rate;
    public final int iteration_cost;
    public final int number_of_genes;
    @Getter
    private int numberOfInterations = 0;
    public Universe(ISA isa,
                    int universe_size,
                    IterationExecutionMode iterationExecutionMode,
                    int move_cost,
                    int iteration_cost, CombatHandler combatHandler,
                    ReproductionHandler reproductionHandler,
                    final float mutation_chance,
                    final int influx_rate,
                    final int initial_energy,
                    int number_of_genes, final int seed)
    {
        this.isa = isa;
        this.cellContainer = new LinearContainer(universe_size);
        this.iterationExecutionMode = iterationExecutionMode;
        this.move_cost = move_cost;
        this.iteration_cost = iteration_cost;
        this.combatHandler = combatHandler;
        this.reproductionHandler = reproductionHandler;
        this.initial_energy = initial_energy;
        this.number_of_genes = number_of_genes;
        this.seed = seed;
        this.randomness = new Random(seed);
        this.mutation_chance = mutation_chance;
        this.influx_rate = influx_rate;
    }

    public void iterate() throws Exception {
        numberOfInterations++;
        for (int i = 0; i < influx_rate; i++)
        {
            int random_x = randomness.nextInt(cellContainer.getSize());
            int random_y = randomness.nextInt(cellContainer.getSize());
            try {
                addRandomCell(random_x, random_y);
            }
            catch (Exception e) {

            }

        }
        cellContainer = CellContainerIterationHelper.iterate(cellContainer, isa, iterationExecutionMode, move_cost, iteration_cost, combatHandler, reproductionHandler, mutation_chance, randomness);
    }

    private void addRandomCell(int x, int y) throws Exception {
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < number_of_genes; i++) {
            genome.add(randomness.nextInt((int) Math.pow(2, isa.getNumberOfBitsPerInstruction())) );
        }
        Cell cell = new Cell(genome, isa);
        cell.setRegister(0, initial_energy);
        cellContainer.set(x, y, cell);
    }
}