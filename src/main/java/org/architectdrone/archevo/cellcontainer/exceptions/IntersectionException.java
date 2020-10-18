package org.architectdrone.archevo.cellcontainer.exceptions;

/**
 * Thrown when attempting to place a cell on top of another cell in a CellContainer.
 */
public class IntersectionException extends Exception {
    public IntersectionException() {
        super("Attempted to put a cell on top of another cell.");
    }
}
