package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.ControlLetterRowDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.ControlLetterRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vladyslav on 07.12.2015.
 *
 */

@Service
public class ControlLetterRowServiceImplementation implements ControlLetterRowService {
    @Autowired
    private ControlLetterRowDAO controlLetterRowDAO;
    @Override
    public boolean create(ControlLetterRow entity) {
        return controlLetterRowDAO.create(entity);
    }

    @Override
    public boolean update(ControlLetterRow entity) {
        return controlLetterRowDAO.update(entity);
    }

    @Override
    public ControlLetterRow find(int id) {
        return controlLetterRowDAO.find(id);
    }

    @Override
    public List<ControlLetterRow> findAll() {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return controlLetterRowDAO.delete(id);
    }

    @Override
    public List<ControlLetterRow> findAll(int controlLetterId) {
        return controlLetterRowDAO.findAll(controlLetterId);
    }
}
