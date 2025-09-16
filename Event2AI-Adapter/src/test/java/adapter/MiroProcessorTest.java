package adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MiroProcessorTest {
    private MiroProcessor miroProcessor;

    @BeforeEach
    public void setUp() {miroProcessor = new MiroProcessor();}

    @Test
    public void MiroProcessor_can_process_file() {
        ArrayList<MiroJsonItem> miroJsonItems = miroProcessor.processFile("./src/test/example.json");
        assertEquals(2, miroJsonItems.size());
    }
}
