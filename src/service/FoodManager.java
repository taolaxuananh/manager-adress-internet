package service;

import model.Food;
import storage.FileFood;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class FoodManager implements ICRUD<Food>{

    private final FileFood fileFood = FileFood.getInstance();

    private List<Food> foods;

    private static FoodManager foodManager;

    private FoodManager() throws IOException, ClassNotFoundException {
        foods = fileFood.readsFile();
    }

    public static FoodManager getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(foodManager)) {
            foodManager = new FoodManager();
        }
        return foodManager;
    }

    public String getFoods() throws IOException, ClassNotFoundException {
        foods = fileFood.readsFile();
        var foodsList = new StringBuilder();
        foods.forEach(c->foodsList.append(c.getId()).append(") ").append(c).append("\n"));
        return foodsList.toString();
    }

    public List<Food> getFoodList() throws IOException, ClassNotFoundException {
        return fileFood.readsFile();
    }

    public void setFoodList(List<Food> foods) throws IOException, ClassNotFoundException {
        fileFood.writeFile(foods);
        this.foods = fileFood.readsFile();
    }

    @Override
    public boolean add(Food food) throws IOException, ClassNotFoundException {
        if (Objects.isNull(findOne(food.getId()))) {
            foods.add(food);
            fileFood.writeFile(foods);
            foods = fileFood.readsFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(Integer id, String field, Objects newValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException {
        var getField = getField(field);
        var computer = findOne(id);
        getField.set(computer, newValue);
        fileFood.writeFile(foods);
        foods = fileFood.readsFile();
        return true;
    }

    private Field getField(String field) throws ClassNotFoundException, NoSuchFieldException {
        var getClass = Class.forName("model.Computer");
        var fieldOption = getClass.getDeclaredField(field);
        fieldOption.setAccessible(true);
        return fieldOption;
    }

    @Override
    public boolean delete(Integer id) throws IOException, ClassNotFoundException {
        var food = findOne(id);
        if (foods.remove(food)) {
            fileFood.writeFile(foods);
            foods = fileFood.readsFile();
            return true;
        }
        return false;
    }

    @Override
    public Food findOne(Integer id) throws IOException, ClassNotFoundException {
        foods = fileFood.readsFile();
        var foodOptional = foods.stream().filter(c-> c.getId().equals(id)).findFirst();
        return foodOptional.orElse(null);
    }

    public boolean saveFoodEdit(Food food) throws IOException, ClassNotFoundException {
        var index = getIndex(food.getId());
        if (index >= 0) {
            foods.set(index, food);
            fileFood.writeFile(foods);
            foods = fileFood.readsFile();
            return true;
        }
        return false;
    }

    public int getIndex(Integer idFood) throws IOException, ClassNotFoundException {
        foods = fileFood.readsFile();
        for (var i = 0; i <foods.size(); i++) {
            if (foods.get(i).getId().equals(idFood)) {
                return i;
            }
        }
        return -1;
    }
}
