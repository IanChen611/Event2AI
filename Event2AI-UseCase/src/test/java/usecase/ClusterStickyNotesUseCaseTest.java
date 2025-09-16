package usecase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.StickyNote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ClusterStickyNotesUseCaseTest {

    @Test
    public void one_StickyNote_alone_should_be_a_Group_which_has_one_member() {
        String id = "001A";
        String desc = "demo entity.StickyNote";
        Point2D pos = new Point2D.Double(10.5, 20.5);
        Point2D geo = new Point2D.Double(50, 50);
        String color = "#FFAA00";

        StickyNote stickyNote = new StickyNote(id, desc, pos, geo, color);
        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        List<StickyNote> group0 = clusterStickyNotesUseCase.getGroupByGroupIdx(0);
        Assertions.assertEquals(group0.get(0).getId(), stickyNote.getId());
    }


    @Test
    public void two_StickyNote_together_should_be_a_Group_which_has_two_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(10, 10);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001A";
        Point2D pos_2 = new Point2D.Double(60, 10);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA00";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);



        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }
    @Test
    public void four_StickyNote_that_two_two_together_should_be_two_Groups_that_each_has_two_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(10, 10);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001B";
        Point2D pos_2 = new Point2D.Double(60, 10);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA01";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);

        String id_3 = "002A";
        String desc_3 = "This is 002A";
        Point2D pos_3 = new Point2D.Double(150, 10);
        Point2D geo_3 = new Point2D.Double(50, 50);
        String color_3 = "#FFAA02";
        StickyNote stickyNote_3 = new StickyNote(id_3, desc_3, pos_3, geo_3, color_3);

        String id_4 = "002B";
        String desc_4 = "This is 002B";
        Point2D pos_4 = new Point2D.Double(200, 10);
        Point2D geo_4 = new Point2D.Double(50, 50);
        String color_4 = "#FFAA03";
        StickyNote stickyNote_4 = new StickyNote(id_4, desc_4, pos_4, geo_4, color_4);



        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(2, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }
    @Test
    public void four_StickyNote_whose_pos_is_small_square_should_be_a_group_which_has_four_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(0, 0);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001B";
        Point2D pos_2 = new Point2D.Double(60, 0);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA01";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);

        String id_3 = "001C";
        String desc_3 = "This is 001C";
        Point2D pos_3 = new Point2D.Double(60, 60);
        Point2D geo_3 = new Point2D.Double(50, 50);
        String color_3 = "#FFAA02";
        StickyNote stickyNote_3 = new StickyNote(id_3, desc_3, pos_3, geo_3, color_3);

        String id_4 = "001D";
        String desc_4 = "This is 001D";
        Point2D pos_4 = new Point2D.Double(0, 60);
        Point2D geo_4 = new Point2D.Double(50, 50);
        String color_4 = "#FFAA03";
        StickyNote stickyNote_4 = new StickyNote(id_4, desc_4, pos_4, geo_4, color_4);



        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }

    @Test
    public void three_StickyNote_whose_pos_is_small_triangle_should_be_a_group_which_has_three_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(0, 0);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001B";
        Point2D pos_2 = new Point2D.Double(0, 60);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA01";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);

        String id_3 = "001C";
        String desc_3 = "This is 001C";
        Point2D pos_3 = new Point2D.Double(60, 30);
        Point2D geo_3 = new Point2D.Double(50, 50);
        String color_3 = "#FFAA02";
        StickyNote stickyNote_3 = new StickyNote(id_3, desc_3, pos_3, geo_3, color_3);

        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }

    @Test
    public void three_StickyNote_whose_pos_is_right_triangle_should_be_a_group_which_has_three_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(0, 0);
        Point2D geo_1 = new Point2D.Double(50, 50);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001B";
        Point2D pos_2 = new Point2D.Double(0, 60);
        Point2D geo_2 = new Point2D.Double(50, 50);
        String color_2 = "#FFAA01";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);

        String id_3 = "001C";
        String desc_3 = "This is 001C";
        Point2D pos_3 = new Point2D.Double(60, 0);
        Point2D geo_3 = new Point2D.Double(50, 50);
        String color_3 = "#FFAA02";
        StickyNote stickyNote_3 = new StickyNote(id_3, desc_3, pos_3, geo_3, color_3);

        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }
    @Test
    public void three_StickyNote_whose_geo_are_different_should_be_a_group_which_has_three_members() {
        String id_1 = "001A";
        String desc_1 = "This is 001A";
        Point2D pos_1 = new Point2D.Double(0, 0);
        Point2D geo_1 = new Point2D.Double(100, 100);
        String color_1 = "#FFAA00";
        StickyNote stickyNote_1 = new StickyNote(id_1, desc_1, pos_1, geo_1, color_1);

        String id_2 = "001B";
        String desc_2 = "This is 001B";
        Point2D pos_2 = new Point2D.Double(70, 0);
        Point2D geo_2 = new Point2D.Double(40, 40);
        String color_2 = "#FFAA01";
        StickyNote stickyNote_2 = new StickyNote(id_2, desc_2, pos_2, geo_2, color_2);

        String id_3 = "001C";
        String desc_3 = "This is 001C";
        Point2D pos_3 = new Point2D.Double(0, 65);
        Point2D geo_3 = new Point2D.Double(100, 30);
        String color_3 = "#FFAA02";
        StickyNote stickyNote_3 = new StickyNote(id_3, desc_3, pos_3, geo_3, color_3);

        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        int count = 0;
        for(int i = 0; i < groupAmount; i++) {
            List<StickyNote> group = clusterStickyNotesUseCase.getGroupByGroupIdx(i);
            for (StickyNote stickyNote : group) {
                Assertions.assertEquals(stickyNotes.get(count++).getId(), stickyNote.getId());
            }
        }
    }
}