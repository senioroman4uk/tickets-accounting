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

    default HashMap<String, Object> getObjectFields(String sql, T entity ) {
        Pattern pattern = Pattern.compile(":([a-z]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        HashMap<String, Object> parameters = new HashMap<>();

        while (matcher.find()) {
            String param = matcher.group().substring(1);
            if (parameters.containsKey(param))
                continue;

            Field field;
            try {
                field = entity.getClass().getDeclaredField(param);
            }
            catch (NoSuchFieldException ex) {continue;}
            field.setAccessible(true);

            try {
                parameters.put(param, field.get(entity));
            }
            catch (Exception ignored) {}
        }

        return parameters;
    }
}
