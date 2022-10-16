package service;

import java.io.IOException;
import java.util.Objects;

public interface ICRUD<T> {
    boolean add(T t) throws IOException, ClassNotFoundException;
    boolean edit(Integer id, String field, Objects newValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException;
    boolean delete(Integer id) throws IOException, ClassNotFoundException;
    T findOne(Integer id) throws IOException, ClassNotFoundException;
}
