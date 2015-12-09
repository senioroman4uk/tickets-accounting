package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;

import java.util.List;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */
public interface ControlLetterDAO extends GenericDAO<ControlLetter> {
    List<ControlLetter> findAll(int invoiceId);
    int totalAmount(int id);
    int amountLeft(int id, int controlLetterRowId);
}
