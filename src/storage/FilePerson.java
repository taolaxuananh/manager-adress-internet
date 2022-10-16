package storage;

import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilePerson implements IActionFile<Person> {

    private final String FILE_PERSON = "src/binaryFileObject/persons.txt";

    private static FilePerson filePerson;

    private FilePerson() {
    }

    public static FilePerson getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(filePerson)) {
            filePerson = new FilePerson();
        }
        return filePerson;
    }

    @Override
    public void writeFile(List<Person> persons) throws IOException {
        var file = new File(FILE_PERSON);
        var os = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(persons);
        oos.close();
        os.close();
    }

    @Override
    public List<Person> readsFile() throws IOException, ClassNotFoundException {
        var persons = new ArrayList<Person>();
        File file = new File(FILE_PERSON);
        if (file.length() <= 0) {
            return persons;
        }
        var is = new FileInputStream(file);
        var ois = new ObjectInputStream(is);
        persons = (ArrayList<Person>) ois.readObject();
        ois.close();
        is.close();
        return persons;
    }
}
