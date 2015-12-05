package org.kpi.senioroman4uk.tickets_accounting.domain;


import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Vladyslav on 22.11.2015.
 *
 */
public class Employee implements Serializable, ViewModel {

    private Integer id;

    @NotNull(message = "employeeForm.position.required")
    private Position position;

    @Size(min = 2, max = 50)
    private String firstname;

    @Size(min = 2, max = 50)
    private String lastname;

    @Size(min = 2, max = 50)
    private String middlename;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date birthDate;
//    private String login;
//    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFullname() {
        return String.format("%s %s %s", lastname, firstname, middlename);
    }

    public boolean isNew() {
        return id == null;
    }
}
