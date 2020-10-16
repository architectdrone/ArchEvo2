package org.architectdrone.archevo.combathandler;
import org.architectdrone.archevo.cell.Cell;

public class CaptureTheFlag implements CombatHandler {
    static final int ATTACK_MIN_SCORE = 4;

    @Override
    public CombatResult getResult(final Cell attacker, final Cell defender) {
        if (defender == null)
        {
            return new CombatResult(0, -16);
        }
        int score = 0;
        int guess = attacker.getRegister(0b010);
        int logo  = defender.getRegister(0b001);
        for (int i = 0; i < 8; i++)
        {
            int attacker_bit = (guess >> i) & 0b1;
            int defender_bit = (logo  >> i) & 0b1;
            if (attacker_bit == defender_bit) score++;
        }

        if (score > ATTACK_MIN_SCORE)
        {
            int damage = (0b1 << (score-ATTACK_MIN_SCORE));
            return new CombatResult(0-damage, damage);
        }
        else
        {
            int damage = (0b1 << (ATTACK_MIN_SCORE-score));
            return new CombatResult(0, 0-damage);
        }
    }
}
