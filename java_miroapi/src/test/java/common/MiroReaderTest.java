package common;

import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MiroReaderTest {
    private MiroReader miroReader;

    @BeforeEach
    public void setUp() {miroReader = new MiroReader();}

    @Test
    public void MiroReader_should_read_example() {
        Optional<JsonArray> datas = miroReader.readBoardData("example.json");
        assertTrue(datas.isPresent());
    }
}
