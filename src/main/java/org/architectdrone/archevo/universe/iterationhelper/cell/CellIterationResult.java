package org.architectdrone.archevo.universe.iterationhelper.cell;

import org.architectdrone.archevo.action.Action;
import org.architectdrone.archevo.cell.Cell;

public class CellIterationResult {
    public final Cell new_state;
    public final Action external_state_changing_action;

    public CellIterationResult(Cell new_state, Action external_state_changing_action)
    {
        assert external_state_changing_action == null || external_state_changing_action.has_external_effect() : "Provided action must have external effect.";
        this.new_state = new_state;
        this.external_state_changing_action = external_state_changing_action;
    }
}
