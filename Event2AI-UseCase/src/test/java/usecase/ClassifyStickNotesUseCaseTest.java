package usecase;

import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import valueobject.DomainEvent;
import valueobject.Attribute;
import valueobject.UsecaseInput;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassifyStickNotesUseCaseTest {

    @Test
    public void a_complete_event_storming() {

        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, -110),
                new Point2D.Double(100, 100),
                "light_yellow");

        StickyNote stickyNote_2 = new StickyNote(
                "001B",
                "userId:String\nteamId:String",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = new StickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, 50),
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
                new Point2D.Double(220, -10),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, 60),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment",
                new Point2D.Double(100, -100),
                new Point2D.Double(200, 170),
                "gray");

        StickyNote stickyNote_9 = new StickyNote(
                "001H",
                "String userId: non-null,\n" +
                        "String teamId: non-null,\n" +
                        "String eventId: non-null",
                new Point2D.Double(220, 0),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_10 = new StickyNote(
                "001J",
                "inviteBoardMember",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 50),
                "pink");

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
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase();
        classifyStickNotesUseCase.classify(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

        // UseCase => stickyNote_4
        assertEquals(stickyNote_4.getId(), group.getGroupId());
        assertEquals(stickyNote_4.getDescription(), group.getUseCaseName());

        // input => stickyNote_2
        List<String> inputsWithType = Arrays.asList(stickyNote_2.getDescription().split("\\n"));
        List<UsecaseInput> usecaseInputs = new ArrayList<>();
        for (String inputWithType : inputsWithType) {
            List<String> nameAndType = Arrays.asList(inputWithType.split(":"));
            UsecaseInput usecaseInput = new UsecaseInput(nameAndType.get(0), nameAndType.get(1));
            usecaseInputs.add(usecaseInput);
        }
        assertTrue(isInputSame(usecaseInputs, group.getInput()));

        // aggregate name => stickyNote_1
        assertEquals(stickyNote_1.getDescription(), group.getAggregateName());

        // user name => stickyNote_3
        assertEquals(stickyNote_3.getDescription(), group.getUserName());

        // comment => stickyNote_8
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));

        // method => stickyNote_10
        assertEquals(stickyNote_1.getDescription() + " " + stickyNote_10.getDescription(), group.getMethod());

        // publishEvents => createStickyNote
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        //      evnet's attribute => stickyNote_9
        List<DomainEvent> expectedDomainEvents = new ArrayList<>();
        List<Attribute> attributes = StickyNoteToAttribute(stickyNote_9);
        expectedDomainEvents.add(new DomainEvent(stickyNote_5.getDescription(), stickyNote_6.getDescription(), stickyNote_7.getDescription(), attributes));

        assertTrue(checkPublishedEvent(expectedDomainEvents, group.getPublishEvents()));

    }

    @Test
    public void a_complete_event_storming_with_three_comment_stickyNote() {

        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, -110),
                new Point2D.Double(100, 100),
                "light_yellow");

        StickyNote stickyNote_2 = new StickyNote(
                "001B",
                "userId:String\nteamId:String",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = new StickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, 50),
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
                new Point2D.Double(220, -10),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220, 60),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment0",
                new Point2D.Double(200, -170),
                new Point2D.Double(100, 100),
                "gray");

        StickyNote stickyNote_9 = new StickyNote(
                "001I",
                "comment1",
                new Point2D.Double(300, -170),
                new Point2D.Double(100, 100),
                "gray");

        StickyNote stickyNote_10 = new StickyNote(
                "001J",
                "comment2",
                new Point2D.Double(400, -170),
                new Point2D.Double(100, 100),
                "gray");

        StickyNote stickyNote_11 = new StickyNote(
                "001K",
                "String userId: non-null,\n" +
                        "String eventId: non-null",
                new Point2D.Double(220, 0),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_12 = new StickyNote(
                "001L",
                "inviteBoardMember",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 50),
                "pink");

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

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase();
        classifyStickNotesUseCase.classify(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

        // UseCase => stickyNote_4
        assertEquals(stickyNote_4.getId(), group.getGroupId());
        assertEquals(stickyNote_4.getDescription(), group.getUseCaseName());

        // input => stickyNote_2
        assertTrue(isInputSame(
                Arrays.asList(
                        new UsecaseInput("userId", "String"),
                        new UsecaseInput("teamId", "String")
                ),
                group.getInput()
        ));

        // aggregate name => stickyNote_1
        assertEquals(stickyNote_1.getDescription(), group.getAggregateName());

        // user name => stickyNote_3
        assertEquals(stickyNote_3.getDescription(), group.getUserName());

        // comment => stickyNote_8, stickyNote_9, stickyNote_10
        assertEquals(3, group.getComment().size());
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));
        assertEquals(stickyNote_9.getDescription(), group.getComment().get(1));
        assertEquals(stickyNote_10.getDescription(), group.getComment().get(2));

        // method => stickyNote_12
        assertEquals(stickyNote_1.getDescription() + " " + stickyNote_12.getDescription(), group.getMethod());

        // publishEvents =>
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        //      attribute => stickyNote_11
        List<DomainEvent> expectedDomainEvents = new ArrayList<>();
        List<Attribute> attributes = StickyNoteToAttribute(stickyNote_11);
        expectedDomainEvents.add(new DomainEvent(stickyNote_5.getDescription(), stickyNote_6.getDescription(), stickyNote_7.getDescription(), attributes));

        assertTrue(checkPublishedEvent(expectedDomainEvents, group.getPublishEvents()));
    }
//
    @Test
    public void a_complete_event_storming_with_two_publish_event() {

        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, -110),
                new Point2D.Double(100, 100),
                "light_yellow");

        StickyNote stickyNote_2 = new StickyNote(
                "001B",
                "userId:String\nteamId:String",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = new StickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, 50),
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
                new Point2D.Double(330, -50),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(330, 50),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment0",
                new Point2D.Double(200, -170),
                new Point2D.Double(100, 100),
                "gray");

        StickyNote stickyNote_9 = new StickyNote(
                "001I",
                "event2",
                new Point2D.Double(110, 210),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_10 = new StickyNote(
                "001J",
                "reactor2",
                new Point2D.Double(330, 150),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_11 = new StickyNote(
                "001K",
                "policy2",
                new Point2D.Double(330, 250),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_12 = new StickyNote(
                "001L",
                "String attr1: non-null,\n" +
                        "int attr2: default = 0",
                new Point2D.Double(220, 0),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_13 = new StickyNote(
                "001M",
                "String attr3: non-null,\n" +
                        "int attr4: default = 0",
                new Point2D.Double(220, 210),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_14 = new StickyNote(
                "001N",
                "inviteBoardMember",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 50),
                "pink");

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
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase();
        classifyStickNotesUseCase.classify(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

        // UseCase => stickyNote_4
        assertEquals(stickyNote_4.getId(), group.getGroupId());
        assertEquals(stickyNote_4.getDescription(), group.getUseCaseName());

        // input => stickyNote_2
        assertTrue(isInputSame(
                Arrays.asList(
                        new UsecaseInput("userId", "String"),
                        new UsecaseInput("teamId", "String")
                ),
                group.getInput()
        ));

        // aggregate name => stickyNote_1
        assertEquals(stickyNote_1.getDescription(), group.getAggregateName());

        // user name => stickyNote_3
        assertEquals(stickyNote_3.getDescription(), group.getUserName());

        // comment => stickyNote_8
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));

        // method => stickyNote_14
        assertEquals(stickyNote_1.getDescription() + " " + stickyNote_14.getDescription(), group.getMethod());

        // publishEvents =>
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        //      attribute => stickyNote_12
        // publishEvents =>
        //      event name => stickyNote_9
        //      notifier => stickyNote_10
        //      behavior => stickyNote_11
        //      attribute => stickyNote_13
        List<DomainEvent> expectedDomainEvents = new ArrayList<>();
        List<Attribute> attributes1 = StickyNoteToAttribute(stickyNote_12);
        List<Attribute> attributes2 = StickyNoteToAttribute(stickyNote_13);
        expectedDomainEvents.add(new DomainEvent(stickyNote_5.getDescription(), stickyNote_6.getDescription(), stickyNote_7.getDescription(), attributes1));
        expectedDomainEvents.add(new DomainEvent(stickyNote_9.getDescription(), stickyNote_10.getDescription(), stickyNote_11.getDescription(), attributes2));

        assertTrue(checkPublishedEvent(expectedDomainEvents, group.getPublishEvents()));
    }

    // editing
    @Test
    public void a_complete_event_storming_with_three_publish_event() {

        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, -110),
                new Point2D.Double(100, 100),
                "light_yellow");

        StickyNote stickyNote_2 = new StickyNote(
                "001B",
                "userId:String\nteamId:String",
                new Point2D.Double(-110, 0),
                new Point2D.Double(100, 100),
                "green");

        StickyNote stickyNote_3 = new StickyNote(
                "001C",
                "User",
                new Point2D.Double(-50, 50),
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
                new Point2D.Double(330, -50),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",

                new Point2D.Double(220, 25),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment0",
                new Point2D.Double(200, -170),
                new Point2D.Double(100, 100),
                "gray");

        StickyNote stickyNote_9 = new StickyNote(
                "001D",
                "event2",
                new Point2D.Double(110, 210),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_10 = new StickyNote(
                "001E",
                "reactor2",
                new Point2D.Double(330, 150),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_11 = new StickyNote(
                "001F",
                "policy2",
                new Point2D.Double(330, 250),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_12 = new StickyNote(
                "001D",
                "event3",
                new Point2D.Double(110, 420),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_13 = new StickyNote(
                "001E",
                "reactor3",
                new Point2D.Double(330, 350),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_14 = new StickyNote(
                "001F",
                "policy3",
                new Point2D.Double(330, 450),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_15 = new StickyNote(
                "001G",
                "String attribute1_1: non-null, \n" +
                        "int attribute1_2: default = 1",
                new Point2D.Double(220, 0),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_16 = new StickyNote(
                "001H",
                "String attribute2_1: non-null, \n" +
                        "int attribute2_2: default = 1",
                new Point2D.Double(220, 210),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_17 = new StickyNote(
                "001I",
                "String attribute3_1: non-null, \n" +
                        "int attribute3_2: default = 1",
                new Point2D.Double(220, 420),
                new Point2D.Double(100, 100),
                "light_green");

        StickyNote stickyNote_18 = new StickyNote(
                "001J",
                "inviteBoardMember",
                new Point2D.Double(-100, 110),
                new Point2D.Double(100, 100),
                "pink");


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
        stickyNotes.add(stickyNote_15);
        stickyNotes.add(stickyNote_16);
        stickyNotes.add(stickyNote_17);
        stickyNotes.add(stickyNote_18);

        // better not use clustering
        List<List<StickyNote>> clusteredStickyNotes = new ArrayList<>();
        clusteredStickyNotes.add(stickyNotes);
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase();
        classifyStickNotesUseCase.classify(clusteredStickyNotes);

        Group group = classifyStickNotesUseCase.getGroups().get(0);

        // UseCase => stickyNote_4
        assertEquals(stickyNote_4.getId(), group.getGroupId());
        assertEquals(stickyNote_4.getDescription(), group.getUseCaseName());

        // input => stickyNote_2
        String[] inputLines = stickyNote_2.getDescription().split("\\n");
        assertEquals(inputLines.length, group.getInput().size());
        for (int i = 0; i < inputLines.length; i++) {
            String[] nameAndType = inputLines[i].split(":");
            assertEquals(nameAndType[0], group.getInput().get(i).getName());
            assertEquals(nameAndType[1], group.getInput().get(i).getType());
        }

        // aggregate name => stickyNote_1
        assertEquals(stickyNote_1.getDescription(), group.getAggregateName());

        // user name => stickyNote_3
        assertEquals(stickyNote_3.getDescription(), group.getUserName());

        // comment => stickyNote_8
        assertEquals(stickyNote_8.getDescription(), group.getComment().get(0));

        // method => stickyNote_18 => aggregateName + " " + methodName
        assertEquals(stickyNote_1.getDescription() + " " + stickyNote_18.getDescription(), group.getMethod());

        // publishEvents =>
        //      event name => stickyNote_5
        //      notifier => stickyNote_6
        //      behavior => stickyNote_7
        //      evnet's attribute => stickyNote_15

        // publishEvents =>
        //      event name => stickyNote_9
        //      notifier => stickyNote_10
        //      behavior => stickyNote_11
        //      evnet's attribute => stickyNote_16

        // publishEvents =>
        //      event name => stickyNote_12
        //      notifier => stickyNote_13
        //      behavior => stickyNote_14
        //      evnet's attribute => stickyNote_17

        List<DomainEvent> expectedDomainEvents = new ArrayList<>();
        List<Attribute> attributes1 = StickyNoteToAttribute(stickyNote_15);
        List<Attribute> attributes2 = StickyNoteToAttribute(stickyNote_16);
        List<Attribute> attributes3 = StickyNoteToAttribute(stickyNote_17);
        expectedDomainEvents.add(new DomainEvent(stickyNote_5.getDescription(), stickyNote_6.getDescription(), stickyNote_7.getDescription(), attributes1));
        expectedDomainEvents.add(new DomainEvent(stickyNote_9.getDescription(), stickyNote_10.getDescription(), stickyNote_11.getDescription(), attributes2));
        expectedDomainEvents.add(new DomainEvent(stickyNote_12.getDescription(), stickyNote_13.getDescription(), stickyNote_14.getDescription(), attributes3));

        assertTrue(checkPublishedEvent(expectedDomainEvents, group.getPublishEvents()));
    }

    private Boolean checkPublishedEvent(List<DomainEvent> expectedEvents, List<DomainEvent> actualEvents) {
        // if size is different would return false
        if(actualEvents.size() != expectedEvents.size()) {
            return false;
        }

        // use an array to present the boolean about every event
        boolean[] isPublishEventMatch = new boolean[expectedEvents.size()];
        Arrays.fill(isPublishEventMatch, false);

        for(int i = 0; i < expectedEvents.size(); i++) {
            for (DomainEvent actualEvent : actualEvents) {
                // check for attributes
                boolean attributeIsSame = isAttributesSame(expectedEvents.get(i).getAttributes(), actualEvent.getAttributes());

                // check for eventName, reactor, policy
                if (Objects.equals(expectedEvents.get(i).getEventName(), actualEvent.getEventName()) &&
                        checkNotifierOrBehavior(expectedEvents.get(i).getReactor(), actualEvent.getReactor()) &&
                        checkNotifierOrBehavior(expectedEvents.get(i).getPolicy(), actualEvent.getPolicy()) &&
                        attributeIsSame
                ) {
                    isPublishEventMatch[i] = true;
                    break;
                }
            }
            if(!isPublishEventMatch[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNotifierOrBehavior(String expected, String actual) {
        if (Objects.equals(expected, actual)) {
            return true;
        } else {
            return Objects.equals(expected, "") &&
                    Objects.equals(actual, "(no statement)");
        }
    }

    private boolean isInputSame(List<UsecaseInput> expected, List<UsecaseInput> actual) {
        if(expected.size() != actual.size()) return false;
        for(int i = 0; i < expected.size(); i++) {
            if(!expected.get(i).getName().equals(actual.get(i).getName()) ||
                    !expected.get(i).getType().equals(actual.get(i).getType())
            ) {
                return false;
            }
        }
        return true;
    }

    private boolean isAttributesSame(List<Attribute> expected, List<Attribute> actual) {
        // if size different would return false
        if(expected.size() != actual.size()) {
            return false;
        }

        //
        for(int i = 0; i < expected.size(); i++) {
            if(!expected.get(i).getName().equals(actual.get(i).getName()) ||
                    !expected.get(i).getType().equals(actual.get(i).getType()) ||
                    !expected.get(i).getConstraint().equals(actual.get(i).getConstraint())){
                return false;
            }
        }
        return true;
    }

    private List<Attribute> StickyNoteToAttribute(StickyNote attribute) {
        List<Attribute> result = new ArrayList<>();
        String description = attribute.getDescription().replace("<!-- -->", "");

        String[] lines = description.replace("\n", "<br />").split(",<br />");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            System.out.println("line:" + line);

            // type1 name1: constrain1
            String[] typeWithVarNameAndConstraint = line.split(":");
            String typeWithVarName = typeWithVarNameAndConstraint[0].trim();     // type1 name1
            String constraint = typeWithVarNameAndConstraint[1].trim();

            String[] typeName = typeWithVarName.split("\\s+");
            String type = typeName[0];
            String name = typeName[1];

            result.add(new Attribute(name, type, constraint));
        }
        return result;
    }
}
