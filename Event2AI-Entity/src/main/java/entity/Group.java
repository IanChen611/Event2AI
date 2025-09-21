package entity;

import valueobject.PublishEvent;

import java.util.ArrayList;
import java.util.List;

public class Group {
    // groupId is the Id of UseCase
    private String groupId;
    private String useCaseName;
    private List<String> input;
    private String aggregateName;
    private String userName;
    private List<String> comment;
    private List<PublishEvent> publishEvents;

    public Group(){

    }

    public String getUseCaseName() {
        return useCaseName;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<String> getInput() {
        return input;
    }

    public String getAggregateName() {
        return aggregateName;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getComment() {
        return comment;
    }

    public List<PublishEvent> getPublishEvents() {
        return publishEvents;
    }

    public void setUseCaseName(String useCaseName) {
        this.useCaseName = useCaseName;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAggregateName(String aggregateName) {
        this.aggregateName = aggregateName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPublishEvents(List<PublishEvent> publishEvents) {
        this.publishEvents = publishEvents;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }

}
