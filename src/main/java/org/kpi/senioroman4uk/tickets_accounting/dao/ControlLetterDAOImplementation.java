package org.kpi.senioroman4uk.tickets_accounting.dao;

import javafx.util.Pair;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.kpi.senioroman4uk.tickets_accounting.domain.RouteIncomeModel;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.function.Function;

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

    @Override
    public List<Pair<String, Double>> reportIncome(Date startDate, Date finishDate) {
        //language=SQL
        String sql = "SELECT SUM((clr.ticketsGiven - clr.ticketsReturned) * cl.price) total, DATEPART(mm, cl.date) [month] " +
                "FROM ControlLetterRow clr " +
                "INNER JOIN ControlLetter cl ON(cl.id = clr.ControlLetterId) " +
                "WHERE cl.date BETWEEN :startDate AND :finishDate " +
                "GROUP BY DATEPART(mm, cl.date)";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("finishDate", finishDate);

        return jdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            int month = resultSet.getInt("month");
            Locale locale = LocaleContextHolder.getLocale();

            DateFormatSymbols symbols = new DateFormatSymbols(locale);
            return new Pair<>(symbols.getShortMonths()[month - 1], resultSet.getDouble("total"));
        });
    }

    @Override
    public List<RouteIncomeModel> reportRoutesIncome(Date startDate, Date finishDate) {
        //language=SQL
        String sql = "SELECT r.id, r.number, CONVERT(DATE, cl.date) [date], ISNULL(SUM((clr.ticketsGiven - clr.ticketsReturned) * cl.price), 0) total " +
                "FROM Route r " +
                "LEFT JOIN ControlLetterRow clr ON(clr.routeId = r.id) " +
                "LEFT JOIN ControlLetter cl ON(clr.ControlLetterId = cl.id) " +
                "WHERE cl.date BETWEEN :startDate AND :finishDate OR [date] IS NULL " +
                "GROUP BY CONVERT(DATE, cl.date), r.id, r.number " +
                "ORDER BY [date]";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("finishDate", finishDate);

        return jdbcTemplate.query(sql, parameters, (ResultSetExtractor<List<RouteIncomeModel>>) resultSet -> {

            HashMap<Integer,RouteIncomeModel> results = new HashMap<>();
            HashSet<Date> dates = new HashSet<>();
            HashSet<Integer> routes = new HashSet<>();
            Hash f = (number, date) -> 31 + (16 << number) + date.hashCode() + 33;

            while (resultSet.next()) {
                Date date = resultSet.getDate("date");
                Integer route = (Integer) resultSet.getObject("number");
                if (!routes.contains(route))
                    routes.add(route);

                if (date != null && !dates.contains(date))
                    dates.add(date);
                else if (date == null) {
                    continue;
                }

                results.put(f.hash(route, date), new RouteIncomeModel(route, (Date)date.clone(), resultSet.getDouble("total")));
            }


            for(int route: routes)
                for(Date date : dates) {
                    if (!results.containsKey(f.hash(route, date)))
                        results.put(f.hash(route, date), new RouteIncomeModel(route, date, 0d));
                }

            return new ArrayList<>(results.values());
        });
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

    private interface Hash {
        int hash(int number, Date date);
    }
}
