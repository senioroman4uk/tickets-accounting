package org.kpi.senioroman4uk.tickets_accounting.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class ControlLetter implements ViewModel {
    public ControlLetter() {
        date = new Date();
    }

    private Integer id;
    @NotNull
    private Integer incomeId;
    private int number;
    private double ticketPrice;
    @NotNull
    @DateTimeFormat(pattern = "y-M-d H:m:s")
    private Date date;
    private Date collectionTime;
    @Min(1)
    private int amount;

    @NotNull
    private Employee cashier;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getCashier() {
        return cashier;
    }

    public void setCashier(Employee cashier) {
        this.cashier = cashier;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(Date collectionTime) {
        this.collectionTime = collectionTime;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
