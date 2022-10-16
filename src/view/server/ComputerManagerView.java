package view.server;
import controller.ServerController;
import model.Computer;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static service.ActionEdit.*;

public class ComputerManagerView {
    private static ComputerManagerView computerManagerView;
    private final Scanner LINE = new Scanner(System.in);
    private final Scanner NUMBER = new Scanner(System.in);
    private final Scanner CONFIRM = new Scanner(System.in);

    private final ServerController SERVER_CONTROLLER;

    private ComputerManagerView() throws IOException, ClassNotFoundException {
        SERVER_CONTROLLER = ServerController.getInstance();
    }

    public static ComputerManagerView getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(computerManagerView)) {
            computerManagerView = new ComputerManagerView();
        }
        return computerManagerView;
    }

    public void computerManager() throws IOException, ClassNotFoundException {
        SERVER_CONTROLLER.showAllComputer();
        System.out.println("Pick one!!");
        System.out.println("-1. Back");
        System.out.println("-2. Add new Computers");
        System.out.println("-3. Restructure Computers");
        int idComputer = NUMBER.nextInt();
        switch (idComputer) {
            case -1:
                ServerView serverView = ServerView.getInstance();
                serverView.loginSuccess();
                break;
            case -2:
                addComputer();
                break;
            case -3:
                restructure();
                break;
            default:
                showInfo(idComputer);
                break;
        }
    }

    private void restructure() throws IOException, ClassNotFoundException {
        if (SERVER_CONTROLLER.restructure()) {
            System.out.println("restructured!!!");
        } else {
            System.out.println("structure failed!!!");
        }
        computerManager();
    }

    private void addComputer() throws IOException, ClassNotFoundException {
        System.out.println("How many computers do you want to add???");
        int quantity = NUMBER.nextInt();
        if (SERVER_CONTROLLER.addComputer(quantity)) {
            System.out.println("Added " + quantity + "computers!!!");
            computerManager();
        } else {
            System.out.println("fails!!!");
        }
    }

    public void showInfo(Integer idComputer) throws IOException, ClassNotFoundException {
        var computer = SERVER_CONTROLLER.findOneComputer(idComputer);
        if (Objects.isNull(computer)) {
            computerManager();
            return;
        }
        System.out.println(computer);
        System.out.println("1. Edit");
        System.out.println("2. Delete");
        System.out.println("-1. Back");
        int choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                editComputer(computer);
                break;
            case 2:
                deleteComputer(computer);
                break;
            case -1:
                computerManager();
                break;
            default:
                System.out.println("Please choose again...");
                break;
        }
    }

    private void deleteComputer(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("Are you sure???");
        boolean isSure = CONFIRM.nextBoolean();
        if (isSure && SERVER_CONTROLLER.deleteComputer(computer)) {
            System.out.println("Delete " + computer.getCode() + " success!!!");
            computerManager();
        } else {
            showInfo(computer.getId());
        }
    }

    private void editComputer(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("What do you want to edit?");
        System.out.println("1." + STATUS);
        System.out.println("2." + CODE);
        System.out.println("-1. Back");
        int choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                editStatus(computer);
                break;
            case 2:
                editCode(computer);
                break;
            case -1:
                showInfo(computer.getId());
                break;
            default:
                System.out.println("Please choose again...");
        }
    }


    private void editCode(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("Input the code name you want to edit");
        var newCode = LINE.nextLine();
        computer.setCode(newCode);
        if (SERVER_CONTROLLER.saveComputerEdit(computer)) {
            System.out.println("Edit code success!!!");
        } else {
            System.out.println("Cannot edit!!!");
        }
        showInfo(computer.getId());
    }

    private void editStatus(Computer computer) throws IOException, ClassNotFoundException {
        System.out.println("Input the status name you want to edit");
        System.out.println("1. On");
        System.out.println("2. Off");
        int choose = NUMBER.nextInt();
        switch (choose) {
            case 1:
                computer.setStatus(true);
                break;
            case 2:
                computer.setStatus(false);
                break;
            default:
                showInfo(computer.getId());
                break;
        }
        if (SERVER_CONTROLLER.saveComputerEdit(computer)) {
            System.out.println("Edit code success!!!");
        } else {
            System.out.println("Cannot edit!!!");
        }
        showInfo(computer.getId());
    }
}
