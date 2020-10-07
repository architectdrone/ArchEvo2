package org.architectdrone.archevo.isa;/*
 * Description
 * <p>
 * Copyrights 2020. Cerner Corporation.
 * @author Pharmacy Outpatient
 */

public class MalformedInstructionException extends Exception {
    public MalformedInstructionException(String operation_name, int correct_number_of_arguments, int given_number_of_arguments)
    {
        super(operation_name + " requires " + correct_number_of_arguments + ", " + given_number_of_arguments + " given.");
    }

    public MalformedInstructionException(String message)
    {
        super(message);
    }
}
