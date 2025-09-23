package adapter;

import com.google.gson.JsonObject;
import entity.StickyNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonFileCreatorTest {
    private JsonFileCreator jsonFileCreator;

    @BeforeEach
    public void setUp() {
        jsonFileCreator = new JsonFileCreator();
    }

    @Test
    public void create_a_json_file() {
        StickyNote stickyNote = new StickyNote("111", "0", new Point2D.Double(0, 0), new Point2D.Double(0, 0), "0");

        jsonFileCreator.create("./src/test/output.json", stickyNote);

        JsonReader jsonReader = new JsonReader();
        JsonObject items = jsonReader.readBoardItem("./src/test/output.json");
        assertFalse(items.isEmpty());
    }
}
