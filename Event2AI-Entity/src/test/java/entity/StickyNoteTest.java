package entity;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class StickyNoteTest {

    @Test
    public void construct_StickyNote_without_tag() {
        // ---Given a entity.StickyNote without tag---
        String id = "001A";
        String desc = "demo entity.StickyNote";
        Point2D pos = new Point2D.Double(10.5, 20.5);
        Point2D geo = new Point2D.Double(100, 50);
        String color = "#FFAA00";

        StickyNote stickyNote = new StickyNote(id, desc, pos, geo, color);

        // ---Then---
        Assertions.assertEquals(id, stickyNote.getId());
        Assertions.assertEquals(desc, stickyNote.getDescription());
        Assertions.assertSame(pos, stickyNote.getPos(), "entity.StickyNote's position should be the same value with pos.");
        Assertions.assertSame(geo, stickyNote.getGeo(), "entity.StickyNote's geometry should be the same value with geo.");
        Assertions.assertEquals(color, stickyNote.getColor());
        assertNull(stickyNote.getTag());
    }

    @Test
    public void construct_StickyNote_with_tag() {
        // ---Given a entity.StickyNote with tag---
        String id = "002A";
        String desc = "has tag";
        Point2D pos = new Point2D.Double(0, 0);
        Point2D geo = new Point2D.Double(300, 200);
        String color = "blue";
        String tag = "OK";

        StickyNote stickyNote = new StickyNote(id, desc, pos, geo, color, tag);

        // ---Then---
        Assertions.assertEquals(desc, stickyNote.getDescription());
        Assertions.assertSame(pos, stickyNote.getPos(), "entity.StickyNote's position should be the same value with pos.");
        Assertions.assertSame(geo, stickyNote.getGeo(), "entity.StickyNote's geometry should be the same value with geo.");
        Assertions.assertEquals(color, stickyNote.getColor());
        Assertions.assertEquals(tag, stickyNote.getTag());
    }
}
