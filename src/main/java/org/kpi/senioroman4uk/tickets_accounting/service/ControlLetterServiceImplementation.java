package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.ControlLetterDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */

@Service
public class ControlLetterServiceImplementation implements ControlLetterService {
    @Autowired
    private ControlLetterDAO controlLetterDAO;

    @Override
    public boolean create(ControlLetter entity) {
        return controlLetterDAO.create(entity);
    }

    @Override
    public boolean update(ControlLetter entity) {
        return controlLetterDAO.update(entity);
    }

    @Override
    public ControlLetter find(int id) {
        return controlLetterDAO.find(id);
    }

    @Override
    public List<ControlLetter> findAll() {
        return controlLetterDAO.findAll();
    }

    @Override
    public List<ControlLetter> findAll(int invoiceId) {return controlLetterDAO.findAll(invoiceId);}

    @Override
    public int totalAmount(Integer id) {
        if (id == null)
            return 0;

        return controlLetterDAO.totalAmount(id);
    }

    @Override
    public int amountLeft(int id, Integer controlLetterRowId) {
        int rowId = controlLetterRowId == null ? -1 : controlLetterRowId;
        return controlLetterDAO.amountLeft(id, rowId);
    }

    @Override
    public boolean delete(int id) {
        return controlLetterDAO.delete(id);
    }
}
