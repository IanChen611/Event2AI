package usecase;

import entity.Group;
import entity.StickyNote;
import valueobject.PublishEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class ClassifyStickNotesUseCase {
    private List<Group> groups;
    private final List<List<StickyNote>> clusteredStickyNotes;

    public ClassifyStickNotesUseCase(List<List<StickyNote>> clusteredStickyNotes){
        this.clusteredStickyNotes = clusteredStickyNotes;
        this.groups = new ArrayList<>();
        for(List<StickyNote> stickyNotes : this.clusteredStickyNotes){
            this.groups.add(classifyGroup(stickyNotes));
        }
    }

    private Group classifyGroup(List<StickyNote> stickyNotes){
        Group group = new Group();

        // Process UseCase
        StickyNote useCase_stickyNote = findByType("use_case", stickyNotes).get(0);
        group.setGroupId(useCase_stickyNote.getId());
        group.setUseCaseName(useCase_stickyNote.getDescription());

        // Process input
        StickyNote input_stickyNote = findByType("input", stickyNotes).get(0);
        List<String> input = Arrays.asList(input_stickyNote.getDescription().split("\\n"));
        group.setInput(input);

        // Process aggregate name
        StickyNote aggregateName_stickyNote = findByType("aggregate_name", stickyNotes).get(0);
        group.setAggregateName(aggregateName_stickyNote.getDescription());

        // Process user's name
        StickyNote userName_stickyNote = findByType("user_name", stickyNotes).get(0);
        group.setUserName(userName_stickyNote.getDescription());

        // Process comments
        List<StickyNote> comment_stickyNotes = findByType("comment", stickyNotes);
        List<String> commentDescriptions = comment_stickyNotes.stream()
                .map(StickyNote::getDescription) // every integer change to String
                .collect(Collectors.toList());
        group.setComment(commentDescriptions);

        // Process "Publish Events" =>  Event Name + Notifier + Behavior
        List<StickyNote> aboutPublishEventStickyNotes = findByType("publish_event", stickyNotes);
        List<PublishEvent> publishEvents = StickyNoteToPublishEvent(aboutPublishEventStickyNotes);
        group.setPublishEvents(publishEvents);

        return group;
    }

    private List<StickyNote> findByType(String type, List<StickyNote> stickyNotes){
        List<StickyNote> result = new ArrayList<>();
        switch(type){
            case "use_case":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("blue")){
                        result.add(stickyNote);
                        break;
                    }
                }
                break;
            case "input":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("green")){
                        result.add(stickyNote);
                        break;
                    }
                }
                break;
            case "aggregate_name":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("light yellow")){
                        result.add(stickyNote);
                        break;
                    }
                }
                break;
            case "user_name":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("yellow")){
                        result.add(stickyNote);
                        break;
                    }
                }
                break;
            case "comment":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("white")){
                        result.add(stickyNote);
                    }
                }
                break;
            case "publish_event":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("orange") ||
                            stickyNote.getColor().equals("light blue") ||
                            stickyNote.getColor().equals("violet")){
                        result.add(stickyNote);
                    }
                }
                break;
        }
        return result;
    }

    private List<PublishEvent> StickyNoteToPublishEvent(List<StickyNote> stickyNotes){
        List<PublishEvent> result = new ArrayList<>();

        List<StickyNote> eventNames = new ArrayList<>();
        List<StickyNote> notifiers = new ArrayList<>();
        List<StickyNote> behaviors = new ArrayList<>();

        for(StickyNote  stickyNote : stickyNotes){
            switch (stickyNote.getColor()) {
                case "orange":
                    eventNames.add(stickyNote);
                    break;
                case "light blue":
                    notifiers.add(stickyNote);
                    break;
                case "violet":
                    behaviors.add(stickyNote);
                    break;
            }
        }

        for (StickyNote eventName : eventNames){
            String notifierDesc = "";
            String behaviorDesc = "";
            double mutiple = 1.3;

            for(StickyNote notifier : notifiers){
                double threshold = max(max(eventName.getGeo().getX(), eventName.getGeo().getY()), max(notifier.getGeo().getX(), notifier.getGeo().getY()));
                double dx = abs(notifier.getPos().getX() - eventName.getPos().getX());
                double dy = abs(notifier.getPos().getY() - eventName.getPos().getY());
                double dist = Math.sqrt(dx * dx + dy * dy);
                // distance < 1.4 geo    and     notifier.y >= eventName.y
                if(dist / threshold <= mutiple && notifier.getPos().getY() >= eventName.getPos().getY()){
                    notifierDesc =  notifier.getDescription();
                    break;
                }
            }
            for(StickyNote behavior : behaviors) {
                double threshold = max(max(eventName.getGeo().getX(), eventName.getGeo().getY()), max(behavior.getGeo().getX(), behavior.getGeo().getY()));
                double dx = abs(behavior.getPos().getX() - eventName.getPos().getX());
                double dy = abs(behavior.getPos().getY() - eventName.getPos().getY());
                double dist = Math.sqrt(dx * dx + dy * dy);
                // distance < 1.4 geo    and     behavior.y <= eventName.y
                if (dist / threshold <= mutiple && behavior.getPos().getY() <= eventName.getPos().getY()) {
                    behaviorDesc = behavior.getDescription();
                    break;
                }
            }
            PublishEvent publishEvent = new PublishEvent(eventName.getDescription(), notifierDesc, behaviorDesc);
            result.add(publishEvent);
        }
        return result;
    }

    public List<List<StickyNote>> getClusteredStickyNotes() {
        return clusteredStickyNotes;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
