package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.RouteDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.kpi.senioroman4uk.tickets_accounting.exception.HasControlLettersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vladyslav on 05.12.2015.
 *
 */


@Service
public class RouteServiceImplementation implements RouteService {
    @Autowired
    private RouteDAO routeDAO;

    @Override
    public boolean create(Route entity) {
        return routeDAO.create(entity);
    }

    @Override
    public boolean update(Route entity) {
        return routeDAO.update(entity);
    }

    @Override
    public Route find(int id) {
        return routeDAO.find(id);
    }

    @Override
    public List<Route> findAll() {
        return routeDAO.findAll();
    }

    @Override
    public boolean delete(int id) throws HasControlLettersException {
        if (routeDAO.hasControlLetters(id))
            throw new HasControlLettersException();

        return routeDAO.delete(id);
    }
}
