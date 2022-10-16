package view.server;

import controller.ServerController;
import model.Person;
import view.Server;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ServerView {
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private final ServerController SERVER_CONTROLLER;
    private final InfoAdminView INFO_ADMIN_VIEW = InfoAdminView.getInstance();
    private final ComputerManagerView COMPUTER_MANAGER_VIEW = ComputerManagerView.getInstance();
    private final FoodAdminView FOOD_ADMIN_VIEW = FoodAdminView.getInstance();
    private final ClientAdminView CLIENT_ADMIN_VIEW = ClientAdminView.getInstance();
    private final OrderAdminView ORDER_ADMIN_VIEW = OrderAdminView.getInstance();
    private static ServerView serverView;

    public static ServerView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(serverView)) {
            serverView = new ServerView();
        }
        return serverView;
    }

    private ServerView() throws IOException, ClassNotFoundException {
        SERVER_CONTROLLER = ServerController.getInstance();
    }

    public void login(boolean login) throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.findOneComputer(0).getPerson().getStatus() && SERVER_CONTROLLER.findOneComputer(0).getStatus()) {
            loginSuccess();
            return;
        }
        while (login) {
            System.out.println("Please enter username and password");
            String username = LINE.nextLine();
            String password = LINE.nextLine();
            if (SERVER_CONTROLLER.login(0, username, password)) {
                loginSuccess();
                login = false;
            }
        }
    }

    public void loginSuccess() throws IOException, ClassNotFoundException {
        var admin = SERVER_CONTROLLER.findOneComputer(0).getPerson();
        System.out.println("Please choose....");
        System.out.println("1. Show info Admin");
        System.out.println("2. Computer Manager");
        System.out.println("3. Food Manager");
        System.out.println("4. Client Manager");
        System.out.println("5. Order Manager");
        System.out.println("-1. Back");
        System.out.println("-2. Logout");
        int choose = NUMBER.nextInt();
        actionAfterChoose(choose, admin);
    }

    public void actionAfterChoose(int choose, Person admin) throws IOException, ClassNotFoundException {
        switch (choose) {
            case 1:
                INFO_ADMIN_VIEW.showInfo(admin);
                break;
            case 2:
                COMPUTER_MANAGER_VIEW.computerManager();
                break;
            case 3:
                FOOD_ADMIN_VIEW.showFoods();
                break;
            case 4:
                CLIENT_ADMIN_VIEW.showClients();
                break;
            case 5:
                ORDER_ADMIN_VIEW.showAllOrder();
                break;
            case -1:
                Server.main(new String[0]);
                break;
            case -2:
                logout();
                break;
            default:
                System.out.println("This choose is not available");
                Server.main(new String[0]);
                break;
        }
    }

    private void logout() throws IOException, ClassNotFoundException {
        SERVER_CONTROLLER.logOut();
        Server.main(new String[0]);
    }
}
