package org.kpi.senioroman4uk.tickets_accounting.validator;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;
import org.kpi.senioroman4uk.tickets_accounting.service.ControlLetterService;
import org.kpi.senioroman4uk.tickets_accounting.service.TicketsInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */

@Component
public class ControlLetterValidator implements Validator {
    @Autowired
    private ControlLetterService controlLetterService;
    @Autowired
    private TicketsInvoiceService ticketsInvoiceService;

    @Override
    public boolean supports(Class<?> aClass) {
        return ControlLetter.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ControlLetter controlLetter = (ControlLetter) o;
        if (controlLetter == null)
            return;

        if(controlLetter.getTicketPrice() <= 0d)
            errors.rejectValue("ticketPrice", "ControlLetter.ticketPrice.TooSmall");
        Employee employee = controlLetter.getCashier();
        if (employee != null && employee.getPosition().getId() != Employee.EmployeePosition.CASIER.getId()) {
            errors.rejectValue("cashier", "ControlLetter.cashier.Rejected");
        }

        if (controlLetter.getAmount() < controlLetterService.totalAmount(controlLetter.getId()))
            errors.rejectValue("amount", "ControlLetter.amount.tooSmall");

        int id = controlLetter.isNew() ? -1 : controlLetter.getId();
        int left = ticketsInvoiceService.amountLeft(controlLetter.getIncomeId(), id);

        if (controlLetter.getAmount() > left)
            errors.rejectValue("amount", "ControlLetter.amount.tooBig");

        if (controlLetter.getDate().after(new Date()))
            errors.rejectValue("date", "ControlLetter.date.tooBig");

        if (controlLetter.getIncomeId() != null) {
            TicketsInvoice ticketsInvoice = ticketsInvoiceService.find(controlLetter.getIncomeId());
            if (controlLetter.getDate().before(ticketsInvoice.getDate()))
                errors.rejectValue("date", "ControlLetter.date.beforeInvoice");
        }

        if(controlLetter.getDate().after(new Date()))
            errors.rejectValue("date", "TicketsInvoice.date.tooBig");
    }
}
