package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.GenericDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetterRow;

import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */
public interface ControlLetterRowService extends GenericService<ControlLetterRow> {
    List<ControlLetterRow> findAll(int controlLetterId);
}
