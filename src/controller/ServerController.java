package controller;

import model.Computer;
import model.Food;
import model.OrderFood;
import model.Person;
import service.ComputerManager;
import service.FoodManager;
import service.OrderManager;
import service.PersonManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static service.ActionEdit.*;

public class ServerController {

    private final PersonManager personManager = PersonManager.getInstance();

    private final ComputerManager computerManager = ComputerManager.getInstance();

    private final OrderManager orderManager = OrderManager.getInstance();

    private final FoodManager foodManager = FoodManager.getInstance();

    private static ServerController serverController;

    public static ServerController getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(serverController)) {
            serverController = new ServerController();
        }
        return serverController;
    }

    private ServerController() throws IOException, ClassNotFoundException {
    }


    public boolean login(Integer computerId, String username, String password) throws IOException, ClassNotFoundException {
        var person = personManager.findByUserNameAndPassword(username, password);
        if (!Objects.isNull(person) && computerManager.serverOpenComputer(computerId, person)) {
            System.out.println(HELLO_ADMIN + " " + person.getName());
            return true;
        }
        System.out.println(SERVER_MESSAGE);
        return false;
    }

    public void showAllFoods() throws IOException, ClassNotFoundException {
        System.out.println(foodManager.getFoods());
    }

    public List<Food> getFoodsList() throws IOException, ClassNotFoundException {
        return foodManager.getFoodList();
    }

    public void showInfo(Integer personId) throws IOException, ClassNotFoundException {
        var person = personManager.findOne(personId);
        if (!Objects.isNull(person) && person.getStatus().equals(true) && person.getUsername().equals(ADMIN)) {
            System.out.println(person);
        } else {
            System.out.println("You aren't Admin");
        }
    }

    public void withdraw(Integer personId) throws IOException, ClassNotFoundException {
        var person = personManager.findOne(personId);
        if (person.getStatus().equals(true) && person.getUsername().equals(ADMIN) && person.getWallet() > 0) {
            System.out.println("Withdraw " + person.getWallet() + "success");
            person.setWallet(0D);
        } else {
            System.out.println("You aren't Admin or no money");
        }
    }

    public void showOrder() {
        System.out.println(orderManager);
    }

    public void showAllComputer() throws IOException, ClassNotFoundException {
        System.out.println(computerManager.getComputers());
    }

    public Computer findOneComputer(Integer id) {
        var computer = computerManager.findOne(id);
        if (!Objects.isNull(computer)) {
            return computer;
        }
        System.out.println("This computer is not available");
        return null;
    }

    public boolean addComputer(int quantity) throws IOException, ClassNotFoundException {
        var isAdd = false;
        var computers = computerManager.getListComputer();
        var count = computers.size() + quantity;
        for (var i = computers.size(); i < count; i++) {
            var computer = new Computer(i, "COMPUTER " + i);
            if (computerManager.add(computer)) {
                isAdd = true;
            } else {
                isAdd = false;
                break;
            }
        }
        return isAdd;
    }

    public boolean deleteComputer(Computer computer) throws IOException {
        return computerManager.delete(computer.getId());
    }

    public boolean saveComputerEdit(Computer computer) throws IOException, ClassNotFoundException {
        return computerManager.saveComputerEdit(computer);
    }

    public boolean restructure() throws IOException, ClassNotFoundException {
        for (var i = 0; i < computerManager.getListComputer().size(); i++) {
            var code = "COMPUTER " + i;
            var computer = computerManager.getListComputer().get(i);
            computer.setCode(code);
            computer.setId(i);
            if (computer.getStatus() && i != 0) {
                reSet(computer);
            }
        }
        return computerManager.setListComputer(computerManager.getListComputer());
    }

    private void reSet(Computer computer) throws IOException, ClassNotFoundException {
        computer.setStatus(false);
        computer.getPerson().setStatus(false);
        computer.setJDate(null);
        personManager.editPerson(computer.getPerson());
        computer.setPerson(null);
        computerManager.editComputer(computer);
    }

    public void adminEdit(Person admin) throws IOException, ClassNotFoundException {
        personManager.editPerson(admin);
    }

    public boolean addANewFood(Food food) throws IOException, ClassNotFoundException {
        return foodManager.add(food);
    }

    public int newId() throws IOException, ClassNotFoundException {
        return foodManager.getFoodList().size();
    }

    public Food findDetailFood(int foodId) throws IOException, ClassNotFoundException {
        return foodManager.findOne(foodId);
    }

    public boolean saveEditFood(Food food) throws IOException, ClassNotFoundException {
        return foodManager.saveFoodEdit(food);
    }

    public boolean deleteFood(Food food) throws IOException, ClassNotFoundException {
        var isDelete = false;
        List<Food> foods = foodManager.getFoodList();
        foods.remove(food);
        if (foods.isEmpty()) {
            foodManager.setFoodList(foodManager.getFoodList());
            return true;
        }
        for (var i = 0; i < foods.size(); i++) {
            foods.get(i).setId(i);
            if (foodManager.saveFoodEdit(foods.get(i))) {
                isDelete = true;
            } else {
                isDelete = false;
                break;
            }
        }
        return isDelete;
    }

    public void showAllClient() throws IOException, ClassNotFoundException {
        System.out.println(personManager.getPersons());
    }

    public boolean isPerSon() throws IOException, ClassNotFoundException {
        return personManager.getPersonList().size() > 1;
    }

    public boolean isAdmin() throws IOException, ClassNotFoundException {
        return personManager.isAdmin();
    }

    public Person findOneMember(int memberId) throws IOException, ClassNotFoundException {
        return personManager.findOne(memberId);
    }

    public int newMemberId() throws IOException, ClassNotFoundException {
        return personManager.getPersonList().size();
    }

    public boolean addNewMember(Person person) throws IOException, ClassNotFoundException {
        return personManager.add(person);
    }

    public boolean isOrder() throws IOException, ClassNotFoundException {
        return orderManager.getOrderFoods().size() > 0;
    }

    public OrderFood findOneOrder(int orderId) throws IOException, ClassNotFoundException {
        return orderManager.findOne(orderId);
    }

    public boolean paymentOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        return orderManager.paymentOrder(orderFood);
    }

    public void saveEditOrder(OrderFood orderFood) throws IOException, ClassNotFoundException {
        var index = orderManager.findIndex(orderFood);
        orderManager.saveEditOrder(index, orderFood);
    }

    public void logOut() throws IOException, ClassNotFoundException {
        var computer = computerManager.getListComputer().get(0);
        computer.setStatus(false);
        computer.getPerson().setStatus(false);
        computerManager.editComputer(computer);
        personManager.editPerson(computer.getPerson());
    }
}
