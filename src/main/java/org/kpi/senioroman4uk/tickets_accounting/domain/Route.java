package org.kpi.senioroman4uk.tickets_accounting.domain;

import java.io.Serializable;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class Route implements Serializable {
    private int id;
    private int number;
    private int length;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
