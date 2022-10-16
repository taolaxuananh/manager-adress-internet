package storage;

import model.Computer;
import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileComputer implements IActionFile<Computer> {
    private final String FILE_COMPUTER = "src/binaryFileObject/computers.txt";

    private FileComputer() throws IOException, ClassNotFoundException {
    }

    private static FileComputer fileComputer;
    private final FilePerson filePerson = FilePerson.getInstance();

    public static FileComputer getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(fileComputer)) {
            fileComputer = new FileComputer();
        }
        return fileComputer;
    }

    @Override
    public void writeFile(List<Computer> computers) throws IOException {
        var file = new File(FILE_COMPUTER);
        var os = new FileOutputStream(file);
        var oos = new ObjectOutputStream(os);
        oos.writeObject(computers);
        oos.close();
        os.close();
    }

    @Override
    public List<Computer> readsFile() throws IOException, ClassNotFoundException {
        var computers = new ArrayList<Computer>();
        var file = new File(FILE_COMPUTER);
        if (file.length() <= 0) {
            var persons = new ArrayList<Person>();
            var computer = new Computer();
            var admin = new Person();
            admin.setWallet(0D);
            admin.setId(0);
            admin.setStatus(false);
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setPassword("12345");
            computer.setPerson(admin);
            computer.setCode("SERVER");
            computer.setId(0);
            computers.add(computer);
            writeFile(computers);
            persons.add(admin);
            filePerson.writeFile(persons);
        }
        var is = new FileInputStream(file);
        var ois = new ObjectInputStream(is);
        computers = (ArrayList<Computer>) ois.readObject();
        ois.close();
        is.close();
        return computers;
    }
}
