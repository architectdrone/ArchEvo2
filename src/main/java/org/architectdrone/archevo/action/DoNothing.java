package org.architectdrone.archevo.action;

public class DoNothing implements Action {
    @Override
    public boolean has_external_effect() {
        return false;
    }
}
