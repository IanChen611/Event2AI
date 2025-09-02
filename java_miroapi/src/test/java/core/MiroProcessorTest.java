package core;

import model.MiroItem;
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
        ArrayList<MiroItem> miroItems = miroProcessor.processFile("./src/test/example.json");
        assertEquals(2, miroItems.size());
    }
}
