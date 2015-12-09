package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */

public class ControlLetterDAOImplementation extends GenericDAOImplementation<ControlLetter> implements ControlLetterDAO {
    @Override
    public boolean create(ControlLetter entity) {
        String sql = "INSERT INTO ControlLetter(incomeId, price, cassierId, collectionTime, [date], [amount]) " +
                "VALUES (:incomeId, :ticketPrice, :cassierId, :collectionTime, :date, :amount)";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("cassierId", entity.getCashier().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(ControlLetter entity) {
        String sql = "UPDATE ControlLetter " +
                "SET price = :ticketPrice, " +
                "cassierId = :cassierId, " +
                "collectionTime = :collectionTime, " +
                "[date] = :date, [amount] = :amount " +
                "WHERE id = :id";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("cassierId", entity.getCashier().getId());
        parameters.put("id", entity.getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public ControlLetter find(int id) {
        //language=SQL
        String sql = "SELECT cl.id, cassierId, collectionTime, date, incomeId, price, cl.amount," +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM ControlLetter cl " +
                "INNER JOIN Employee e ON(e.id = cl.cassierId AND e.type = :type)" +
                "INNER JOIN EmployeeType et ON(e.type = et.id) " +
                "WHERE cl.id = :id";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("type", Employee.EmployeePosition.CASIER.getId());

        return super.find(id, sql, new ControlLetterRowMapper(), parameters);
    }

    @Override
    public List<ControlLetter> findAll() {
        //language=SQL
        String sql = "SELECT cl.id, cassierId, collectionTime, date, incomeId, price, cl.amount," +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM ControlLetter cl " +
                "INNER JOIN Employee e ON(e.id = cl.cassierId AND e.type = :type)" +
                "INNER JOIN EmployeeType et ON(e.type = et.id)";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("type", Employee.EmployeePosition.CASIER.getId());

        return jdbcTemplate.query(sql, parameters, new ControlLetterRowMapper());
    }

    @Override
    public List<ControlLetter> findAll(int invoiceId) {
        //language=SQL
        String sql = "SELECT cl.id, cassierId, collectionTime, cl.date, incomeId, price, cl.amount," +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM ControlLetter cl " +
                "INNER JOIN Employee e ON(e.id = cl.cassierId AND e.type = :type)" +
                "INNER JOIN EmployeeType et ON(e.type = et.id) " +
                "INNER JOIN TicketsIncome ti ON(ti.id =  cl.incomeId) " +
                "WHERE ti.id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", invoiceId);
        parameters.put("type", Employee.EmployeePosition.CASIER.getId());

        return jdbcTemplate.query(sql, parameters, new ControlLetterRowMapper());
    }

    @Override
    public int totalAmount(int id) {
        String sql = "SELECT ISNULL(SUM(ticketsGiven), 0) amount FROM ControlLetterRow WHERE ControlLetterId = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ControlLetter WHERE id = :id";
        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public int amountLeft(int id, int controlLetterRowId) {
        String sql = "SELECT amount  - ISNULL((SELECT sum(ticketsGiven) FROM ControlLetterRow WHERE ControlLetterId = :id AND ControlLetterRow.id != :controlLetterRowId), 0)" +
                "FROM ControlLetter WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("controlLetterRowId", controlLetterRowId);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }

    public static final class ControlLetterRowMapper implements RowMapper<ControlLetter> {

        @Override
        public ControlLetter mapRow(ResultSet resultSet, int i) throws SQLException {
            ControlLetter controlLetter = new ControlLetter();
            controlLetter.setDate(resultSet.getDate("date"));
//            controlLetter.setNumber(resultSet.getInt("number"));
            controlLetter.setId(resultSet.getInt("id"));
            controlLetter.setIncomeId(resultSet.getInt("incomeId"));
            controlLetter.setTicketPrice(resultSet.getDouble("price"));
            controlLetter.setAmount(resultSet.getInt("amount"));
            controlLetter.setCollectionTime(resultSet.getDate("collectionTime"));

            controlLetter.setCashier(new EmployeeDAOImplementation.EmployeeRowMapper().mapRow(resultSet, i));
            return controlLetter;
        }
    }
}
