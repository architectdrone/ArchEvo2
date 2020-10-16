package org.architectdrone.archevo.combathandler;

import lombok.Data;

@Data
public class CombatResult {
    private final int defenderEnergyChange;
    private final int attackerEnergyChange;

    public CombatResult(int defenderEnergyChange, int attackerEnergyChange)
    {
        this.attackerEnergyChange = attackerEnergyChange;
        this.defenderEnergyChange = defenderEnergyChange;
    }
}
