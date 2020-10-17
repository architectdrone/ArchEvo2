package org.architectdrone.archevo.combathandler;

import java.util.ArrayList;
import org.architectdrone.archevo.cell.Cell;
import org.architectdrone.archevo.isa.asia.ASIA;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaptureTheFlagTest {
    @Test
    void onPerfectAttack_defenderGives16Energy() {
        CaptureTheFlag captureTheFlag = new CaptureTheFlag();

        int logo =  0b10101010;
        int guess = 0b10101010;

        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo );

        assertEquals(16, captureTheFlag.getResult(attacker, defender).getAttackerEnergyChange());
        assertEquals(-16, captureTheFlag.getResult(attacker, defender).getDefenderEnergyChange());
    }

    @Test
    void oneBitOff_defenderGives8Energy() {
        CaptureTheFlag captureTheFlag = new CaptureTheFlag();

        int logo =  0b10101010;
        int guess = 0b00101010;

        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo );

        assertEquals(8, captureTheFlag.getResult(attacker, defender).getAttackerEnergyChange());
        assertEquals(-8, captureTheFlag.getResult(attacker, defender).getDefenderEnergyChange());
    }

    @Test
    void sevenBitsOff_AttackerLoses8Energy() {
        CaptureTheFlag captureTheFlag = new CaptureTheFlag();

        int logo =  0b11111111;
        int guess = 0b10000000;

        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo );

        assertEquals(-8, captureTheFlag.getResult(attacker, defender).getAttackerEnergyChange());
        assertEquals(0, captureTheFlag.getResult(attacker, defender).getDefenderEnergyChange());
    }

    @Test
    void eightBitsOff_AttackerLoses16Energy() {
        CaptureTheFlag captureTheFlag = new CaptureTheFlag();

        int logo =  0b11111111;
        int guess = 0b00000000;

        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo );

        assertEquals(-16, captureTheFlag.getResult(attacker, defender).getAttackerEnergyChange());
        assertEquals(0, captureTheFlag.getResult(attacker, defender).getDefenderEnergyChange());
    }
}