package usecase;

import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import valueobject.PublishEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassifierStickNotesUseCaseTest {

    @Test
    public void a_complete_event_storming() {
        final int USE_CASE_STICKYNOTE_INDEX = 3;

        StickyNote stickyNote_1 = createStickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                "light yellow");

        StickyNote stickyNote_2 = createStickyNote(
                "001B",
                "userId\nteamId",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = createStickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, -50),
                new Point2D.Double(50, 50),
                "yellow");

        StickyNote stickyNote_4 = createStickyNote(
                "001D",
                "Invite Board Member",
                new Point2D.Double(0, 0),
                new Point2D.Double(100, 100),
                "blue");

        StickyNote stickyNote_5 = createStickyNote(
                "001E",
                "BoardMemberJoined",
                new Point2D.Double(110, 0),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_6 = createStickyNote(
                "001F",
                "NotifyBoard",
                new Point2D.Double(220, 10),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_7 = createStickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, -60),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = createStickyNote(
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

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifierStickNotesUseCase classifierStickNotesUseCase = new ClassifierStickNotesUseCase(clusteredStickyNotes);

        Group group = classifierStickNotesUseCase.getGroups().get(0);

        // UseCase => stickyNote_4
        assertEquals(stickyNote_4.getId(), group.getGroupId());
        assertEquals(stickyNote_4.getDescription(), group.getUseCaseName());

        // input => stickyNote_2
        List<String> intputDescription = Arrays.asList(stickyNote_2.getDescription().split("\\n"));
        assertEquals(intputDescription.size(), group.getInput().size());
        for (int i = 0; i < intputDescription.size(); i++) {
            assertEquals(intputDescription.get(i), group.getInput().get(i));
        }

        // aggregate name => stickyNote_1
        assertEquals(stickyNote_1.getDescription(), group.getAggregateName());

        // user name => stickyNote_3
        assertEquals(stickyNote_3.getDescription(), group.getUserName());

        // comment => stickyNote_8
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));

        // publishEvents => createStickyNote
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        PublishEvent publishEvent = group.getPublishEvents().get(0);
        assertEquals(stickyNote_5.getDescription(), publishEvent.getEventName());
        assertEquals(stickyNote_6.getDescription(), publishEvent.getNotifier());
        assertEquals(stickyNote_7.getDescription(), publishEvent.getBehavior());

    }


    private StickyNote createStickyNote(String id, String desc, Point2D pos, Point2D geo, String color) {
        return new StickyNote(id, desc, pos, geo, color);
    }

}
