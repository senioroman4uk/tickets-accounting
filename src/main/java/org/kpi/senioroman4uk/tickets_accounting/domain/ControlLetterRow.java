package org.kpi.senioroman4uk.tickets_accounting.domain;

import java.io.Serializable;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class ControlLetterRow implements Serializable {
    private int id;
    private Employee conductor;
    private Route route;
    private int ticketsGiven;
    private int ticketsReturned;
}
