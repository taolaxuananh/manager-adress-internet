package view;
import storage.FileComputer;
import view.server.ServerView;
import java.util.InputMismatchException;


public class Server {
    public static void main(String[] args) {
        try {
            FileComputer fileComputer = FileComputer.getInstance();
            fileComputer.readsFile();
            ServerView adminView = ServerView.getInstance();
            adminView.login(true);
        } catch (InputMismatchException e) {
            System.out.println("Input the wrong format");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
