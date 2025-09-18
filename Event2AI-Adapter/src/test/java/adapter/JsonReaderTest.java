package adapter;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() {
        jsonReader = new JsonReader();
    }

    @Test
    public void JsonReader_can_read_file() {
        JsonObject items = jsonReader.readBoardItem("./src/test/example.json");
        assertFalse(items.isEmpty());
    }

    @Test
    public void JsonReader_throw_exception_after_reading_file_failed() {
        JsonObject items = jsonReader.readBoardItem("./src/test/donotexistt.json");
        assertTrue(items.isEmpty());
    }
}
