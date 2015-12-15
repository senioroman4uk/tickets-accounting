package org.kpi.senioroman4uk.tickets_accounting.dao;

import javafx.util.Pair;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.kpi.senioroman4uk.tickets_accounting.domain.RouteIncomeModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */
public interface ControlLetterDAO extends GenericDAO<ControlLetter> {
    List<ControlLetter> findAll(int invoiceId);
    int totalAmount(int id);
    int amountLeft(int id, int controlLetterRowId);
    List<Pair<String, Double>> reportIncome(Date startDate, Date finishDate);
    List<RouteIncomeModel> reportRoutesIncome(Date startDate, Date finishDate);
}
