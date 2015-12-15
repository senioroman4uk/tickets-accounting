package org.kpi.senioroman4uk.tickets_accounting.service;

import javafx.util.Pair;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.kpi.senioroman4uk.tickets_accounting.domain.RouteIncomeModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */
public interface ControlLetterService extends GenericService<ControlLetter> {
    List<ControlLetter> findAll(int invoiceId);
    int totalAmount(Integer id);
    int amountLeft(int id, Integer controlLetterRowId);
    List<Pair<String, Double>> reportIncome(Date startDate, Date finishDate);
    List<RouteIncomeModel> reportRoutesIncome(Date startDate, Date finishDate);
}
