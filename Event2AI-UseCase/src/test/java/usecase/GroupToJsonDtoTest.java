package usecase;

import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import valueobject.PublishEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupToJsonDtoTest {
    @Test
    public void group_can_be_put_into_GroupToJsonDto() {
        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                "light yellow");

        StickyNote stickyNote_2 = new StickyNote(
                "001B",
                "userId\nteamId",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = new StickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, -50),
                new Point2D.Double(50, 50),
                "yellow");

        StickyNote stickyNote_4 = new StickyNote(
                "001D",
                "Invite Board Member",
                new Point2D.Double(0, 0),
                new Point2D.Double(100, 100),
                "blue");

        StickyNote stickyNote_5 = new StickyNote(
                "001E",
                "BoardMemberJoined",
                new Point2D.Double(110, 0),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_6 = new StickyNote(
                "001F",
                "NotifyBoard",
                new Point2D.Double(220, 10),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, -60),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment",
                new Point2D.Double(100, 100),
                new Point2D.Double(200, 170),
                "white");

        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);
        stickyNotes.add(stickyNote_5);
        stickyNotes.add(stickyNote_6);
        stickyNotes.add(stickyNote_7);
        stickyNotes.add(stickyNote_8);

        Group group = new Group();
        group.setGroupId(stickyNote_4.getId());
        group.setUseCaseName(stickyNote_4.getDescription());
        group.setInput(Arrays.asList(stickyNote_2.getDescription().split("\\n")));
        group.setAggregateName(stickyNote_1.getDescription());
        group.setUserName(stickyNote_3.getDescription());
        group.setComment(List.of(stickyNote_8.getDescription()));
        group.setPublishEvents(List.of(new PublishEvent(
                stickyNote_5.getDescription(),
                stickyNote_6.getDescription(),
                stickyNote_7.getDescription())));

        GroupToJsonDto groupToJsonDto = new GroupToJsonDto(group);
        assertNotNull(groupToJsonDto);
    }
}
