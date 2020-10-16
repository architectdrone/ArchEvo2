package org.architectdrone.archevo.universe;

/**
 * Specifies the manner in which instructions are executed in an iteration.
 */
public enum IterationExecutionMode {
    INSTRUCTION_BY_INSTRUCTION, //Cells run exactly one instruction at a time.
    RUN_UNTIL_STATE_CHANGE_OR_N, //Cells run until they reach one of the following actions: Reproduce, Attack, or Move. If no action occurs after a number of executions equal to the size of the genome, it also halts.
    RUN_UNTIL_STATE_CHANGE_OR_N_SAVE_IP, //Same as above, except that the state of the instruction pointer is saved after halting.
}
