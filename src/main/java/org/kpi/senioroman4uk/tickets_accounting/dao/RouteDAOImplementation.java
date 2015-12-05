package org.kpi.senioroman4uk.tickets_accounting.dao;


import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 05.12.2015.
 *
 */
public class RouteDAOImplementation implements RouteDAO {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Override
    public boolean create(Route entity) {
        String sql = "INSERT INTO Route([number], [length]) " +
                    "VALUES(:number, :length)";
        HashMap<String, Object> parameters = getObjectFields(sql, entity);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(Route entity) {
        String sql = "UPDATE Route " +
                "SET [number] = :number, " +
                "[length] = :length";
        HashMap<String, Object> parameters = getObjectFields(sql, entity);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public Route find(int id) {
        String sql = "SELECT * " +
                "FROM Route " +
                "WHERE id = :id";
        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        List<Route> routes = jdbcTemplate.query(sql, parameters, new RouteRowMapper());
        return routes.isEmpty() ? null : routes.get(0);
    }

    @Override
    public List<Route> findAll() {
        String sql = "SELECT * " +
                "FROM Route";

        return jdbcTemplate.query(sql, new RouteRowMapper());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Route WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource == null)
            throw new BeanCreationException("Must set dataSource on ContactDao");
        if (jdbcTemplate == null)
            throw new BeanCreationException("Null NamedParameterJdbcTemplate on EmployeeDAOImplementation");
    }

    @Override
    public boolean hasControlLetters(int id) {
        String sql = "SELECT COUNT(id) " +
                "FROM ControlLetterRow " +
                "WHERE routeId = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class) > 0;
    }

    private static final class RouteRowMapper implements RowMapper<Route> {

        @Override
        public Route mapRow(ResultSet resultSet, int i) throws SQLException {
            Route route = new Route();

            route.setId(resultSet.getInt("id"));
            route.setLength(resultSet.getInt("length"));
            route.setNumber(resultSet.getInt("number"));

            return route;
        }
    }
}
