package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */

public class TicketsInvoiceDAOImplementation extends GenericDAOImplementation<TicketsInvoice> implements TicketsInvoiceDAO {

    @Override
    public boolean create(TicketsInvoice entity) {
        String sql = "INSERT INTO TicketsIncome([amount], [date], [mainCasierId]) " +
                    "VALUES(:amount, :date, :employeeId)";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("employeeId", entity.getMainCashier().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean update(TicketsInvoice entity) {
        String sql = "UPDATE TicketsIncome " +
                "SET [amount] = :amount, " +
                "[date] = :date, " +
                "[mainCasierId] = :employeeId " +
                "WHERE id = :id";

        HashMap<String, Object> parameters = getObjectFields(sql, entity);
        parameters.put("employeeId", entity.getMainCashier().getId());

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public TicketsInvoice find(int id) {
        //language=SQL
        String sql = "SELECT ti.id, ti.amount, ti.mainCasierId, ti.[date], " +
                            "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM TicketsIncome ti " +
                "INNER JOIN Employee e ON(e.id = ti.mainCasierId AND e.type = :mainCasierTypeId) " +
                "INNER JOIN EmployeeType et ON(et.id = e.type)" +
                "WHERE ti.id = :id";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("mainCasierTypeId", Employee.EmployeePosition.MAIN_CASIER.getId());

        return super.find(id, sql, new TicketsInvoiceRowMapper(), parameters);
    }

    @Override
    public List<TicketsInvoice> findAll() {
        String sql = "SELECT ti.id, ti.amount, ti.mainCasierId, ti.[date], " +
                "e.id empid, e.type, e.firstname, e.lastname, e.middlename, e.birthDate, et.id typeId, et.name " +
                "FROM TicketsIncome ti " +
                "INNER JOIN Employee e ON(e.id = ti.mainCasierId AND e.type = :mainCasierTypeId) " +
                "INNER JOIN EmployeeType et ON(et.id = e.type)";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("mainCasierTypeId", Employee.EmployeePosition.MAIN_CASIER.getId());

        return jdbcTemplate.query(sql, parameters, new TicketsInvoiceRowMapper());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM TicketsIncome WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.update(sql, parameters) > 0;
    }

    @Override
    public boolean invoiceExsists(int id) {
        String sql = "SELECT COUNT(1) " +
                "FROM TicketsIncome " +
                "WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class) > 0;
    }

    private static final class TicketsInvoiceRowMapper implements RowMapper<TicketsInvoice> {

        @Override
        public TicketsInvoice mapRow(ResultSet resultSet, int i) throws SQLException {
            TicketsInvoice ticketsInvoice = new TicketsInvoice();

            ticketsInvoice.setId(resultSet.getInt("id"));
            ticketsInvoice.setAmount(resultSet.getInt("amount"));
            ticketsInvoice.setDate(resultSet.getDate("date"));

            EmployeeDAOImplementation.EmployeeRowMapper rowMapper = new EmployeeDAOImplementation.EmployeeRowMapper();
            ticketsInvoice.setMainCashier(rowMapper.mapRow(resultSet, i));

            return ticketsInvoice;
        }
    }

    @Override
    public int totalAmount(int id) {
        String sql = "SELECT ISNULL(SUM(amount), 0) amount FROM ControlLetter WHERE incomeId = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }

    @Override
    public int amountLeft(int id, int controlLetterId) {
        String sql = "SELECT amount  - ISNULL((SELECT sum(amount) FROM ControlLetter WHERE incomeId = :id AND ControlLetter.id <> :controlLetterId), 0)" +
                "FROM TicketsIncome WHERE id = :id";

        HashMap<String, Integer> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("controlLetterId", controlLetterId);

        return jdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}
