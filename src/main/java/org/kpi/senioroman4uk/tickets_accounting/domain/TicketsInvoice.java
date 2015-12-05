package org.kpi.senioroman4uk.tickets_accounting.domain;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Vladyslav on 22.11.2015.
 *
 */
public class TicketsInvoice implements Serializable {
    private int id;
    private Employee MainCashier;
    private Date date;
    private int amount;
}
