package org.kpi.senioroman4uk.tickets_accounting.dao;

import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */
public interface TicketsInvoiceDAO extends GenericDAO<TicketsInvoice> {
    boolean invoiceExsists(int id);
    int totalAmount(int id);
    int amountLeft(int id, int controlLetterId);
}
