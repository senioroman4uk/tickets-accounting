package org.kpi.senioroman4uk.tickets_accounting.converters;


import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.service.EmployeeService;
import org.kpi.senioroman4uk.tickets_accounting.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Vladyslav on 30.11.2015.
 *
 */

@Component
public class PositionConverter implements Converter<String, Position> {
    @Autowired
    private PositionService positionService;

    @Override
    public Position convert(String s)  {
        if (s == null)
            return null;

        Integer id;
        try {
            id = Integer.parseInt(s);
        } catch (Exception ex) {
            return null;
        }

        return positionService.find(id);
    }
}