package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Position;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 03.12.2015.
 *
 */

public class PositionDAOImplementation extends GenericDAOImplementation<Position> implements PositionDAO {
    @Override
    public boolean create(Position entity) {
        String sql = "INSERT INTO EmployeeType([name]) VALUES(:name)";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", entity.getName());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(Position entity) {
        String sql = "UPDATE EmployeeType " +
                    "SET [name] = :name " +
                    "WHERE id = :id";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", entity.getName());
        parameters.put("id", entity.getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public Position find(int id) {
        String sql = "SELECT et.id typeId, et.name " +
                "FROM EmployeeType et " +
                "WHERE id = :id";

        return super.find(id, sql, new PositionRowMapper(), new HashMap<>());
    }

    @Override
    public List<Position> findAll() {
        String sql = "SELECT et.id typeId, et.name " +
                "FROM EmployeeType et";

        return jdbcTemplate.query(sql, new PositionRowMapper());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM EmployeeType WHERE [id] = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    /**
     * Checking if position has employees
     */
    public boolean employeesExist(int positionId) {
        String sql = "SELECT COUNT(id) FROM Employee WHERE type = :type";
        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("type", positionId);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class) > 0;
    }

    public static final class PositionRowMapper implements RowMapper<Position> {

        @Override
        public Position mapRow(ResultSet resultSet, int i) throws SQLException {
            Position position = new Position();

            position.setId(resultSet.getInt("typeId"));
            position.setName(resultSet.getString("name"));

            return position;
        }
    }
}
