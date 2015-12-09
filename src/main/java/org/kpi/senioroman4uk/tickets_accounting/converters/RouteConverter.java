package org.kpi.senioroman4uk.tickets_accounting.converters;

import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.kpi.senioroman4uk.tickets_accounting.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Vladyslav on 08.12.2015.
 *
 */

@Component
public class RouteConverter implements Converter<String, Route> {
    @Autowired
    private RouteService routeService;

    @Override
    public Route convert(String s)  {
        if (s == null)
            return null;

        Integer id;
        try {
            id = Integer.parseInt(s);
        } catch (Exception ex) {
            return null;
        }

        return routeService.find(id);
    }
}
