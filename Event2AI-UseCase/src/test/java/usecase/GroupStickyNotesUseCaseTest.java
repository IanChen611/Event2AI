package usecase;

import common.TestTool;
import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import valueobject.PublishEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupStickyNotesUseCaseTest {
    @Test
    public void a_complete_event_storming() {
        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                "light_yellow");

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
                new Point2D.Double(220, -10),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "Add the member to the board in board bounded context",
                new Point2D.Double(220,  60),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment",
                new Point2D.Double(100, 100),
                new Point2D.Double(200, 170),
                "gray");

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

        List<Group> expectGroups = new ArrayList<>(List.of(group));

        GroupStickyNotesUseCase groupStickyNotesUseCase = new GroupStickyNotesUseCase();
        groupStickyNotesUseCase.group(stickyNotes);

        List<Group> actualGroups = groupStickyNotesUseCase.getGroups();
        assertTrue(checkGroup(expectGroups.get(0), actualGroups.get(0)));
    }

    @Test
    public void a_complete_event_storming_with_two_events_that_each_has_two_events() {
        StickyNote stickyNote_1 = new StickyNote(
                "001A",
                "Team",
                new Point2D.Double(0, 110),
                new Point2D.Double(100, 100),
                "light_yellow");

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
                "event1",
                new Point2D.Double(110, 0),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_6 = new StickyNote(
                "001F",
                "reactor1-1",
                new Point2D.Double(220, -30),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_7 = new StickyNote(
                "001G",
                "policy1-1",
                new Point2D.Double(220,  55),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_8 = new StickyNote(
                "001H",
                "comment",
                new Point2D.Double(100, 100),
                new Point2D.Double(200, 170),
                "gray");

        StickyNote stickyNote_9 = new StickyNote(
                "001F",
                "reactor1-2",
                new Point2D.Double(330, -30),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_10 = new StickyNote(
                "001G",
                "policy1-2",
                new Point2D.Double(330,  55),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_11 = new StickyNote(
                "001E",
                "event2",
                new Point2D.Double(110, 130),
                new Point2D.Double(100, 100),
                "orange");

        StickyNote stickyNote_12 = new StickyNote(
                "001F",
                "reactor2-1",
                new Point2D.Double(220, 95),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_13 = new StickyNote(
                "001G",
                "policy2-1",
                new Point2D.Double(220,  180),
                new Point2D.Double(100, 100),
                "violet");

        StickyNote stickyNote_14 = new StickyNote(
                "001F",
                "reactor2-2",
                new Point2D.Double(330, 95),
                new Point2D.Double(100, 50),
                "light_blue");

        StickyNote stickyNote_15 = new StickyNote(
                "001G",
                "policy2-2",
                new Point2D.Double(330,  180),
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
        stickyNotes.add(stickyNote_15);

        Group group = new Group();
        group.setGroupId(stickyNote_4.getId());
        group.setUseCaseName(stickyNote_4.getDescription());
        group.setInput(Arrays.asList(stickyNote_2.getDescription().split("\\n")));
        group.setAggregateName(stickyNote_1.getDescription());
        group.setUserName(stickyNote_3.getDescription());
        group.setComment(List.of(stickyNote_8.getDescription()));
        List<PublishEvent> publishEvents = new ArrayList<>();
        // event1 => stickyNote_5
        // reactor1-1、1-2 => stickyNote_6、stickyNote_9
        // policy1-1、1-2 => stickyNote_7、stickyNote_10
        publishEvents.add(new PublishEvent(stickyNote_5.getDescription(), stickyNote_6.getDescription(), stickyNote_7.getDescription()));
        publishEvents.add(new PublishEvent(stickyNote_5.getDescription(), stickyNote_9.getDescription(), stickyNote_10.getDescription()));

        // event2 => stickyNote_11
        // reactor2-1、2-2 => stickyNote_12、stickyNote_14
        // policy2-1、2-2 => stickyNote_13、stickyNote_15
        publishEvents.add(new PublishEvent(stickyNote_11.getDescription(), stickyNote_12.getDescription(), stickyNote_13.getDescription()));
        publishEvents.add(new PublishEvent(stickyNote_11.getDescription(), stickyNote_14.getDescription(), stickyNote_15.getDescription()));

        group.setPublishEvents(publishEvents);

        List<Group> expectGroups = new ArrayList<>(List.of(group));

        GroupStickyNotesUseCase groupStickyNotesUseCase = new GroupStickyNotesUseCase();
        groupStickyNotesUseCase.group(stickyNotes);
        List<Group> actualGroups = groupStickyNotesUseCase.getGroups();

        assertTrue(checkGroup(expectGroups.get(0), actualGroups.get(0)));
    }

    private Boolean checkGroup(Group expectedGroup, Group actualGroup) {
        if (!Objects.equals(expectedGroup.getGroupId(), actualGroup.getGroupId())) {
            return false;
        }
        if (!Objects.equals(expectedGroup.getUseCaseName(), actualGroup.getUseCaseName())) {
            return false;
        }
        if (!TestTool.checkListOfObject(expectedGroup.getInput(), actualGroup.getInput())) {
            return false;
        }
        if (!Objects.equals(expectedGroup.getAggregateName(), actualGroup.getAggregateName())) {
            return false;
        }
        if (!Objects.equals(expectedGroup.getUserName(), actualGroup.getUserName())) {
            return false;
        }
        if (!TestTool.checkListOfObject(expectedGroup.getComment(), actualGroup.getComment())) {
            return false;
        }
        return checkPublishEvent(expectedGroup.getPublishEvents(), actualGroup.getPublishEvents());
    }

    private Boolean checkPublishEvent(List<PublishEvent> expectedEvents, List<PublishEvent> actualEvents) {
        if(actualEvents.size() != expectedEvents.size()) {
            return false;
        }
        boolean[] isPublishEventMatch = new boolean[expectedEvents.size()];
        Arrays.fill(isPublishEventMatch, false);
        for(int i = 0; i < expectedEvents.size(); i++) {
            for (PublishEvent actualEvent : actualEvents) {
                if (Objects.equals(expectedEvents.get(i).getEventName(), actualEvent.getEventName()) &&
                checkNotifierOrBehavior(expectedEvents.get(i).getReactor(), actualEvent.getReactor()) &&
                checkNotifierOrBehavior(expectedEvents.get(i).getPolicy(), actualEvent.getPolicy())) {
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

    private Boolean checkNotifierOrBehavior(String expected, String actual) {
        if (Objects.equals(expected, actual)) {
            return true;
        } else {
            return Objects.equals(expected, "") &&
                    Objects.equals(actual, "(no statement)");
        }
    }
}
