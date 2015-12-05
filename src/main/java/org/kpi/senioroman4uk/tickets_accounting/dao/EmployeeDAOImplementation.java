package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */
public class EmployeeDAOImplementation implements EmployeeDAO {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

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
        String sql = "SELECT e.id, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM Employee e " +
                "INNER JOIN EmployeeType et ON(et.id = e.type) " +
                "WHERE e.id = :id";
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);
        
        return jdbcTemplate.query(sql, parameters, new SingleEmployeeExtractor());
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT e.id, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM Employee e " +
                "INNER JOIN EmployeeType et ON(et.id = e.type)";

        return jdbcTemplate.query(sql, new EmployeeExtractor());
    }

    @Override
    public boolean delete(int id) {
        String sql  = "DELETE FROM Employee WHERE id = :id";

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

    private static final class EmployeeExtractor implements ResultSetExtractor<List<Employee>> {
        public List<Employee> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Integer, Position> map = new HashMap<>();
            ArrayList<Employee> employees = new ArrayList<>();


            Position position;

            while (resultSet.next()) {
                Employee employee = new Employee();

                employee.setId(resultSet.getInt("id"));
                employee.setBirthDate(resultSet.getDate("birthDate"));
                employee.setFirstname(resultSet.getString("firstname"));
                employee.setMiddlename(resultSet.getString("middlename"));
                employee.setLastname(resultSet.getString("lastname"));

                int positionId = resultSet.getInt("typeId");
                position = map.get(positionId);

                if (position == null) {
                    position = new Position();
                    position.setId(positionId);
                    position.setName(resultSet.getString("name"));
                    map.put(positionId, position);
                } else
                    position = new Position(position);

                employee.setPosition(position);
                employees.add(employee);
            }

            return employees;
        }
    }

    private static final class SingleEmployeeExtractor implements ResultSetExtractor<Employee> {
        public Employee extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            ResultSetExtractor<List<Employee>> extractor = new EmployeeExtractor();
            List<Employee> employees = extractor.extractData(resultSet);

            return employees.isEmpty() ? null : employees.get(0);
        }
    }

    private static final class PositionRowMapper implements RowMapper<Position> {

        @Override
        public Position mapRow(ResultSet resultSet, int i) throws SQLException {
            Position position = new Position();

            position.setId(resultSet.getInt("id"));
            position.setName(resultSet.getString("name"));

            return position;
        }
    }
}
