package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.EmployeeDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */

@Service
public class EmployeeService implements UserService {
    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    @Transactional
    public boolean create(Employee entity) {
       return employeeDAO.create(entity);
    }

    @Override
    @Transactional
    public boolean update(Employee entity) {
        return employeeDAO.update(entity);
    }

    @Override
    public Employee find(int id) {
        return employeeDAO.find(id);
    }

    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee find(int id, int positionId) {
        return employeeDAO.find(id, positionId);
    }

    @Override
    public List<Employee> findAll(int positionId) {
        return employeeDAO.findAll(positionId);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
       return employeeDAO.delete(id);
    }
}
