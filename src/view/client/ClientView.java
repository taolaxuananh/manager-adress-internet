package view.client;
import controller.ClientController;
import model.Computer;
import model.OrderFood;
import view.Client;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ClientView {
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private static final Double DEFAULT_MONEY = 5000D;

    private final ClientController clientController = ClientController.getInstance();

    private static ClientView clientView;


    private ClientView() throws IOException, ClassNotFoundException {
    }

    public static ClientView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(clientView)) {
            clientView = new ClientView();
        }
        return clientView;
    }

    public void login(int computerId) throws IOException, ClassNotFoundException {
        var computer = clientController.findOneComputer(computerId);
        if (!Objects.isNull(computer) && Objects.isNull(computer.getPerson())) {
            System.out.println("Input username and password");
            var username = LINE.nextLine();
            var password = LINE.nextLine();
            var person = clientController.findByUserNameAndPassword(username, password);
            if (!Objects.isNull(person) && person.getWallet() >= DEFAULT_MONEY) {
                if (person.getStatus()) {
                    System.out.println("account is logged in on another machine");
                    chooseComputer();
                } else {
                    computer.setPerson(person);
                    clientController.setUpComputer(computer);
                    helloClient(computer);
                }
            } else {
                System.out.println("wrong account or not enough money");
                chooseComputer();
            }
        } else {
            System.out.println("computer is in use");
            chooseComputer();
        }
    }

    private void helloClient(Computer computer) throws IOException, ClassNotFoundException {
        if (computer.getStatus() && computer.getPerson().getStatus()) {
            System.out.println("Welcome " + computer.getPerson().getName() + ", login at " + computer.getJDate().getHourOpenString());
            System.out.println("Money : " + computer.getPerson().getWallet());
            System.out.println("1. Change name");
            System.out.println("2. Change password");
            System.out.println("3. Order foods");
            System.out.println("-1. logout");
            var choose = NUMBER.nextInt();
            switch (choose) {
                case -1:
                    logout(computer);
                    break;
                case 1:
                    changeName(computer);
                    break;
                case 2:
                    changePassword(computer);
                    break;
                case 3:
                    var orderFoods = new OrderFood();
                    orderFoods.setComputer(computer);
                    orderFoods.setPerson(computer.getPerson());
                    orderFoods.setId(clientController.getSizeOrder());
                    orderFoods(orderFoods);
                    break;
                default:
                    System.out.println("This choose is not available");
                    helloClient(computer);
                    break;
            }
        } else {
            System.out.println("Please login");
            login(computer.getId());
        }
    }

    private void orderFoods(OrderFood orderFood) throws IOException, ClassNotFoundException {
        var sizeFoods = clientController.getSizeFood();
        if (sizeFoods <= 0) {
            System.out.println("no food");
            helloClient(orderFood.getComputer());
        } else {
            clientController.showAllFoods();
            System.out.println("-1. Back");
            System.out.println("-2. Order done");
            System.out.println("Please choose food");
            var choose = NUMBER.nextInt();
            if (choose == -1) {
                helloClient(orderFood.getComputer());
            } else if (choose == -2) {
                clientController.saveOrderFoods(orderFood);
                System.out.println("order success");
                helloClient(orderFood.getComputer());
            } else if (choose >= sizeFoods) {
                System.out.println("This choice is not available");
                helloClient(orderFood.getComputer());
            } else {
                choiceFood(choose, orderFood);
            }
        }
    }

    private void choiceFood(int choose, OrderFood orderFood) throws IOException, ClassNotFoundException {
        var food = clientController.getOneFood(choose);
        System.out.println("you put " + food.getName());
        System.out.println("please input amount");
        var amount = NUMBER.nextInt();
        if (amount > food.getQuantity()) {
            System.out.println("Not enough quantity");
        } else {
            orderFood.getFoodMap().put(food, amount);
        }
        orderFoods(orderFood);
    }

    private void changePassword(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("input old password");
        var oldPass = LINE.nextLine();
        if (oldPass.equals(computer.getPerson().getPassword())) {
            System.out.println("input new password");
            var newPass = LINE.nextLine();
            computer.getPerson().setPassword(newPass);
            clientController.changePersonInfo(computer.getPerson());
            System.out.println("change password success");
        } else {
            System.out.println("wrong password");
        }
        helloClient(computer);
    }

    private void changeName(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("input a new name");
        var newName = LINE.nextLine();
        computer.getPerson().setName(newName);
        clientController.changePersonInfo(computer.getPerson());
        System.out.println("Hello " + computer.getPerson().getName());
        helloClient(computer);
    }

    private void logout(Computer computer) throws IOException, ClassNotFoundException {
        clientController.logout(computer);
        chooseComputer();
    }

    public void chooseComputer() throws IOException, ClassNotFoundException {
        if (clientController.isServer()) {
            var countComputer = clientController.countComputer();
            if (countComputer <= 0) {
                System.out.println("No computer yet");
            } else {
                clientController.showAllComputer();
                System.out.println("Pick one");
                System.out.println("-1. Back");
                var choose = NUMBER.nextInt();
                if (choose == -1) {
                    Client.main(new String[0]);
                }
                if (choose == 0) {
                    System.out.println("fuck off");
                }
                if (choose > countComputer) {
                    System.out.println("This computer belongs to another shop");
                } else {
                    login(choose);
                }
            }
        } else {
            System.out.println("server is shutting down");
        }
    }
}
