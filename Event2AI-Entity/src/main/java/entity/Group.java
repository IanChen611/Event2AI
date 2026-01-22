package entity;

import valueobject.AggregateWithAttribute;
import valueobject.DomainEvent;
import valueobject.UsecaseInput;

import java.util.List;

public class Group {
    // groupId is the Id of UseCase
    private String groupId;
    private String useCaseName;
    private List<UsecaseInput> input;
    private String aggregateName;
    private String userName;
    private List<String> comment;
    private List<DomainEvent> domainEvents;
    private List<AggregateWithAttribute>  aggregateWithAttributes;
    private String method;

    public Group(){

    }

    public String getUseCaseName() {
        return useCaseName;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<UsecaseInput> getInput() {
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

    public List<DomainEvent> getPublishEvents() {
        return domainEvents;
    }

    public List<AggregateWithAttribute> getAggregateWithAttributes() {
        return aggregateWithAttributes;
    }

    public String getMethod() {return method;}

    public void setUseCaseName(String useCaseName) {
        this.useCaseName = useCaseName;
    }

    public void setInput(List<UsecaseInput> input) {
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

    public void setDomainEvents(List<DomainEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }

    public void setAggregateWithAttributes(List<AggregateWithAttribute> aggregateWithAttributes) {
        this.aggregateWithAttributes = aggregateWithAttributes;
    }

    public void setMethod(String method) { this.method = method; }
}
