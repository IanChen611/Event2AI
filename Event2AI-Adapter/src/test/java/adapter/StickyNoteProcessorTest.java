package adapter;

import com.google.gson.JsonArray;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import usecase.JsonToStickyNote;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StickyNoteProcessorTest {
    private StickyNoteProcessor stickyNoteProcessor;

    @BeforeEach
    public void setUp() {stickyNoteProcessor = new StickyNoteProcessor();}

    @Test
    public void JsonToStickyNote_can_return_StickyNote(){
        JsonReader jsonReader = new JsonReader();
        JsonArray jsonItems = jsonReader.readBoardItem("./src/test/example.json").get("stickyNotes").getAsJsonArray();
        ArrayList<StickyNote> items = JsonToStickyNote.convert(jsonItems);

        assertEquals("3458764639701211551", items.get(0).getId());
        assertEquals("測試5", items.get(0).getDescription());
        assertEquals("light_yellow", items.get(0).getColor());
        assertEquals(new Point2D.Double(3364.4755148009413, -2124.19073862342), items.get(0).getPos());
        assertEquals(new Point2D.Double(368.15000000000003, 421.8), items.get(0).getGeo());
        assertEquals("Done", items.get(0).getTag());
    }

    @Test
    public void JsonToStickyNote_get_empty_JsonArray_should_return_empty_ArrayList(){
        JsonArray jsonItems = new JsonArray();
        ArrayList<StickyNote> items = JsonToStickyNote.convert(jsonItems);
        assertEquals(new ArrayList<>(), items);
    }

    @Test
    public void MiroProcessor_can_process_file() {
        ArrayList<StickyNote> processedItems = stickyNoteProcessor.processFile("./src/test/example.json");

        assertEquals("3458764639701211551", processedItems.get(0).getId());
        assertEquals("測試5\n", processedItems.get(0).getDescription());
        assertEquals("light_yellow", processedItems.get(0).getColor());
        assertEquals(new Point2D.Double(3364.4755148009413, -2124.19073862342), processedItems.get(0).getPos());
        assertEquals(new Point2D.Double(368.15000000000003, 421.8), processedItems.get(0).getGeo());
        assertEquals("Done", processedItems.get(0).getTag());
    }
}
