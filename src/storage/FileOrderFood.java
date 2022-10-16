package storage;

import model.OrderFood;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileOrderFood implements IActionFile<OrderFood> {
    private static FileOrderFood fileOrderFood;
    private final String FILE_ORDER = "src/binaryFileObject/orders.txt";

    private FileOrderFood() {
    }

    public static FileOrderFood getInstance() {
        if (Objects.isNull(fileOrderFood)) {
            fileOrderFood = new FileOrderFood();
        }
        return fileOrderFood;
    }

    @Override
    public void writeFile(List<OrderFood> orders) throws IOException {
        var file = new File(FILE_ORDER);
        var os = new FileOutputStream(file);
        var oos = new ObjectOutputStream(os);
        oos.writeObject(orders);
        oos.close();
        os.close();
    }

    @Override
    public List<OrderFood> readsFile() throws IOException, ClassNotFoundException {
        var orders = new ArrayList<OrderFood>();

        var file = new File(FILE_ORDER);
        if (file.length() <= 0) {
            return orders;
        }
        var is = new FileInputStream(file);
        var ois = new ObjectInputStream(is);
        orders = (ArrayList<OrderFood>) ois.readObject();
        return orders;

    }
}
