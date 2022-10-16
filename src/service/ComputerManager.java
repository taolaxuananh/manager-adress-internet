package service;

import model.Computer;
import model.Person;
import storage.FileComputer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static service.ActionEdit.ADMIN;

public class ComputerManager implements ICRUD<Computer>{

    private final FileComputer fileComputer = FileComputer.getInstance();
    private List<Computer> computers;
    private final PersonManager personManager = PersonManager.getInstance();

    private static ComputerManager computerManager;

    private ComputerManager() throws IOException, ClassNotFoundException {
        computers = fileComputer.readsFile();
    }

    public static ComputerManager getInstance() throws IOException, ClassNotFoundException {
        if (computerManager == null) {
            computerManager = new ComputerManager();
        }
        return computerManager;
    }

    public List<Computer> getListComputer() throws IOException, ClassNotFoundException {
        return fileComputer.readsFile();
    }

    public String getComputers() throws IOException, ClassNotFoundException {
        var computerList = new StringBuilder();
        computers = fileComputer.readsFile();
        for (var i = 0; i < computers.size(); i++) {
            computerList.append(i).append(") ").append(computers.get(i).toString()).append("\n");
        }
        return computerList.toString();
    }


    public boolean add(Computer computer) throws IOException, ClassNotFoundException {
        if (Objects.isNull(findByIdAndCode(computer.getId(), computer.getCode()))) {
            computers.add(computer);
            fileComputer.writeFile(computers);
            computers = fileComputer.readsFile();
            return true;
        }
        return false;
    }

    public Computer findByIdAndCode(Integer id, String code) throws IOException, ClassNotFoundException {
        computers = fileComputer.readsFile();
        var computerOptional = computers.stream().filter(c-> c.getId().equals(id) && c.getCode().equals(code)).findFirst();
        return computerOptional.orElse(null);
    }

    @Override
    public boolean edit(Integer id, String field, Objects newValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException {
        var getField = getField(field);
        var computer = findOne(id);
        getField.set(computer, newValue);
        fileComputer.writeFile(computers);
        computers = fileComputer.readsFile();
        return true;
    }

    public void editComputer(Computer computer) throws IOException, ClassNotFoundException {
        int index = getIndex(computer);
        if (index >= 0) {
            computers.set(index, computer);
            fileComputer.writeFile(computers);
            computers = fileComputer.readsFile();
        }
    }

    private int getIndex(Computer computer) throws IOException, ClassNotFoundException {
        computers = fileComputer.readsFile();
        for (var i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId().equals(computer.getId())) {
                return i;
            }
        }
        return -1;
    }


    private Field getField(String field) throws ClassNotFoundException, NoSuchFieldException {
        var getClass = Class.forName("model.Computer");
        var fieldOption = getClass.getDeclaredField(field);
        fieldOption.setAccessible(true);
        return fieldOption;
    }

    @Override
    public boolean delete(Integer id) throws IOException {
        var computer = findOne(id);
        boolean isDelete = computers.remove(computer);
        fileComputer.writeFile(computers);
        return isDelete;
    }

    @Override
    public Computer findOne(Integer id) {
        var computerOptional = computers.stream().filter(c-> c.getId().equals(id)).findFirst();
        return computerOptional.orElse(null);
    }

    public boolean serverOpenComputer(Integer id, Person person) throws IOException, ClassNotFoundException {
        if (isServer(id, person.getUsername())) {
            var computer = findOne(id);
            computer.setStatus(true);
            computer.getPerson().setStatus(true);
            editComputer(computer);
            personManager.editPerson(computer.getPerson());
            return true;
        } else {
            return false;
        }
    }

    private boolean isServer(Integer id, String username) {
        return id.equals(0) && username.equals(ADMIN);
    }

    public boolean saveComputerEdit(Computer computer) throws IOException, ClassNotFoundException {
        int index = computers.indexOf(computer);
        if (index >= 0) {
            computers.set(index, computer);
            fileComputer.writeFile(computers);
            computers = fileComputer.readsFile();
            return true;
        }
        return false;
    }

    public boolean setListComputer(List<Computer> listComputer) throws IOException, ClassNotFoundException {
        fileComputer.writeFile(listComputer);
        computers = fileComputer.readsFile();
        return true;
    }

    public String showAllComputerClient() throws IOException, ClassNotFoundException {
        computers = fileComputer.readsFile();
        var showAllClient = new StringBuilder();
        computers.forEach(c->showAllClient.append(!c.getId().equals(0) ? c.toString() : "").append("\n"));
        return showAllClient.toString();
    }
}
