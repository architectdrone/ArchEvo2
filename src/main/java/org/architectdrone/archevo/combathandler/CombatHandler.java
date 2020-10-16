package org.architectdrone.archevo.combathandler;

import org.architectdrone.archevo.cell.Cell;

public interface CombatHandler {
    public CombatResult getResult(Cell attacker, Cell defender);
}
