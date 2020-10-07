package org.architectdrone.archevo.misc;

import org.architectdrone.archevo.cell.Cell;

@FunctionalInterface
public interface OffsetToCell {
    public Cell f(int x, int y);
}
