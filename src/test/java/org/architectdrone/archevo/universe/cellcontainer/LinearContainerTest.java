package org.architectdrone.archevo.universe.cellcontainer;

import java.util.ArrayList;
import java.util.List;
import org.architectdrone.archevo.cell.Cell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


 class LinearContainerTest {
    @Test
    void settingACellAtACoordinate_allowsTheCellToBeReturnedWithGetAtSameCoordinate() throws Exception {
        LinearContainer linearContainer = new LinearContainer(15);
        Cell cell = new Cell(new ArrayList<>());
        linearContainer.set(1, 1, cell);
        assertEquals(cell, linearContainer.get(1, 1));
    }

    @Test
    void settingACellOutOutBounds_causesItToWrapToTheOtherSide() throws Exception {
        LinearContainer linearContainer = new LinearContainer(15);
        Cell cell = new Cell(new ArrayList<>());
        linearContainer.set(-1, -1, cell);
        assertEquals(cell, linearContainer.get(14, 14));
        assertEquals(cell, linearContainer.get(-1, -1));
    }

     @Test
     void settingACellAtACoordinate_thenSettingAnotherCellAtTheSameCoordinate_throwsException() throws Exception {
         LinearContainer linearContainer = new LinearContainer(15);
         Cell cell1 = new Cell(new ArrayList<>());
         Cell cell2 = new Cell(new ArrayList<>());
         linearContainer.set(1, 1, cell1);
         assertThrows(Exception.class, () -> linearContainer.set(1, 1, cell2));
     }

     @Test
     void gettingACellWhereThereIsNoCell_returnsNull() {
         LinearContainer linearContainer = new LinearContainer(15);
         assertNull(linearContainer.get(5, 5));
     }

     @Test
     void deletingACellWorks() throws Exception {
         LinearContainer linearContainer = new LinearContainer(15);
         Cell cell = new Cell(new ArrayList<>());
         linearContainer.set(1, 1, cell);
         assertEquals(cell, linearContainer.get(1, 1));
         linearContainer.delete(1, 1);
         assertNull(linearContainer.get(1, 1));
     }

     @Test
     void loadingAList_works() throws Exception {
        List<CellPosition> cellPositionList = new ArrayList<>();
        Cell cell1 = new Cell(new ArrayList<>());
        Cell cell2 = new Cell(new ArrayList<>());
        Cell cell3 = new Cell(new ArrayList<>());
        cellPositionList.add(new CellPosition(cell1, 1, 1));
        cellPositionList.add(new CellPosition(cell2, 2, 2));
        cellPositionList.add(new CellPosition(cell3, 3, 3));
        LinearContainer linearContainer = new LinearContainer(15);
        linearContainer.load(cellPositionList);
        assertEquals(cell1, linearContainer.get(1, 1));
        assertEquals(cell2, linearContainer.get(2, 2));
        assertEquals(cell3, linearContainer.get(3, 3));
     }

     @Test
     void loadingAListWhenCellsAlreadyExist_throwsAnError() throws Exception {
         List<CellPosition> cellPositionList = new ArrayList<>();
         Cell cell = new Cell(new ArrayList<>());
         LinearContainer linearContainer = new LinearContainer(15);
         linearContainer.set(1, 1, cell);
         assertThrows(Exception.class, () ->  linearContainer.load(cellPositionList) );
     }

     @Test
     void gettingAllCells_works() throws Exception {
         LinearContainer linearContainer = new LinearContainer(15);
         Cell cell1 = new Cell(new ArrayList<>());
         Cell cell2 = new Cell(new ArrayList<>());
         Cell cell3 = new Cell(new ArrayList<>());
         linearContainer.set(1, 1, cell1);
         linearContainer.set(2, 2, cell2);
         linearContainer.set(3, 3, cell3);
        List<Cell> cells = linearContainer.getAll();

         assertTrue(cells.contains(cell1));
         assertTrue(cells.contains(cell2));
         assertTrue(cells.contains(cell3));
         assertEquals(3, cells.size());
     }

     @Test
     void gettingAllCellPositions_works() throws Exception {
         LinearContainer linearContainer = new LinearContainer(15);
         Cell cell1 = new Cell(new ArrayList<>());
         Cell cell2 = new Cell(new ArrayList<>());
         Cell cell3 = new Cell(new ArrayList<>());
         linearContainer.set(1, 1, cell1);
         linearContainer.set(2, 2, cell2);
         linearContainer.set(3, 3, cell3);
         List<CellPosition> cellPositions = linearContainer.getAllPositions();

         assertTrue(cellPositions.contains(new CellPosition(cell1, 1, 1)));
         assertTrue(cellPositions.contains(new CellPosition(cell2, 2, 2)));
         assertTrue(cellPositions.contains(new CellPosition(cell3, 3, 3)));
         assertEquals(3, cellPositions.size());
     }
}