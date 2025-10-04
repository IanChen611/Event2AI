package usecase;

import entity.Group;
import entity.StickyNote;
import valueobject.PublishEvent;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class ClassifyStickNotesUseCase {
    private final List<Group> groups;
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
                    if(stickyNote.getColor().equals("light_yellow")){
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
                    if(stickyNote.getColor().equals("gray")){
                        result.add(stickyNote);
                    }
                }
                break;
            case "publish_event":
                for(StickyNote stickyNote : stickyNotes){
                    if(stickyNote.getColor().equals("orange") ||
                            stickyNote.getColor().equals("light_blue") ||
                            stickyNote.getColor().equals("violet")){
                        result.add(stickyNote);
                    }
                }
                break;
        }
        return result;
    }

    private List<PublishEvent> StickyNoteToPublishEvent(List<StickyNote> stickyNotes){
        List<StickyNote> eventNames = new ArrayList<>();
        List<StickyNote> reactors = new ArrayList<>();
        List<StickyNote> policies = new ArrayList<>();
        List<PublishEvent> result = new ArrayList<>();

        for(StickyNote  stickyNote : stickyNotes){
            switch (stickyNote.getColor()) {
                case "orange":
                    eventNames.add(stickyNote);
                    break;
                case "light_blue":
                    reactors.add(stickyNote);
                    break;
                case "violet":
                    policies.add(stickyNote);
                    break;
            }
        }

        class RelativeData {
            public final String reactorDescription;
            public final String policyDescription;
            RelativeData(String reactorDescription, String policyDescription){
                this.reactorDescription = reactorDescription;
                this.policyDescription = policyDescription;
            }
        }

//        List<Map<String, List<RelativeData>>> eventMaps = new ArrayList<>();

        for (int i = 0;i < eventNames.size();i++){
            StickyNote eventName = eventNames.get(i);
            double multiple_Y = 0.7;
            double multiple_X = 0.5;
            List<StickyNote> thisEventsReactors = new ArrayList<>();
            List<StickyNote> thisEventsPolicies = new ArrayList<>();
            // ---------------------------------
            // Take out the reactors and policies that belong to this eventName and then
            // put them separately into thisEventsReactors and thisEventsPolicies
            for(StickyNote reactor : reactors){
                double threshold = max(max(eventName.getGeo().getX(), eventName.getGeo().getY()), max(reactor.getGeo().getX(), reactor.getGeo().getY()));
                double dy = abs(reactor.getPos().getY() - eventName.getPos().getY());
                // dy < 0.7 * geo.y    and     reactor.y <= eventName.y
                if(dy / threshold <= multiple_Y &&
                        reactor.getPos().getY() <= eventName.getPos().getY()){
                    thisEventsReactors.add(reactor);
                }
            }
            for(StickyNote policy : policies) {
                double threshold = max(max(eventName.getGeo().getX(), eventName.getGeo().getY()), max(policy.getGeo().getX(), policy.getGeo().getY()));
                double dy = abs(policy.getPos().getY() - eventName.getPos().getY());
                // distance < 0.7 * geo.y    and     policy.y >= eventName.y
                if (dy / threshold <= multiple_Y &&
                        policy.getPos().getY() >= eventName.getPos().getY()) {
                    thisEventsPolicies.add(policy);
                }
            }
            // ---------------------------------
            // Package the extracted reactors and policies that belong to this Event into relativeData, then add it to relativeDatas
            for(StickyNote reactor : thisEventsReactors){
                for(StickyNote policy : thisEventsPolicies){
                    double threshold = max(max(reactor.getGeo().getX(), reactor.getGeo().getY()), max(policy.getGeo().getX(), policy.getGeo().getY()));
                    double dx =  abs(policy.getPos().getX() - reactor.getPos().getX());
                    if(dx <= (multiple_X * threshold)){
                        PublishEvent publishEvent = new PublishEvent(eventName.getDescription(), reactor.getDescription(), policy.getDescription());
                        result.add(publishEvent);
                        break;
                    }
                }
            }
            // ---------------------------------
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
