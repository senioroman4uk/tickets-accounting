package org.kpi.senioroman4uk.tickets_accounting.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Vladyslav on 22.11.2015.
 *
 */
public class TicketsInvoice implements Serializable, ViewModel {
    public TicketsInvoice() {
        date = new Date();
    }

    private Integer id;

    @NotNull
    private Employee MainCashier;

    @DateTimeFormat(pattern = "y-M-d H:m:s")
    @NotNull
    private Date date;

    @Min(1)
    private int amount;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getMainCashier() {
        return MainCashier;
    }

    public void setMainCashier(Employee mainCashier) {
        MainCashier = mainCashier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
