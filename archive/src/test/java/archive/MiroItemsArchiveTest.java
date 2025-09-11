package archive;

import core.*;
import event2ai.stickynote.StickyNote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MiroItemsArchiveTest {
    private MiroProcessor miroProcessor;
    private MiroItemsArchive miroItemsArchive;

    @BeforeEach
    public void setUp() {
        miroProcessor = new MiroProcessor();
        miroItemsArchive = new MiroItemsArchive();
    }

    @Test
    public void MiroItemsArchive_can_save() {
        ArrayList<MiroJsonItem> miroJsonItems = miroProcessor.processFile("./src/test/example.json");
        miroItemsArchive.save(miroJsonItems);
        assertEquals(miroJsonItems,  miroItemsArchive.load());
    }

    @Test
    public void MiroItemsArchive_can_getItemWith_stickyNote() {
        ArrayList<MiroJsonItem> miroJsonItems = miroProcessor.processFile("./src/test/example.json");
        miroItemsArchive.save(miroJsonItems);
        ArrayList<StickyNote> stickyNotes = miroItemsArchive.getItemsWith("StickyNote");
        assertEquals("3458764638678469633",  stickyNotes.get(0).getId());
        assertEquals("測試",  stickyNotes.get(0).getDescription());
        assertEquals(new Point2D.Double(1186.1195941727892, -314.73606314892874),  stickyNotes.get(0).getPos());
        assertEquals(new Point2D.Double(368.15000000000003, 421.8),  stickyNotes.get(0).getGeo());
        assertEquals("light_yellow",  stickyNotes.get(0).getColor());
    }
}
