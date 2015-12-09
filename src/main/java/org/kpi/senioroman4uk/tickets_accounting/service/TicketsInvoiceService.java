package org.kpi.senioroman4uk.tickets_accounting.service;

import org.kpi.senioroman4uk.tickets_accounting.domain.TicketsInvoice;

/**
 * Created by Vladyslav on 06.12.2015.
 *
 */
public interface TicketsInvoiceService extends GenericService<TicketsInvoice> {
    boolean invoiceExists(int id);

    int totalAmount(Integer id);
    int amountLeft(int id, int controlLetterId);
}
