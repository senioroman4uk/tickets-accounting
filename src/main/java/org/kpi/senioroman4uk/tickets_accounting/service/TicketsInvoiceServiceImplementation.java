package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.dao.TicketsInvoiceDAO;
import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */

@Service
public class TicketsInvoiceServiceImplementation implements TicketsInvoiceService {
    @Autowired
    private TicketsInvoiceDAO ticketsInvoiceDAO;

    @Override
    public boolean create(TicketsInvoice entity) {
        return ticketsInvoiceDAO.create(entity);
    }

    @Override
    public boolean update(TicketsInvoice entity) {
        return ticketsInvoiceDAO.update(entity);
    }

    @Override
    public TicketsInvoice find(int id) {
        return ticketsInvoiceDAO.find(id);
    }

    @Override
    public List<TicketsInvoice> findAll() {
        return ticketsInvoiceDAO.findAll();
    }

    @Override
    public boolean delete(int id) {
        return ticketsInvoiceDAO.delete(id);
    }

    @Override
    public boolean invoiceExists(int id) {
        return ticketsInvoiceDAO.invoiceExsists(id);
    }

    @Override
    public int totalAmount(Integer id) {
        if(id == null)
            return 0;

        return ticketsInvoiceDAO.totalAmount(id);
    }

    @Override
    public int amountLeft(int id, int controlLetterId) {
        return ticketsInvoiceDAO.amountLeft(id, controlLetterId);
    }
}
