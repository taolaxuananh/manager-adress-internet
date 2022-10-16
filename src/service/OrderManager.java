package service;

import model.Food;
import model.OrderFood;
import storage.FileOrderFood;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static service.ActionEdit.ADMIN;

public class OrderManager {
    private List<OrderFood> orderFoods;
    private final FileOrderFood fileOrderFood = FileOrderFood.getInstance();
    private final FoodManager foodManager = FoodManager.getInstance();
    private final PersonManager personManager = PersonManager.getInstance();

    private static OrderManager orderManager;

    private OrderManager() throws IOException, ClassNotFoundException {
        orderFoods = fileOrderFood.readsFile();
    }

    public static OrderManager getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(orderManager)) {
            orderManager = new OrderManager();
        }
        return orderManager;
    }

    public List<OrderFood> getOrderFoods() throws IOException, ClassNotFoundException {
        return fileOrderFood.readsFile();
    }

    public boolean add(OrderFood orderFood) throws IOException, ClassNotFoundException {
        if (checkOrder(orderFood)) {
            this.orderFoods.add(orderFood);
            fileOrderFood.writeFile(this.orderFoods);
            this.orderFoods = fileOrderFood.readsFile();
            return true;
        }
        return false;
    }

    private boolean checkOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        var isFood = new Boolean[1];
        for (Food foodKey : orderFood.getFoodMap().keySet()) {
            isFood[0] = false;
            foodManager.getFoodList().forEach(f -> {
                if (foodKey.getId().equals(f.getId())) {
                    isFood[0] = true;
                }
            });
            if (!isFood[0]) {
                return false;
            }
        }
        return isFood[0];
    }

    public boolean paymentOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        final Double[] money = {0D};
        orderFood.getFoodMap().forEach((f, q) -> money[0] += f.getPrice() * q);
        var admin = personManager.getPersonList().stream().filter(p->p.getUsername().equals(ADMIN)).findFirst().orElse(null);
        if (!Objects.isNull(admin) && orderFoods.remove(orderFood)) {
            admin.setWallet(admin.getWallet() + money[0]);
            personManager.editPerson(admin);
            if (arrangement()) {
                fileOrderFood.writeFile(orderFoods);
                orderFoods = fileOrderFood.readsFile();
                return true;
            }
        }
        return false;
    }

    private boolean arrangement() throws IOException, ClassNotFoundException {
        orderFoods = fileOrderFood.readsFile();
        var isArrangement = false;
        if (orderFoods.isEmpty()) {
            return true;
        }
        for (int i = 0; i < orderFoods.size(); i++) {
            var orderFood = orderFoods.get(i);
            orderFood.setId(i);
            if (saveOrder(i,orderFood)) {
                isArrangement = true;
            } else {
                isArrangement = false;
                break;
            }
        }
        return isArrangement;
    }

    private boolean saveOrder(int index, OrderFood orderFood) throws IOException, ClassNotFoundException {
        if (index >= 0) {
            orderFoods.set(index, orderFood);
            fileOrderFood.writeFile(orderFoods);
            orderFoods = fileOrderFood.readsFile();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        try {
            orderFoods = fileOrderFood.readsFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < orderFoods.size(); i++) {
            stringBuilder.append("order ").append(i).append(": ").append(orderFoods.get(i).getPerson().getName()).append(", ").append(orderFoods.get(i).getComputer().getCode());
        }
        return stringBuilder.toString();
    }

    public OrderFood findOne(int orderId) throws IOException, ClassNotFoundException {
        orderFoods = fileOrderFood.readsFile();
        return orderFoods.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
    }

    public int findIndex(OrderFood orderFood) throws IOException, ClassNotFoundException {
        orderFoods = fileOrderFood.readsFile();
        for (var i = 0; i< orderFoods.size(); i++) {
            if (orderFoods.get(i).getId().equals(orderFood.getId())) {
                return i;
            }
        }
        return -1;
    }

    public void saveEditOrder(int index, OrderFood orderFood) throws IOException, ClassNotFoundException {
        orderFoods.set(index, orderFood);
        fileOrderFood.writeFile(orderFoods);
        orderFoods = fileOrderFood.readsFile();
    }
}
