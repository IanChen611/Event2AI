package usecase;

import entity.Group;
import valueobject.PublishEvent;

import java.util.ArrayList;

public class GroupToJsonDto {
    private String usecase;
    private ArrayList<String> input;
    private String aggregate;
    private String user;
    private ArrayList<PublishEvent> events;

    public GroupToJsonDto(Group group) {
        this.usecase = group.getUseCaseName();
        this.input = new ArrayList<>(group.getInput());
        this.aggregate = group.getAggregateName();
        this.user = group.getUserName();
        this.events = new ArrayList<>(group.getPublishEvents());
    }
}
