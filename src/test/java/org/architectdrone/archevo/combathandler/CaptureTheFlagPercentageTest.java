package org.architectdrone.archevo.combathandler;

import org.architectdrone.archevo.cell.Cell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CaptureTheFlagPercentageTest {
    @Test
    void getResult_0bits_noChange() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111111;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 255);

        int expected_change = 0;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_1bits_lose128th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111110;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 1;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_1bits_roundDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111110;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 0;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_2bits_lose64th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111100;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 2;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_2bits_roundDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111100;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 1;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_3bits_lose32th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 4;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_3bits_roundsDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11111000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 3;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_4bits_lose16th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11110000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 8;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_4bits_roundsDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11110000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 7;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_5bits_lose8th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11100000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 16;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_5bits_roundsDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11100000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 15;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_6bits_lose4th() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11000000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 32;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_6bits_roundsDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b11000000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 31;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_7bits_loseHalf() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b10000000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 64;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_7bits_roundsDown() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b10000000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 127);

        int expected_change = 63;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }

    @Test
    void getResult_8bits_loseAll() {
        Cell attacker = new Cell(new ArrayList<>(), null);
        Cell defender = new Cell(new ArrayList<>(), null);

        int logo =  0b00000000;
        int guess = 0b00000000;

        attacker.setRegister(0b010, guess);
        defender.setRegister(0b001, logo);

        defender.setRegister(0b000, 129);

        int expected_change = 129;
        CombatResult combatResult = new CaptureTheFlagPercentage().getResult(attacker, defender);
        assertEquals(expected_change, combatResult.getAttackerEnergyChange());
        assertEquals(0-expected_change, combatResult.getDefenderEnergyChange());
    }
}