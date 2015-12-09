package org.kpi.senioroman4uk.tickets_accounting.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class ControlLetterRow implements Serializable, ViewModel {
    private Integer id;
    @NotNull
    private Integer controlLetterId;
    @NotNull
    private Employee conductor;
    @NotNull
    private Route route;
    @Min(1)
    private int ticketsGiven;
    @Min(0)
    private int ticketsReturned;

    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public Integer getControlLetterId() {
        return controlLetterId;
    }

    public void setControlLetterId(Integer controlLetterId) {
        this.controlLetterId = controlLetterId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getConductor() {
        return conductor;
    }

    public void setConductor(Employee conductor) {
        this.conductor = conductor;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getTicketsGiven() {
        return ticketsGiven;
    }

    public void setTicketsGiven(int ticketsGiven) {
        this.ticketsGiven = ticketsGiven;
    }

    public int getTicketsReturned() {
        return ticketsReturned;
    }

    public void setTicketsReturned(int ticketsReturned) {
        this.ticketsReturned = ticketsReturned;
    }
}
