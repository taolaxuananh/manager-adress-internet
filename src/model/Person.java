package model;

import java.io.Serializable;

public class Person implements Serializable {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Double wallet;
    private Boolean status;

    public Person() {
    }

    public Person(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Person(Integer id, String name, String username, String password, Double wallet, Boolean status) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.wallet = wallet;
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return id + ") " + "Name: " + name + ", username: " + username + ", Wallet: " + wallet;
    }
}
