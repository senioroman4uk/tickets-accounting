package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.PositionDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.exception.PositionHasEmployeesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vladyslav on 05.12.2015.
 *
 */

@Service
public class PositionServiceImplementation implements PositionService {
    @Autowired
    private PositionDAO positionDAO;

    @Override
    public boolean create(Position entity) {
        return positionDAO.create(entity);
    }

    @Override
    public boolean update(Position entity) {
        return positionDAO.update(entity);
    }

    @Override
    public Position find(int id) {
        return positionDAO.find(id);
    }

    @Override
    public List<Position> findAll() {
        return positionDAO.findAll();
    }

    @Override
    public boolean delete(int id) throws PositionHasEmployeesException{
        if(positionDAO.employeesExist(id))
            throw new PositionHasEmployeesException();

        return positionDAO.delete(id);
    }
}
