package usecase;

import entity.Group;
import valueobject.PublishEvent;
import valueobject.UsecaseInput;

import java.util.List;

public class GroupToJsonDto {
    private String usecase;
    private List<UsecaseInput> input;
    private String aggregate;
    private String aggregateId;
    private String repository;
    private String output;
    private String actor;
    private List<PublishEvent> events;
    private List<String> domainModelNotes;

    public GroupToJsonDto(Group group) {
        this.usecase = group.getUseCaseName();
        this.input = group.getInput();
        this.aggregate = group.getAggregateName();
        this.aggregateId = this.aggregate + "Id";
        this.repository = this.aggregate + "Repository";
        this.output = "CqrsOutput with " + this.aggregateId;
        this.actor = group.getUserName();
        this.events = group.getPublishEvents();
        this.domainModelNotes = group.getComment();
    }

    public String getUsecaseName() {
        return usecase;
    }
}
