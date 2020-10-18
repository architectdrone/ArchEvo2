package org.architectdrone.archevo.cellcontainer;

import java.util.List;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.cellcontainer.exceptions.AlreadyLoadedException;
import org.architectdrone.archevo.cellcontainer.exceptions.IntersectionException;

public interface CellContainer {
    int getSize();
    void set(int x, int y, Cell cell) throws IntersectionException;
    Cell get(int x, int y);
    void delete(int x, int y);
    List<Cell> getAll();
    List<CellPosition> getAllPositions();
    void load(List<CellPosition> cells) throws AlreadyLoadedException;
}
