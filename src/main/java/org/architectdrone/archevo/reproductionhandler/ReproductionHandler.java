package org.architectdrone.archevo.reproductionhandler;

import org.architectdrone.archevo.cell.Cell;

public interface ReproductionHandler {
    public boolean canReproduce(Cell parent);

    public int reproductionEnergyCost(Cell parent);

    public int newCellEnergy(Cell parent);
}
