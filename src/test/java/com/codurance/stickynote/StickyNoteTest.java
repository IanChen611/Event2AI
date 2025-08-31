package com.codurance.stickynote;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class StickyNoteTest {

    @Test
    public void construct_StickyNote_without_tag() {
        // ---Given a StickyNote without tag---
        long id = 101L;
        String desc = "demo StickyNote";
        Point2D pos = new Point2D.Double(10.5, 20.5);
        Point2D geo = new Point2D.Double(100, 50);
        String color = "#FFAA00";

        StickyNote stickyNote = new StickyNote(id, desc, pos, geo, color);

        // ---Then---
        assertEquals(id, stickyNote.getId());
        assertEquals(desc, stickyNote.getDescription());
        assertSame(pos, stickyNote.getPos(), "StickyNote's position should be the same value with pos.");
        assertSame(geo, stickyNote.getGeo(), "StickyNote's geometry should be the same value with geo.");
        assertEquals(color, stickyNote.getColor());
        assertNull(stickyNote.getTag());
    }

    @Test
    public void construct_StickyNote_with_tag() {
        // ---Given a StickyNote with tag---
        long id = 7L;
        String desc = "has tag";
        Point2D pos = new Point2D.Double(0, 0);
        Point2D geo = new Point2D.Double(300, 200);
        String color = "blue";
        String tag = "OK";

        StickyNote stickyNote = new StickyNote(id, desc, pos, geo, color, tag);

        // ---Then---
        assertEquals(desc, stickyNote.getDescription());
        assertSame(pos, stickyNote.getPos(), "StickyNote's position should be the same value with pos.");
        assertSame(geo, stickyNote.getGeo(), "StickyNote's geometry should be the same value with geo.");
        assertEquals(color, stickyNote.getColor());
        assertEquals(tag, stickyNote.getTag());
    }
}
