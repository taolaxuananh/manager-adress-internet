package view;

import view.client.ClientView;

import java.util.InputMismatchException;

public class Client {
    public static void main(String[] args) {
        try {
            ClientView clientView = ClientView.getInstance();
            clientView.chooseComputer();
        } catch (InputMismatchException e) {
            System.out.println("Input the wrong format");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
