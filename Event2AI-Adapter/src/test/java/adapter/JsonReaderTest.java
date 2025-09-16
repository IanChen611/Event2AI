package adapter;

import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class
JsonReaderTest {
    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() {
        jsonReader = new JsonReader();}

    @Test
    public void MiroReader_can_read_file() {
        JsonArray items = jsonReader.readBoardItem("./src/test/example.json");
        assertFalse(items.isEmpty());
    }
}
