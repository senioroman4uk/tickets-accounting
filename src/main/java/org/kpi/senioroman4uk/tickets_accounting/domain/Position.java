package org.kpi.senioroman4uk.tickets_accounting.domain;

import org.springframework.web.servlet.View;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Vladyslav on 22.11.2015.
 *
 */

public class Position implements Serializable, ViewModel {
    private Integer id;
    private String name;

    public Position() {}

    public Position(Position position) {
        id = position.id;
        name = position.name;
    }
//    private HashMap<String, Permicion> permisions;

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return id == null;
    }

//    public HashMap<String, Permicion> getPermisions() {
//        return permisions;
//    }

//    public void setPermisions(HashMap<String, Permicion> permisions) {
//        this.permisions = permisions;
//    }
}
