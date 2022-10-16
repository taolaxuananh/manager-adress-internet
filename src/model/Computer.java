package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Computer implements Serializable {
    private static final Double MONEY = 10000D;
    private Integer id;
    private String code;
    private Boolean status = false;
    private SystemDate systemDate;
    private Person person;

    public Computer() {
    }

    public Computer(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public void setJDate(SystemDate systemDate) {
        this.systemDate = systemDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public SystemDate getJDate() {
        return systemDate;
    }

    public int getHour() {
        return systemDate.getCurrenHour();
    }

    public void defaultJDate() {
        systemDate.setHourOpen(LocalDateTime.now());
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Double getMoney() {
        return Math.ceil(getHour() * (MONEY / 60));
    }

    public Double remainingAmount() {
        return person.getWallet() - getMoney();
    }

    @Override
    public String toString() {
        return "Computer: " + code + (id != 0 && status ? ", Money: " + remainingAmount() : "") +
                (person != null ? ", Player: " : "") + (person != null ? person.getName() : "");
    }
}
