package org.architectdrone.archevo.action;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for all actions.
 */
public class ActionTest {
    @Nested
    public class RegisterUpdateTest {
        @Test
        void hasExternalEffect_returnsFalse() {
            RegisterUpdate testRegisterUpdate = new RegisterUpdate(5, 4);
            assertFalse(testRegisterUpdate.has_external_effect());
        }

        @Test
        void getNewValue_returnsNewValue() {
            int new_value = 5;
            RegisterUpdate testRegisterUpdate = new RegisterUpdate(new_value, 4);
            assertEquals(testRegisterUpdate.getNewValue(), new_value);
        }

        @Test
        void getRegisterToChange_returnsRegisterToChange() {
            int register_to_change = 4;
            RegisterUpdate testRegisterUpdate = new RegisterUpdate(5, register_to_change);
            assertEquals(testRegisterUpdate.getRegisterToChange(), register_to_change);
        }
    }

    @Nested
    public class MoveTest {
        @Test
        void hasExternalEffect_returnsTrue() {
            Move testMove = new Move(5, 4);
            assertTrue(testMove.has_external_effect());
        }

        @Test
        void getXOffset_returnsXOffset() {
            int x_offset = 5;
            Move testMove = new Move(x_offset, 4);
            assertEquals(testMove.getXOffset(), x_offset);
        }

        @Test
        void getYOffset_returnsYOffset() {
            int y_offset = 5;
            Move testMove = new Move(5, y_offset);
            assertEquals(testMove.getYOffset(), y_offset);
        }
    }

    @Nested
    public class AttackTest {
        @Test
        void hasExternalEffect_returnsTrue() {
            Attack testAttack = new Attack(5, 4);
            assertTrue(testAttack.has_external_effect());
        }

        @Test
        void getXOffset_returnsXOffset() {
            int x_offset = 5;
            Attack testAttack = new Attack(x_offset, 4);
            assertEquals(testAttack.getXOffset(), x_offset);
        }

        @Test
        void getYOffset_returnsYOffset() {
            int y_offset = 5;
            Attack testAttack = new Attack(5, y_offset);
            assertEquals(testAttack.getYOffset(), y_offset);
        }
    }

    @Nested
    public class MoveInstructionPointerTest {
        @Test
        void hasExternalEffect_returnsFalse() {
            MoveInstructionPointer testMoveInstructionPointer = new MoveInstructionPointer(13);
            assertFalse(testMoveInstructionPointer.has_external_effect());
        }

        @Test
        void getNewInstructionPointerLocation_returnsNewInstructionPointerLocation() {
            MoveInstructionPointer testMoveInstructionPointer = new MoveInstructionPointer(13);
            assertEquals(testMoveInstructionPointer.getNewInstructionPointerLocation(), 13);
        }
    }
}
