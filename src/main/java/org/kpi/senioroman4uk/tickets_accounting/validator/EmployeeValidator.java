package org.kpi.senioroman4uk.tickets_accounting.validator;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

/**
 * Created by Vladyslav on 30.11.2015.
 *
 */

@Component
public class EmployeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Employee employee = (Employee) o;
        if (employee == null) {
            throw new IllegalArgumentException("object is null where it must not be");
        }
        Date currentDate = new Date();
        if (employee.getBirthDate() != null) {
            if (employee.getBirthDate().after(currentDate))
                errors.rejectValue("birthDate", "employeeForm.birthDate.after");

        }
        else
            errors.rejectValue("birthDate", "employeeForm.birthDate.required");
    }
}
