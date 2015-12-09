package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */

public abstract class GenericDAOImplementation<T> implements GenericDAO<T> {

    protected NamedParameterJdbcTemplate jdbcTemplate;
    protected DataSource dataSource;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource == null)
            throw new BeanCreationException("Must set dataSource on " + getClass().getName());
        if (jdbcTemplate == null)
            throw new BeanCreationException("Null NamedParameterJdbcTemplate on " + getClass().getName());
    }

    protected T find(int id, String sql, RowMapper<T> rowMapper, HashMap<String, Object> parameters) {
        parameters.put("id", id);
        try {
            return jdbcTemplate.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return  null;
        }

    }

    @Override
    public HashMap<String, Object> getObjectFields(String sql, T entity ) {
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
