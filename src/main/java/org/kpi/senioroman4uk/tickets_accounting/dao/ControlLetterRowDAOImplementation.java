package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetterRow;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */
public class ControlLetterRowDAOImplementation extends GenericDAOImplementation<ControlLetterRow> implements ControlLetterRowDAO {
    @Override
    public boolean create(ControlLetterRow entity) {
        String sql = "INSERT INTO ControlLetterRow(ControlLetterId, conductorId, routeId, ticketsGiven, ticketsReturned) " +
                "VALUES(:controlLetterId, :conductorId, :routeId, :ticketsGiven, :ticketsReturned) ";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("conductorId", entity.getConductor().getId());
        parameters.put("routeId", entity.getRoute().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(ControlLetterRow entity) {
        String sql = "UPDATE ControlLetterRow " +
                "SET conductorId = :conductorId, " +
                "routeId = :routeId, " +
                "ticketsGiven = :ticketsGiven, " +
                "ticketsReturned = :ticketsReturned " +
                "WHERE id = :id";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("conductorId", entity.getConductor().getId());
        parameters.put("routeId", entity.getRoute().getId());
        parameters.put("id", entity.getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public ControlLetterRow find(int id) {
        String sql = "SELECT clr.id, clr.ControlLetterId, clr.conductorId, clr.routeId, clr.ticketsGiven, clr.ticketsReturned, " +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name," +
                "r.length, r.number " +
                "FROM ControlLetterRow clr " +
                "INNER JOIN Employee e ON(e.id = clr.conductorId AND e.type = :type)" +
                "INNER JOIN EmployeeType et ON(e.type = et.id) " +
                "INNER JOIN ControlLetter cl ON(cl.id =  clr.ControlLetterId) " +
                "INNER JOIN Route r ON(r.id = clr.routeId)" +
                "WHERE clr.id = :id";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("type", Employee.EmployeePosition.CONDUCTOR.getId());

        return super.find(id, sql, new ControlLetterRowRowMapper(), parameters);
    }

    @Override
    public List<ControlLetterRow> findAll() {return null;}

    @Override
    public List<ControlLetterRow> findAll(int controlLetterId) {
        //language=SQL
        String sql = "SELECT clr.id, clr.ControlLetterId, clr.conductorId, clr.routeId, clr.ticketsGiven, clr.ticketsReturned, " +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name," +
                "r.length, r.number " +
                "FROM ControlLetterRow clr " +
                "INNER JOIN Employee e ON(e.id = clr.conductorId AND e.type = :type)" +
                "INNER JOIN EmployeeType et ON(e.type = et.id) " +
                "INNER JOIN ControlLetter cl ON(cl.id =  clr.ControlLetterId) " +
                "INNER JOIN Route r ON(r.id = clr.routeId)" +
                "WHERE cl.id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", controlLetterId);
        parameters.put("type", Employee.EmployeePosition.CONDUCTOR.getId());

        return jdbcTemplate.query(sql, parameters, new ControlLetterRowRowMapper());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ControlLetterRow WHERE id = :id";
        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    public static final class ControlLetterRowRowMapper implements RowMapper<ControlLetterRow> {

        @Override
        public ControlLetterRow mapRow(ResultSet resultSet, int i) throws SQLException {
            ControlLetterRow row = new ControlLetterRow();

            Route route = new RouteDAOImplementation.RouteRowMapper().mapRow(resultSet, i);
            Employee employee = new EmployeeDAOImplementation.EmployeeRowMapper().mapRow(resultSet, i);

            row.setId(resultSet.getInt("id"));
            row.setTicketsGiven(resultSet.getInt("ticketsGiven"));
            row.setTicketsReturned(resultSet.getInt("ticketsReturned"));
            row.setControlLetterId(resultSet.getInt("ControlLetterId"));
            row.setRoute(route);
            row.setConductor(employee);

            return row;
        }
    }
}
