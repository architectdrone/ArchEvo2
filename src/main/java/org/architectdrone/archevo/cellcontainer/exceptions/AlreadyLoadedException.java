package org.architectdrone.archevo.cellcontainer.exceptions;

public class AlreadyLoadedException extends Exception{
    public AlreadyLoadedException() {
        super("Attempted to put a cell on top of another cell.");
    }
}
