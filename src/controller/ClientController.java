package controller;

import model.*;
import service.ComputerManager;
import service.FoodManager;
import service.OrderManager;
import service.PersonManager;

import java.io.IOException;
import java.util.Objects;

public class ClientController {
    private static ClientController clientController;
    private final ComputerManager computerManager = ComputerManager.getInstance();
    private final PersonManager personManager = PersonManager.getInstance();
    private final FoodManager foodManager = FoodManager.getInstance();
    private final OrderManager orderManager = OrderManager.getInstance();

    private ClientController() throws IOException, ClassNotFoundException {
    }

    public static ClientController getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(clientController)) {
            clientController = new ClientController();
        }
        return clientController;
    }

    public boolean isComputer() throws IOException, ClassNotFoundException {
        return computerManager.getListComputer().size() > 1;
    }

    public void showAllComputer() throws IOException, ClassNotFoundException {
        System.out.println(computerManager.showAllComputerClient());
    }

    public int countComputer() throws IOException, ClassNotFoundException {
        return computerManager.getListComputer().size() - 1;
    }

    public Computer findOneComputer(int computerId) {
        return computerManager.findOne(computerId);
    }

    public Person findByUserNameAndPassword(String username, String password) throws IOException, ClassNotFoundException {
        return personManager.findByUserNameAndPassword(username, password);
    }

    public void setUpComputer(Computer computer) throws IOException, ClassNotFoundException {
        computer.setJDate(new SystemDate());
        computer.defaultJDate();
        computer.setStatus(true);
        computer.getPerson().setStatus(true);
        computerManager.editComputer(computer);
        personManager.editPerson(computer.getPerson());
    }

    public boolean isServer() throws IOException, ClassNotFoundException {
        return computerManager.getListComputer().get(0).getStatus() && personManager.getPersonList().get(0).getStatus();
    }

    public void logout(Computer computer) throws IOException, ClassNotFoundException {
        computer.setStatus(false);
        computer.getPerson().setStatus(false);
        computer.setJDate(null);
        personManager.editPerson(computer.getPerson());
        computer.setPerson(null);
        computerManager.editComputer(computer);
    }

    public void changePersonInfo(Person person) throws IOException, ClassNotFoundException {
        personManager.editPerson(person);
    }

    public void showAllFoods() throws IOException, ClassNotFoundException {
        System.out.println(foodManager.getFoods());
    }

    public int getSizeFood() throws IOException, ClassNotFoundException {
        return foodManager.getFoodList().size();
    }

    public Food getOneFood(int foodId) throws IOException, ClassNotFoundException {
        return foodManager.findOne(foodId);
    }

    public void saveOrderFoods(OrderFood orderFood) throws IOException, ClassNotFoundException {
        orderManager.add(orderFood);
    }

    public Integer getSizeOrder() throws IOException, ClassNotFoundException {
        return orderManager.getOrderFoods().size();
    }
}
