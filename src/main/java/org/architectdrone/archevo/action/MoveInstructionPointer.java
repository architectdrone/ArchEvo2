package org.architectdrone.archevo.action;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MoveInstructionPointer implements Action {
    private final int new_instruction_pointer_location;

    public MoveInstructionPointer(int new_instruction_pointer_location) {
        this.new_instruction_pointer_location = new_instruction_pointer_location;
    }

    @Override
    public boolean has_external_effect() {
        return false;
    }

    public int getNewInstructionPointerLocation() {
        return new_instruction_pointer_location;
    }
}
