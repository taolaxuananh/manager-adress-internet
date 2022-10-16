package storage;

import model.Food;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileFood implements IActionFile<Food> {

    private static FileFood fileFood;

    private final String FILE_FOOD = "src/binaryFileObject/foods.txt";

    private FileFood() {
    }

    public static FileFood getInstance() {
        if (Objects.isNull(fileFood)) {
            fileFood = new FileFood();
        }
        return fileFood;
    }

    @Override
    public void writeFile(List<Food> foods) throws IOException {
        File file = new File(FILE_FOOD);
        var os = new FileOutputStream(file);
        var oos = new ObjectOutputStream(os);
        oos.writeObject(foods);
        oos.close();
        os.close();
    }

    @Override
    public List<Food> readsFile() throws IOException, ClassNotFoundException {
        var foods = new ArrayList<Food>();
        File file = new File(FILE_FOOD);
        if (file.length() <= 0) {
            return foods;
        }
        var is = new FileInputStream(file);
        var ois = new ObjectInputStream(is);
        foods = (ArrayList<Food>) ois.readObject();
        return foods;
    }
}
