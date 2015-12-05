package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Position;

/**
 * Created by Vladyslav on 03.12.2015.
 *
 */
public interface PositionDAO extends GenericDAO<Position> {
    boolean employeesExist(int positionId);
}
