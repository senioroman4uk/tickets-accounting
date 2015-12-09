package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;

import java.util.List;


/**
 * Created by Vladyslav on 22.11.2015.
 *
 */
public interface EmployeeDAO extends GenericDAO<Employee> {
    Employee find(int id, int positionId);
    List<Employee> findAll(int positionId);
}
