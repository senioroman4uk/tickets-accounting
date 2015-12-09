package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Route;

/**
 * Created by Vladyslav on 05.12.2015.
 *
 */
public interface RouteDAO extends GenericDAO<Route> {
    boolean hasControlLetters(int id);
}
