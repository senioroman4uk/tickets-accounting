package org.kpi.senioroman4uk.tickets_accounting.validator;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetterRow;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.service.ControlLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Vladyslav on 08.12.2015.
 *
 */

@Component
public class ControlLetterRowValidator implements Validator {
    @Autowired
    ControlLetterService controlLetterService;

    @Override
    public boolean supports(Class<?> aClass) {
        return ControlLetterRow.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ControlLetterRow controlLetterRow = (ControlLetterRow) o;
        if (controlLetterRow == null)
            return;

        Employee employee = controlLetterRow.getConductor();
        if (employee != null && employee.getPosition().getId() != Employee.EmployeePosition.CONDUCTOR.getId()) {
            errors.rejectValue("conductor", "ControlLetterRow.conductor.Rejected");
        }

        if (controlLetterRow.getTicketsGiven() < controlLetterRow.getTicketsReturned())
            errors.rejectValue("ticketsReturned", "ControlLetterRow.ticketsReturned.tooBig");

        int left = controlLetterService.amountLeft(controlLetterRow.getControlLetterId(), controlLetterRow.getId());

        if (controlLetterRow.getTicketsGiven() > left) {
            errors.rejectValue("ticketsGiven", "ControlLetterRow.ticketsGiven.tooBig");
        }
    }
}
