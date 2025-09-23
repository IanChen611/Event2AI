package usecase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @Test
    public void seven_StickyNote_simulate_real_case_in_event_storming_some_geo_are_different_should_be_a_group_which_has_seven_members() {

        StickyNote stickyNote_1 = createStickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                StickyNoteColor.LIGHT_YELLOW.getHexCode());

        StickyNote stickyNote_2 = createStickyNote(
                "001B",
                "userId\nteamId",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.GREEN.getHexCode());

        StickyNote stickyNote_3 = createStickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, -50),
                new Point2D.Double(50, 50),
                StickyNoteColor.YELLOW.getHexCode());

        StickyNote stickyNote_4 = createStickyNote(
                "001D",
                "Invite Board Member",
                new Point2D.Double(0, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.BLUE.getHexCode());

        StickyNote stickyNote_5 = createStickyNote(
                "001E",
                "Board Member Joined",
                new Point2D.Double(110, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.ORANGE.getHexCode());

        StickyNote stickyNote_6 = createStickyNote(
                "001F",
                "NotifyBoard",
                new Point2D.Double(220, 10),
                new Point2D.Double(100, 50),
                StickyNoteColor.LIGHT_BLUE.getHexCode());

        StickyNote stickyNote_7 = createStickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, -60),
                new Point2D.Double(100, 100),
                StickyNoteColor.VIOLET.getHexCode());

        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);
        stickyNotes.add(stickyNote_5);
        stickyNotes.add(stickyNote_6);
        stickyNotes.add(stickyNote_7);

        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(1, groupAmount);
        assertTrue(checkClusterStickyNoteGroup(stickyNotes, clusterStickyNotesUseCase.getGroupByGroupIdx(0)));
    }

    @Test
    public void fourteen_StickyNote_simulate_real_case_in_event_storming_some_geo_are_different_should_be_two_groups_each_has_seven_members() {
        StickyNote stickyNote_1 = createStickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                StickyNoteColor.LIGHT_YELLOW.getHexCode());

        StickyNote stickyNote_2 = createStickyNote(
                "001B",
                "userId\nteamId",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.GREEN.getHexCode());

        StickyNote stickyNote_3 = createStickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, -50),
                new Point2D.Double(50, 50),
                StickyNoteColor.YELLOW.getHexCode());

        StickyNote stickyNote_4 = createStickyNote(
                "001D",
                "Invite Board Member",
                new Point2D.Double(0, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.BLUE.getHexCode());

        StickyNote stickyNote_5 = createStickyNote(
                "001E",
                "Board Member Joined",
                new Point2D.Double(110, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.ORANGE.getHexCode());

        StickyNote stickyNote_6 = createStickyNote(
                "001F",
                "NotifyBoard",
                new Point2D.Double(220, 10),
                new Point2D.Double(100, 50),
                StickyNoteColor.LIGHT_BLUE.getHexCode());

        StickyNote stickyNote_7 = createStickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, -60),
                new Point2D.Double(100, 100),
                StickyNoteColor.VIOLET.getHexCode());

        List<StickyNote> stickyNotes1 = new ArrayList<>();
        stickyNotes1.add(stickyNote_1);
        stickyNotes1.add(stickyNote_2);
        stickyNotes1.add(stickyNote_3);
        stickyNotes1.add(stickyNote_4);
        stickyNotes1.add(stickyNote_5);
        stickyNotes1.add(stickyNote_6);
        stickyNotes1.add(stickyNote_7);

        StickyNote stickyNote_8 = createStickyNote(
                "001A",
                "Team",
                new Point2D.Double(0 + 1000, 110),
                new Point2D.Double(100, 100),
                StickyNoteColor.LIGHT_YELLOW.getHexCode());

        StickyNote stickyNote_9 = createStickyNote(
                "001B",
                "userId\nteamId",
                new Point2D.Double(-110 + 1000, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.GREEN.getHexCode());

        StickyNote stickyNote_10 = createStickyNote(
                "001C",
                "User",
                new Point2D.Double(-50 + 1000, -50),
                new Point2D.Double(50, 50),
                StickyNoteColor.YELLOW.getHexCode());

        StickyNote stickyNote_11 = createStickyNote(
                "001D",
                "Invite Board Member",
                new Point2D.Double(0 + 1000, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.BLUE.getHexCode());

        StickyNote stickyNote_12 = createStickyNote(
                "001E",
                "Board Member Joined",
                new Point2D.Double(110 + 1000, 0),
                new Point2D.Double(100, 100),
                StickyNoteColor.ORANGE.getHexCode());

        StickyNote stickyNote_13 = createStickyNote(
                "001F",
                "NotifyBoard",
                new Point2D.Double(220 + 1000, 10),
                new Point2D.Double(100, 50),
                StickyNoteColor.LIGHT_BLUE.getHexCode());

        StickyNote stickyNote_14 = createStickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220 + 1000, -60),
                new Point2D.Double(100, 100),
                StickyNoteColor.VIOLET.getHexCode());

        List<StickyNote> stickyNotes2 = new ArrayList<>();
        stickyNotes2.add(stickyNote_8);
        stickyNotes2.add(stickyNote_9);
        stickyNotes2.add(stickyNote_10);
        stickyNotes2.add(stickyNote_11);
        stickyNotes2.add(stickyNote_12);
        stickyNotes2.add(stickyNote_13);
        stickyNotes2.add(stickyNote_14);

        List<StickyNote> combined = new ArrayList<>();
        combined.addAll(stickyNotes1);
        combined.addAll(stickyNotes2);

        List<List<StickyNote>> expectGroup = new ArrayList<>();
        expectGroup.add(stickyNotes1);
        expectGroup.add(stickyNotes2);

        System.out.println(combined); // [A, B, C, D]
        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(combined);
        int groupAmount = clusterStickyNotesUseCase.getGroupAmount();
        assertEquals(2, groupAmount);
        assertTrue(checkClusterStickyNoteGroups(expectGroup, clusterStickyNotesUseCase.getAllGroup()));
    }

    private StickyNote createStickyNote(String id, String desc, Point2D pos, Point2D geo, String color) {
        return new StickyNote(id, desc, pos, geo, color);
    }

    private Boolean checkClusterStickyNoteGroups(List<List<StickyNote>> stickyNoteGroups, List<List<StickyNote>> clusterStickyNoteGroups){
        if(clusterStickyNoteGroups.size() != stickyNoteGroups.size()) {
            return false;
        }
        boolean[] isStickyNoteGroupMatch = new boolean[stickyNoteGroups.size()];
        Arrays.fill(isStickyNoteGroupMatch, false);
        for(int i = 0; i < stickyNoteGroups.size(); i++) {
            for (List<StickyNote> clusterStickyNote : clusterStickyNoteGroups) {
                if (checkClusterStickyNoteGroup(stickyNoteGroups.get(i), clusterStickyNote)) {
                    isStickyNoteGroupMatch[i] = true;
                    break;
                }
            }
            if(!isStickyNoteGroupMatch[i]) {
                return false;
            }
        }
        return true;
    }
    private Boolean checkClusterStickyNoteGroup(List<StickyNote> stickyNoteGroup, List<StickyNote> clusterStickyNoteGroup) {
        if(clusterStickyNoteGroup.size() != stickyNoteGroup.size()) {
            return false;
        }
        boolean[] isStickyNoteMatch = new boolean[stickyNoteGroup.size()];
        Arrays.fill(isStickyNoteMatch, false);
        for(int i = 0; i < stickyNoteGroup.size(); i++) {
            for (StickyNote clusterStickyNote : clusterStickyNoteGroup) {
                if (stickyNoteGroup.get(i).getId().equals(clusterStickyNote.getId())) {
                    isStickyNoteMatch[i] = true;
                    break;
                }
            }
            if(!isStickyNoteMatch[i]) {
                return false;
            }
        }
        return true;
    }

    private enum StickyNoteColor {
        LIGHT_YELLOW("#AAAAAA"),
        YELLOW("#FFF59D"),   // Team, User
        GREEN("#A5D6A7"),    // userId, teamId...
        LIGHT_BLUE("#81D4FA"), // Invite Board Member
        ORANGE("#FFB74D"),   // Board Member Joined
        BLUE("#90CAF9"),     // NotifyBoard
        VIOLET("#B39DDB");   // Add member...

        private final String hexCode;

        StickyNoteColor(String hexCode) {
            this.hexCode = hexCode;
        }

        public String getHexCode() {
            return hexCode;
        }
    }

}