package usecase;

import entity.Group;
import valueobject.PublishEvent;

import java.util.List;

public class GroupToJsonDto {
    private String usecase;
    private List<String> input;
    private String aggregate;
    private String user;
    private List<PublishEvent> events;
    private List<String> comments;

    public GroupToJsonDto(Group group) {
        this.usecase = group.getUseCaseName();
        this.input = group.getInput();
        this.aggregate = group.getAggregateName();
        this.user = group.getUserName();
        this.events = group.getPublishEvents();
        this.comments = group.getComment();
    }

    public String getUsecaseName() {
        return usecase;
    }
}
