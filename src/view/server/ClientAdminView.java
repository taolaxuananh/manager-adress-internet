package view.server;

import controller.ServerController;
import model.Person;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ClientAdminView {
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private static ClientAdminView clientAdminView;
    private final ServerController SERVER_CONTROLLER = ServerController.getInstance();

    private ClientAdminView() throws IOException, ClassNotFoundException {
    }

    public static ClientAdminView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(clientAdminView)) {
            clientAdminView = new ClientAdminView();
        }
        return clientAdminView;
    }


    public void showClients() throws IOException, ClassNotFoundException {
        boolean isPerson = SERVER_CONTROLLER.isPerSon();
        if (!isPerson) {
            System.out.println("No member of the public");
        } else {
            SERVER_CONTROLLER.showAllClient();
        }
        System.out.println(isPerson ? "Pick one" : "");
        System.out.println("-2. Add member");
        System.out.println("-1. Back");
        var choose = NUMBER.nextInt();
        if (choose == -1) {
            ServerView serverView = ServerView.getInstance();
            serverView.loginSuccess();
        } else if (choose == -2) {
            addNewMember();
        } else {
            detailMember(choose);
        }
    }

    private void detailMember(int memberId) throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.isAdmin()) {
            var person = SERVER_CONTROLLER.findOneMember(memberId);
            if (!Objects.isNull(person)) {
                System.out.println(person);
                System.out.println("1. Change password");
                System.out.println("2. Recharge");
                System.out.println("-1. Back");
                var choose = NUMBER.nextInt();
                switch (choose) {
                    case 1:
                        changePassword(person);
                        break;
                    case 2:
                        recharge(person);
                        break;
                    default:
                        System.out.println("No this choice");
                }
            } else {
                System.out.println("This guy doesn't exist");
            }
        }
        showClients();
    }

    private void recharge(Person person) throws IOException, ClassNotFoundException {
        System.out.println("Input money");
        var money = NUMBER.nextDouble();
        person.setWallet(person.getWallet() + money);
        var admin = SERVER_CONTROLLER.findOneMember(0);
        admin.setWallet(admin.getWallet() + money);
        SERVER_CONTROLLER.adminEdit(admin);
        SERVER_CONTROLLER.adminEdit(person);
        System.out.println("Recharged success for " + person.getName());
    }

    private void changePassword(Person person) throws IOException, ClassNotFoundException {
        System.out.println("Input admin password");
        var adminPassword = LINE.nextLine();
        if (SERVER_CONTROLLER.findOneMember(0).getPassword().equals(adminPassword)) {
            System.out.println("Input new pass for " + person.getName());
            var newPassword = LINE.nextLine();
            person.setPassword(newPassword);
            SERVER_CONTROLLER.adminEdit(person);
            System.out.println("Change password success");
        } else {
            System.out.println("fails");
        }
        showClients();
    }

    private void addNewMember() throws IOException, ClassNotFoundException {
        var newId = SERVER_CONTROLLER.newMemberId();
        System.out.println("Input member name");
        var name = LINE.nextLine();
        System.out.println("Input member username");
        var username = LINE.nextLine();
        var DEFAULT_PASSWORD = "1";
        var person = new Person(newId, name, username, DEFAULT_PASSWORD, 0D, false);
        if (SERVER_CONTROLLER.addNewMember(person)) {
            System.out.println("Added " + person.getName());
        } else {
            System.out.println("fails");
        }
        showClients();
    }
}
