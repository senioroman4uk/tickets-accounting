package org.kpi.senioroman4uk.tickets_accounting.converters;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * Created by Vladyslav on 06.12.2015.
 */

//TODO: USE GenericConverter
@Component
public class StringToEmployeeConverter implements Converter<String, Employee> {
    @Autowired
    private EmployeeService service;

    @Override
    public Employee convert(String s) {
        if (s == null)
            return null;

        Integer id;
        try {
            id = Integer.parseInt(s);
        } catch (Exception ex) {
            return null;
        }

        return service.find(id);
    }
}
