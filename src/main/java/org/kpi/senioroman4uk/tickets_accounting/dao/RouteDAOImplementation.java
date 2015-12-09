package org.kpi.senioroman4uk.tickets_accounting.dao;


import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 05.12.2015.
 *
 */

public class RouteDAOImplementation extends GenericDAOImplementation<Route> implements RouteDAO {
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
        //language=SQL
        String sql = "SELECT id routeId, number, [length] " +
                "FROM Route " +
                "WHERE id = :id";

        return super.find(id, sql, new RouteRowMapper(), new HashMap<>());
    }

    @Override
    public List<Route> findAll() {
        //language=SQL
        String sql = "SELECT id routeId, number, [length]" +
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
    public boolean hasControlLetters(int id) {
        String sql = "SELECT COUNT(id) " +
                "FROM ControlLetterRow " +
                "WHERE routeId = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class) > 0;
    }

    public static final class RouteRowMapper implements RowMapper<Route> {
        @Override
        public Route mapRow(ResultSet resultSet, int i) throws SQLException {
            Route route = new Route();

            route.setId(resultSet.getInt("routeId"));
            route.setLength(resultSet.getInt("length"));
            route.setNumber(resultSet.getInt("number"));

            return route;
        }
    }
}
