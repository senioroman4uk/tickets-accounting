package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.domain.ViewModel;

import java.util.List;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */
public interface GenericService<T extends ViewModel> {
    boolean create(T entity);
    boolean update(T entity);
    T find(int id);
    List<T> findAll();
    boolean delete(int id);
}
