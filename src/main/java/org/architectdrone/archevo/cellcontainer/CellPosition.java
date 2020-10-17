package org.architectdrone.archevo.cellcontainer;

import lombok.EqualsAndHashCode;
import org.architectdrone.archevo.cell.Cell;

/**
 * Stores positional information about a cell
 */
@EqualsAndHashCode
public class CellPosition {
    public final int x;
    public final int y;
    public final Cell cell;

    public CellPosition(Cell cell, int x, int y) {
        this.cell = cell;
        this.x = x;
        this.y = y;
    }
}