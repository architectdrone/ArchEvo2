package org.architectdrone.archevo.reproductionhandler;/*
 * Description
 * <p>
 * Copyrights 2020. Cerner Corporation.
 * @author Pharmacy Outpatient
 */

import org.architectdrone.archevo.cell.Cell;

public class SetCost implements ReproductionHandler {
    static final int REPRODUCTION_COST = 64;

    @Override
    public boolean canReproduce(final Cell parent) {
        return parent.getRegister(0b000) > REPRODUCTION_COST;
    }

    @Override
    public int reproductionEnergyCost(final Cell parent) {
        return REPRODUCTION_COST;
    }

    @Override
    public int newCellEnergy(final Cell parent) {
        return REPRODUCTION_COST;
    }
}
