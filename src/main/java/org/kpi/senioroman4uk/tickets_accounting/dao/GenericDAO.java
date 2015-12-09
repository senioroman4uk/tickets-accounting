package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vladyslav on 22.11.2015.
 *
 */
public interface GenericDAO<T> extends InitializingBean {
    void setDataSource(DataSource dataSource);

    boolean create(T entity);

    boolean update(T entity);

    T find(int id);

    List<T> findAll();

    boolean delete(int id);

    HashMap<String, Object> getObjectFields(String sql, T entity);
}
