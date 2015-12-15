package org.kpi.senioroman4uk.tickets_accounting.domain;

import java.util.Date;

/**
 * Created by Vladyslav on 13.12.2015.
 *
 */

public class RouteIncomeModel {
    public RouteIncomeModel() {}
    public RouteIncomeModel(Integer number, Date date, Double total) {
        this.number = number;
        this.date = date;
        this.total =total;
    }
   public Integer number;
   public Date date;
   public Double total;
}
