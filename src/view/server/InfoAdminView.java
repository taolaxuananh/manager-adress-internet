package view.server;

import controller.ServerController;
import model.Person;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class InfoAdminView {
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private static InfoAdminView infoAdminView;
    private final ServerController SERVER_CONTROLLER = ServerController.getInstance();

    private InfoAdminView() throws IOException, ClassNotFoundException {
    }

    public static InfoAdminView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(infoAdminView)) {
            infoAdminView = new InfoAdminView();
        }
        return infoAdminView;
    }

    public void showInfo(Person admin) throws IOException, ClassNotFoundException {
        if (admin.getStatus()) {
            SERVER_CONTROLLER.showInfo(admin.getId());
        } else {
            System.out.println("Please login!");
            final ServerView serverView = ServerView.getInstance();
            serverView.login(true);
        }
        System.out.println("1. Edit info");
        System.out.println("2. Withdraw");
        System.out.println("-1. Back");
        int choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                editInfoAdmin(admin);
                break;
            case 2:
                adminWithDraw(admin);
                break;
            case -1:
                var serverView = ServerView.getInstance();
                serverView.loginSuccess();
                break;
            default:
                System.out.println("This choose is not available");
                break;
        }
    }

    private void adminWithDraw(Person admin) throws IOException, ClassNotFoundException {
        System.out.println("please admin enter password");
        var password = LINE.nextLine();
        if (admin.getPassword().equals(password)) {
            SERVER_CONTROLLER.withdraw(admin.getId());
        } else {
            System.out.println("wrong password");
        }
        showInfo(admin);

    }

    private void editInfoAdmin(Person admin) throws IOException, ClassNotFoundException {
        System.out.println("What information do you want to edit?");
        System.out.println("1. name");
        System.out.println("2. password");
        System.out.println("-1. Back");
        var choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                adminEditName(admin);
                break;
            case 2:
                adminEditPassword(admin);
                break;
            case -1:
                showInfo(admin);
                break;
            default:
                System.out.println("This choose not available");
                showInfo(admin);
                break;
        }
    }

    private void adminEditPassword(Person admin) throws IOException, ClassNotFoundException {
        System.out.println("Input a old password");
        var oldPass = LINE.nextLine();
        if (admin.getPassword().equals(oldPass)) {
            System.out.println("Input a new password");
            var newPass = LINE.nextLine();
            admin.setPassword(newPass);
            SERVER_CONTROLLER.adminEdit(admin);
        } else {
            System.out.println("Wrong password");
            showInfo(admin);
        }
    }

    private void adminEditName(Person admin) throws IOException, ClassNotFoundException {
        System.out.println("Input a new name");
        var newName = LINE.nextLine();
        admin.setName(newName);
        SERVER_CONTROLLER.adminEdit(admin);
        System.out.println("Hello " + admin.getName());
        showInfo(admin);
    }
}
