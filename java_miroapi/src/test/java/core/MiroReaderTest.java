package core;

import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class
MiroReaderTest {
    private MiroReader miroReader;

    @BeforeEach
    public void setUp() {miroReader = new MiroReader();}

    @Test
    public void MiroReader_can_read_file() {
        Optional<JsonArray> items = miroReader.readBoardItem("./src/test/example.json");
        assertTrue(items.isPresent());
    }
}
