package usecase;

import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import valueobject.PublishEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassifyStickNotesUseCaseTest {

    @Test
    public void a_complete_event_storming() {

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

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

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

    @Test
    public void a_complete_event_storming_with_three_comment_stickyNote() {

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
                "comment0",
                new Point2D.Double(200, 170),
                new Point2D.Double(100, 100),
                "white");

        StickyNote stickyNote_9 = new StickyNote(
                "001I",
                "comment1",
                new Point2D.Double(300, 170),
                new Point2D.Double(100, 100),
                "white");

        StickyNote stickyNote_10 = new StickyNote(
                "001I",
                "comment2",
                new Point2D.Double(400, 170),
                new Point2D.Double(100, 100),
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
        stickyNotes.add(stickyNote_9);
        stickyNotes.add(stickyNote_10);

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

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

        // comment => stickyNote_8, stickyNote_9, stickyNote_10
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));
        assertEquals(stickyNote_9.getDescription(), group.getComment().get(1));
        assertEquals(stickyNote_10.getDescription(), group.getComment().get(2));

        // publishEvents => createStickyNote
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        PublishEvent publishEvent = group.getPublishEvents().get(0);
        assertEquals(stickyNote_5.getDescription(), publishEvent.getEventName());
        assertEquals(stickyNote_6.getDescription(), publishEvent.getNotifier());
        assertEquals(stickyNote_7.getDescription(), publishEvent.getBehavior());

    }

    @Test
    public void a_complete_event_storming_with_two_publish_event() {

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
                new Point2D.Double(220, 60),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, -25),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment0",
                new Point2D.Double(200, 170),
                new Point2D.Double(100, 100),
                "white");

        StickyNote stickyNote_9 = new StickyNote(
                "001D",
                "event2",
                new Point2D.Double(110, -150),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_10 = new StickyNote(
                "001E",
                "notifier2",
                new Point2D.Double(220, -90),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_11 = new StickyNote(
                "001F",
                "behavior2",
                new Point2D.Double(220, -175),
                new Point2D.Double(100, 100),
                "violet");


        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);
        stickyNotes.add(stickyNote_5);
        stickyNotes.add(stickyNote_6);
        stickyNotes.add(stickyNote_7);
        stickyNotes.add(stickyNote_8);
        stickyNotes.add(stickyNote_9);
        stickyNotes.add(stickyNote_10);
        stickyNotes.add(stickyNote_11);

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

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

        // publishEvents =>
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        PublishEvent publishEvent1 = group.getPublishEvents().get(0);
        assertEquals(stickyNote_5.getDescription(), publishEvent1.getEventName());
        assertEquals(stickyNote_6.getDescription(), publishEvent1.getNotifier());
        assertEquals(stickyNote_7.getDescription(), publishEvent1.getBehavior());
        // publishEvents =>
        //      event name => stickyNote_9
        //      notifier => stickyNote_10
        //      behavior => stickyNote_11
        PublishEvent publishEvent2 = group.getPublishEvents().get(1);
        assertEquals(stickyNote_9.getDescription(), publishEvent2.getEventName());
        assertEquals(stickyNote_10.getDescription(), publishEvent2.getNotifier());
        assertEquals(stickyNote_11.getDescription(), publishEvent2.getBehavior());
    }

    @Test
    public void a_complete_event_storming_with_three_publish_event() {

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
                new Point2D.Double(220, 60),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",

                new Point2D.Double(220, -25),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment0",
                new Point2D.Double(200, 170),
                new Point2D.Double(100, 100),
                "white");

        StickyNote stickyNote_9 = new StickyNote(
                "001D",
                "event2",
                new Point2D.Double(110, -150),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_10 = new StickyNote(
                "001E",
                "notifier2",
                new Point2D.Double(220, -90),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_11 = new StickyNote(
                "001F",
                "behavior2",
                new Point2D.Double(220, -175),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_12 = new StickyNote(
                "001D",
                "event3",
                new Point2D.Double(110, -300),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_13 = new StickyNote(
                "001E",
                "notifier3",
                new Point2D.Double(220, -240),
                new Point2D.Double(100, 50),
                "light blue");

        StickyNote stickyNote_14 = new StickyNote(
                "001F",
                "behavior3",
                new Point2D.Double(220, -325),
                new Point2D.Double(100, 100),
                "violet");


        List<StickyNote> stickyNotes = new ArrayList<>();
        stickyNotes.add(stickyNote_1);
        stickyNotes.add(stickyNote_2);
        stickyNotes.add(stickyNote_3);
        stickyNotes.add(stickyNote_4);
        stickyNotes.add(stickyNote_5);
        stickyNotes.add(stickyNote_6);
        stickyNotes.add(stickyNote_7);
        stickyNotes.add(stickyNote_8);
        stickyNotes.add(stickyNote_9);
        stickyNotes.add(stickyNote_10);
        stickyNotes.add(stickyNote_11);
        stickyNotes.add(stickyNote_12);
        stickyNotes.add(stickyNote_13);
        stickyNotes.add(stickyNote_14);

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

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

        // publishEvents =>
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        PublishEvent publishEvent1 = group.getPublishEvents().get(0);
        assertEquals(stickyNote_5.getDescription(), publishEvent1.getEventName());
        assertEquals(stickyNote_6.getDescription(), publishEvent1.getNotifier());
        assertEquals(stickyNote_7.getDescription(), publishEvent1.getBehavior());
        // publishEvents =>
        //      event name => stickyNote_9
        //      notifier => stickyNote_10
        //      behavior => stickyNote_11
        PublishEvent publishEvent2 = group.getPublishEvents().get(1);
        assertEquals(stickyNote_9.getDescription(), publishEvent2.getEventName());
        assertEquals(stickyNote_10.getDescription(), publishEvent2.getNotifier());
        assertEquals(stickyNote_11.getDescription(), publishEvent2.getBehavior());

        // publishEvents =>
        //      event name => stickyNote_12
        //      notifier => stickyNote_13
        //      behavior => stickyNote_14
        PublishEvent publishEvent3 = group.getPublishEvents().get(2);
        assertEquals(stickyNote_12.getDescription(), publishEvent3.getEventName());
        assertEquals(stickyNote_13.getDescription(), publishEvent3.getNotifier());
        assertEquals(stickyNote_14.getDescription(), publishEvent3.getBehavior());
    }

}
