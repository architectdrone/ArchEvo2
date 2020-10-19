package org.architectdrone.archevo.reproductionhandler;/*
 * Description
 * <p>
 * Copyrights 2020. Cerner Corporation.
 * @author Pharmacy Outpatient
 */

import org.architectdrone.archevo.cell.Cell;

public class SetCost implements ReproductionHandler {
    final int reproduction_cost;

    public SetCost(int reproduction_cost)
    {
        this.reproduction_cost = reproduction_cost;
    }

    @Override
    public boolean canReproduce(final Cell parent) {
        return parent.getRegister(0b000) > (reproduction_cost);
    }

    @Override
    public int reproductionEnergyCost(final Cell parent) {
        return reproduction_cost;
    }

    @Override
    public int newCellEnergy(final Cell parent) {
        return reproduction_cost;
    }
}
