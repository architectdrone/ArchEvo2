package org.architectdrone.archevo.action;

/**
 * Represents an action that only changes a register.
 */
public class RegisterUpdate implements Action {
    private final int new_value;
    private final int register_to_change;

    public RegisterUpdate(int _new_value, int _register_to_change)
    {
        new_value = _new_value;
        register_to_change = _register_to_change;
    }

    public boolean has_external_effect() {
        return false;
    }

    public int getNewValue() {
        return new_value;
    }

    public int getRegisterToChange() {
        return register_to_change;
    }
}
