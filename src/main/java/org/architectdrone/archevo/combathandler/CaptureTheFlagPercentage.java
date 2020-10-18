package org.architectdrone.archevo.combathandler;

import org.architectdrone.archevo.cell.Cell;

/**
 * The number of bits correct determines the percentage of energy taken from the defender.
 * 0 bits -> Attacker takes 1/256 of the other cell's energy. (aka, no change, since The maximum value of energy is 255)
 * 1 bit  -> Attacker takes 1/128 of the other cell's energy.
 * 2 bits -> Attacker takes 1/64  of the other cell's energy.
 * 3 bits -> Attacker takes 1/32  of the other cell's energy.
 * 4 bits -> Attacker takes 1/16  of the other cell's energy
 * 5 bits -> Attacker takes 1/8   of the other cell's energy
 * 6 bits -> Attacker takes 1/4   of the other cell's energy
 * 7 bits -> Attacker takes 1/2   of the other cell's energy
 * 8 bits -> Attacker takes all   of the other cell's energy
 * If defender is null, attacker loses 10 points of energy.
 */
public class CaptureTheFlagPercentage implements CombatHandler {
    public static int NULL_PENALTY = 10;
    @Override
    public CombatResult getResult(Cell attacker, Cell defender) {
        if (defender == null)
            return new CombatResult(0, -NULL_PENALTY);

        int score = 0;
        int guess = attacker.getRegister(0b010);
        int logo  = defender.getRegister(0b001);
        for (int i = 0; i < 8; i++)
        {
            int attacker_bit = (guess >> i) & 0b1;
            int defender_bit = (logo  >> i) & 0b1;
            if (attacker_bit == defender_bit) score++;
        }

        float percentage_of_energy;

        if      (score == 0) percentage_of_energy = 0.00390625f; // 1/256
        else if (score == 1) percentage_of_energy = 0.0078125f;  // 1/128
        else if (score == 2) percentage_of_energy = 0.015625f;   // 1/64
        else if (score == 3) percentage_of_energy = 0.03125f;    // 1/32
        else if (score == 4) percentage_of_energy = 0.0625f;     // 1/16
        else if (score == 5) percentage_of_energy = 0.125f;      // 1/8
        else if (score == 6) percentage_of_energy = 0.25f;       // 1/4
        else if (score == 7) percentage_of_energy = 0.5f;        // 1/2
        else                 percentage_of_energy = 1.0f;        // 1

        int energy_change = (int) Math.floor(percentage_of_energy*(float)defender.getRegister(0));

        int defender_change = 0-energy_change;
        int attacker_change =   energy_change;

        return new CombatResult(defender_change, attacker_change);
    }
}
