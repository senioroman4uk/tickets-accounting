package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetterRow;

import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */
public interface ControlLetterRowDAO extends GenericDAO<ControlLetterRow> {
    List<ControlLetterRow> findAll(int controlLetterId);
}
