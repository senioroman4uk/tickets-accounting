package org.kpi.senioroman4uk.tickets_accounting.validator;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;
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
public class TicketsInvoiceValidator implements Validator {
    @Autowired
    TicketsInvoiceService ticketsInvoiceService;

    @Override
    public boolean supports(Class<?> aClass) {
        return TicketsInvoice.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TicketsInvoice ticketsInvoice = (TicketsInvoice)o;

        if (ticketsInvoice == null) {
            return;
        }

        Employee employee = ticketsInvoice.getMainCashier();
        if (employee != null &&
                employee.getPosition().getId() != Employee.EmployeePosition.MAIN_CASIER.getId()) {
            errors.rejectValue("MainCashier", "TicketsInvoice.MainCashier.Rejected");
        }

        if (ticketsInvoice.getAmount() < ticketsInvoiceService.totalAmount(ticketsInvoice.getId())) {
            errors.rejectValue("amount", "TicketsInvoice.amount.tooSmall");
        }

        if (ticketsInvoice.getDate().after(new Date()))
            errors.rejectValue("date", "TicketsInvoice.date.tooBig");
    }
}
