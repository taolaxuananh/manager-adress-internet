package view.server;

import controller.ServerController;
import model.Food;
import model.OrderFood;

import java.io.IOException;
import java.util.*;

public class OrderAdminView {
    private final Scanner NUMBER = new Scanner(System.in);
    private final ServerController SERVER_CONTROLLER = ServerController.getInstance();
    private static OrderAdminView orderAdminView;

    private OrderAdminView() throws IOException, ClassNotFoundException {
    }

    public static OrderAdminView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(orderAdminView)) {
            orderAdminView = new OrderAdminView();
        }
        return orderAdminView;
    }

    public void showAllOrder() throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.isAdmin()) {
            var isOrder = SERVER_CONTROLLER.isOrder();
            if (!isOrder) {
                System.out.println("Not have order");
            } else {
                SERVER_CONTROLLER.showOrder();
            }
            System.out.println(isOrder ? "Pick one" : "");
            System.out.println("-1. Back");
            var choose = NUMBER.nextInt();
            if (choose == -1) {
                ServerView serverView = ServerView.getInstance();
                serverView.loginSuccess();
            } else {
                detailOrder(choose);
            }
        } else {
            System.out.println("Please login");
        }
    }

    private void detailOrder(int orderId) throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.isAdmin()) {
            var orderFood = SERVER_CONTROLLER.findOneOrder(orderId);
            if (Objects.isNull(orderFood)) {
                System.out.println("Cannot find this order");
                showAllOrder();
            } else {
                System.out.println(orderFood);
                System.out.println("1. Edit order");
                System.out.println("2. Payment order");
                System.out.println("3. Confirm order");
                System.out.println("4. ");
                System.out.println("-1. Back");
                var choose = NUMBER.nextInt();
                switch (choose) {
                    case 1:
                        editOrder(orderFood);
                        break;
                    case 2:
                        paymentOrder(orderFood);
                        break;
                    case 3:
                        confirmOrder(orderFood);
                        break;
                    case -1:
                        showAllOrder();
                        break;
                    default:
                        System.out.println("This choice is not available");
                        showAllOrder();
                        break;
                }
            }
        }
    }

    private void confirmOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        orderFood.setStatus(true);
        SERVER_CONTROLLER.saveEditOrder(orderFood);
        System.out.println("confirm success!!");
        showAllOrder();
    }

    private void paymentOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.paymentOrder(orderFood)) {
            System.out.println("payment for" + orderFood.getPerson().getName() + " success");
        } else {
            System.out.println("fails");
        }
        showAllOrder();
    }

    private void editOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        System.out.println("-1. Back");
        var choose = NUMBER.nextInt();
        var i = 0;
        List<Food> foods = new ArrayList<>();
        for (Map.Entry<Food, Integer> entry : orderFood.getFoodMap().entrySet()) {
            System.out.println(i + ") " + entry.getKey().getName() + " : " + entry.getValue());
            foods.add(entry.getKey());
        }
        if (choose == -1) {
            showAllOrder();
        } else {
            editFood(foods.get(choose), orderFood);
        }
    }

    private void editFood(Food food, OrderFood orderFood) throws IOException, ClassNotFoundException {
        System.out.println("1. quantity reset");
        System.out.println("2. delete food");
        var choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                quantityReset(food, orderFood);
                break;
            case 2:
                deleteFood(food, orderFood);
                break;
            default:
                System.out.println("This choose is not available");
                editOrder(orderFood);
                break;
        }
    }

    private void deleteFood(Food food, OrderFood orderFood) throws IOException, ClassNotFoundException {
        if (Objects.isNull(orderFood.getFoodMap().remove(food))) {
            System.out.println("fails");
        } else {
            SERVER_CONTROLLER.saveEditOrder(orderFood);
            System.out.println("Delete " + food.getName() + " success");
        }
    }

    private void quantityReset(Food food, OrderFood orderFood) throws IOException, ClassNotFoundException {
        System.out.println("Input new quantity");
        var newQuantity = NUMBER.nextInt();
        orderFood.getFoodMap().put(food, newQuantity);
        SERVER_CONTROLLER.saveEditOrder(orderFood);
    }
}
