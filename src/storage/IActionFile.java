package storage;

import java.io.IOException;
import java.util.List;

public interface IActionFile<T> {
    void writeFile(List<T> t) throws IOException;

    List<T> readsFile() throws IOException, ClassNotFoundException;
}
