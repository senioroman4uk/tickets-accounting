package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */

public class EmployeeDAOImplementation extends GenericDAOImplementation<Employee> implements EmployeeDAO {
    @Override
    public boolean create(Employee entity) {
        String sql = "INSERT INTO Employee([type], [firstname], [lastname], [middlename], [birthDate])" +
                "VALUES(:type, :firstname, :lastname, :middlename, :birthDate)";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("type", entity.getPosition().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(Employee entity) {
        String sql = "UPDATE Employee " +
                "SET [type] = :type" +
                " ,[firstname] = :firstname" +
                " ,[lastname]  = :lastname" +
                " ,[middlename] = :middlename" +
                " ,[birthDate]  = :birthDate" +
                " WHERE id = :id";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("type", entity.getPosition().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public Employee find(int id) {
        //language=SQL
        String sql = "SELECT e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM Employee e " +
                "INNER JOIN EmployeeType et ON(et.id = e.type) " +
                "WHERE e.id = :id";

        return super.find(id, sql, new EmployeeRowMapper(), new HashMap<>());
    }

    @Override
    public Employee find(int id, int positionId) {
        String sql = "SELECT e.id empid, " +
                            "e.type, " +
                            "e.firstname, " +
                            "e.lastname, " +
                            "e.middlename, " +
                            "e.birthDate, " +
                            "et.id typeId, " +
                            "et.name " +
                    "FROM Employee e " +
                    "INNER JOIN EmployeeType et ON(et.id = e.type) " +
                    "WHERE e.id = :id AND " +
                    "e.type = :typeId";

        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("typeId", positionId);

        return jdbcTemplate.queryForObject(sql, parameters, new EmployeeRowMapper());
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM Employee e " +
                "INNER JOIN EmployeeType et ON(et.id = e.type)";

        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    @Override
    public List<Employee> findAll(int positionId) {
        String sql = "SELECT e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM Employee e " +
                "INNER JOIN EmployeeType et ON(et.id = e.type) " +
                "WHERE e.type = :typeId";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("typeId", positionId);

        return jdbcTemplate.query(sql, parameters, new EmployeeRowMapper());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Employee WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);
        return jdbcTemplate.update(sql, parameters) > 0;
    }

    public static final class EmployeeRowMapper implements RowMapper<Employee> {

        @Override
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee employee = new Employee();

            employee.setId(resultSet.getInt("empid"));
            employee.setBirthDate(resultSet.getDate("birthDate"));
            employee.setFirstname(resultSet.getString("firstname"));
            employee.setMiddlename(resultSet.getString("middlename"));
            employee.setLastname(resultSet.getString("lastname"));
            employee.setPosition(new PositionDAOImplementation.PositionRowMapper().mapRow(resultSet, i));

            return employee;
        }
    }
}