package event2ai.stickynote;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;


public class StickyNoteGroupTest {

    @Test
    public void one_StickyNote_alone_should_be_a_Group_which_has_one_member() {
        String id = "001A";
        String desc = "demo StickyNote";
        Point2D pos = new Point2D.Double(10.5, 20.5);
        Point2D geo = new Point2D.Double(50, 50);
        String color = "#FFAA00";

        StickyNote stickyNote1 = new StickyNote(id, desc, pos, geo, color);
        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote1);

        Classifier classifier = new Classifier(stickyNotes);
        int groupAmount = classifier.getGroupAmount();
        assertEquals(1, groupAmount);
        List<StickyNote> group0 = classifier.getGroupByGroupIdx(0);
        assertEquals(group0.get(0).getId(), stickyNote1.getId());
    }


    @Test
    public void one_StickyNote_alone_should_be_a_Group_which_has_one_member() {
        String id_1 = "001A";
        String desc_1 = "demo StickyNote";
        Point2D pos_1 = new Point2D.Double(10, 10);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id, desc, pos, geo, color);

        String id_2 = "001A";
        String desc_2 = "demo StickyNote";
        Point2D pos_2 = new Point2D.Double(60, 10);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA00";
        StickyNote stickyNote_2 = new StickyNote(id, desc, pos, geo, color);



        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote1);
        stickyNotes.add(stickyNote2);

        Classifier classifier = new Classifier(stickyNotes);
        int groupAmount = classifier.getGroupAmount();
        assertEquals(1, groupAmount);
        List<StickyNote> group0 = classifier.getGroupByGroupIdx(0);
        for(int i = 0; i < group0.size(); i++) {
            assertEquals(stickyNotes.get(i).getId(), group0.get(i).getId());
        }
    }
}