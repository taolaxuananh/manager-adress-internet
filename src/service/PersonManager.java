package service;

import model.Person;
import storage.FilePerson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import static service.ActionEdit.ADMIN;

public class PersonManager implements ICRUD<Person>{
    private List<Person> persons;

    private final FilePerson filePerson = FilePerson.getInstance();

    private static PersonManager personManager;

    private PersonManager() throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
    }

    public static PersonManager getInstance() throws IOException, ClassNotFoundException {
        if (Objects.isNull(personManager)) {
            personManager = new PersonManager();
        }
        return personManager;
    }


    public String getPersons() throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        var perSonList = new StringBuilder();
        persons.forEach(p -> perSonList.append(!p.getId().equals(0) ? p : "").append("\n"));
        return perSonList.toString();
    }

    public List<Person> getPersonList() throws IOException, ClassNotFoundException {
        return filePerson.readsFile();
    }

    @Override
    public boolean add(Person person) throws IOException, ClassNotFoundException {
        if (Objects.isNull(findByIdAndUserName(person.getId(), person.getUsername()))) {
            persons.add(person);
            filePerson.writeFile(persons);
            persons = filePerson.readsFile();
            return true;
        }
        return false;
    }

    private Person findByIdAndUserName(Integer id, String username) throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        var personOptional = persons.stream().filter(p-> p.getId().equals(id) && p.getUsername().equals(username)).findFirst();
        return personOptional.orElse(null);
    }

    @Override
    public boolean edit(Integer id, String field, Objects newValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException {
        var getField = getField(field);
        var person = findOne(id);
        getField.set(person, newValue);
        filePerson.writeFile(persons);
        return true;
    }

    public void editPerson(Person person) throws IOException, ClassNotFoundException {
        int index = getIndex(person.getId());
        if (index >= 0) {
            persons.set(index, person);
            filePerson.writeFile(persons);
            persons = filePerson.readsFile();
        } else {
            System.out.println("Cannot edit!!!");
        }
                              }

    private int getIndex(Integer id) throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        for (var i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private Field getField(String field) throws ClassNotFoundException, NoSuchFieldException {
        var getClass = Class.forName("model.Person");
        var fieldOption = getClass.getDeclaredField(field);
        fieldOption.setAccessible(true);
        return fieldOption;
    }

    @Override
    public boolean delete(Integer id) throws IOException, ClassNotFoundException {
        var person = findOne(id);
        if (persons.remove(person)) {
            filePerson.writeFile(persons);
            persons = filePerson.readsFile();
            return true;
        }
        return false;
    }

    @Override
    public Person findOne(Integer id) throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        var person = persons.stream().filter(c-> c.getId().equals(id)).findFirst();
        return person.orElse(null);
    }

    public Person findByUserNameAndPassword(String username, String password) throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        return persons.stream().filter(c-> c.getUsername().equals(username) && c.getPassword().equals(password)).findFirst().orElse(null);
    }

    public boolean isAdmin() throws IOException, ClassNotFoundException {
        persons = filePerson.readsFile();
        return persons.stream().anyMatch(person -> person.getId().equals(0) && person.getUsername().equals(ADMIN) && person.getStatus().equals(true));
    }
}
