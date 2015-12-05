package org.kpi.senioroman4uk.tickets_accounting.domain;

import java.sql.Date;
import java.util.List;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class ControlLetter {
    private int id;
    private int number;
    private double ticketPrice;
    private Date date;
    List<ControlLetterRow> rows;
    private Employee cashier;
}
