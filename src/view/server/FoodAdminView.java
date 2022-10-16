package view.server;

import controller.ServerController;
import model.Food;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class FoodAdminView {
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private final ServerController SERVER_CONTROLLER = ServerController.getInstance();
    private static FoodAdminView foodAdminView;

    private FoodAdminView() throws IOException, ClassNotFoundException {
    }

    public static FoodAdminView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(foodAdminView)) {
            foodAdminView = new FoodAdminView();
        }
        return foodAdminView;
    }


    public void showFoods() throws IOException, ClassNotFoundException {
        boolean checkListFood = SERVER_CONTROLLER.getFoodsList().isEmpty();
        if (!checkListFood) {
            SERVER_CONTROLLER.showAllFoods();
        } else {
            System.out.println("no menu yet");
        }
        System.out.println(!checkListFood ? "Pick one" : "");
        System.out.println("-2. Add a new food");
        System.out.println("-1. Back");
        var choose = NUMBER.nextInt();
        if (choose == -2) {
            addANewFoods();
        } else if (choose == -1) {
            ServerView serverView = ServerView.getInstance();
            serverView.loginSuccess();
        } else {
            detailFood(choose);
        }
    }

    private void detailFood(int foodId) throws IOException, ClassNotFoundException {
        var food = SERVER_CONTROLLER.findDetailFood(foodId);
        if (Objects.isNull(food)) {
            System.out.println("food not exist");
            showFoods();
        } else {
            System.out.println(food);
            System.out.println("1. Add quantity");
            System.out.println("2. Edit Price");
            System.out.println("3. Delete");
            var choose = NUMBER.nextInt();
            switch (choose) {
                case 1:
                    addQuantity(food);
                    break;
                case 2:
                    editPrice(food);
                    break;
                case 3:
                    deleteFood(food);
                    break;
                default:
                    System.out.println("There is no choice now");
                    showFoods();
                    break;
            }
        }
    }

    private void editPrice(Food food) throws IOException, ClassNotFoundException {
        System.out.println("Input new price");
        var newPrice = NUMBER.nextDouble();
        food.setPrice(newPrice);
        if (SERVER_CONTROLLER.saveEditFood(food)) {
            System.out.println("Add quantity success");
        } else {
            System.out.println("fails");
        }
        showFoods();
    }

    private void deleteFood(Food food) throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.deleteFood(food)) {
            System.out.println("Delete " + food.getName() + " success");
        } else {
            System.out.println("fails");
        }
        showFoods();
    }

    private void addQuantity(Food food) throws IOException, ClassNotFoundException {
        System.out.println("Input new quantity");
        var quantity = NUMBER.nextInt();
        food.setQuantity(food.getQuantity() + quantity);
        if (SERVER_CONTROLLER.saveEditFood(food)) {
            System.out.println("Add quantity success");
        } else {
            System.out.println("fails");
        }
        showFoods();
    }

    private void addANewFoods() throws IOException, ClassNotFoundException {
        int newId = SERVER_CONTROLLER.newId();
        System.out.println("Input food name");
        var foodName = LINE.nextLine();
        System.out.println("Input price");
        var price = NUMBER.nextDouble();
        System.out.println("Input quantity");
        var quantity = NUMBER.nextInt();
        var food = new Food(newId, foodName, price, quantity);
        if (SERVER_CONTROLLER.addANewFood(food)) {
            System.out.println("Added " + food.getName() + " success!!");
        } else {
            System.out.println("fails");
        }
        showFoods();
    }
}
