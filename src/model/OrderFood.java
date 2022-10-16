package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderFood implements Serializable {
    private Integer id;
    private Person person;
    private Computer computer;
    private final Map<Food, Integer> foodMap = new HashMap<>();
    private Boolean status = false;

    public OrderFood() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Map<Food, Integer> getFoodMap() {
        return foodMap;
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("User: ").append(person.getName()).append(", Computer: ").append(computer.getCode()).append(status ? ", CONFIRM" : ", PENDING").append("\n");
        final Double[] quantities = {0D};
        foodMap.forEach((key, value) -> {
            stringBuilder.append(key.getName()).append(" : ").append(value).append("\n");
            quantities[0] += (value * key.getPrice());
        });
        stringBuilder.append("Bill: ").append(quantities[0]);
        return stringBuilder.toString();
    }
}
