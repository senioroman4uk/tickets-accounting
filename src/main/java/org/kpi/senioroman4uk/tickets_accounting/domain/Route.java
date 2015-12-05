package org.kpi.senioroman4uk.tickets_accounting.domain;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by Vladyslav on 21.11.2015.
 *
 */
public class Route implements Serializable, ViewModel {
    private Integer id;

    @Min(1)
    private int number;

    @Min(1)
    private int length;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean isNew() {
        return id == null;
    }
}
