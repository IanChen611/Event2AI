package valueobject;

import java.util.List;
import java.util.Objects;

public class PublishEvent {
    private String eventName;
    private String reactor;
    private String policy;
    public PublishEvent(String eventName, String reactor, String policy) {
        this.eventName = eventName;
        this.reactor = reactor;
        this.policy = policy;
        if (Objects.equals(this.reactor, "")){
            this.reactor = "(no statement)";
        }
        if (Objects.equals(this.policy, "")){
            this.policy = "(no statement)";
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getReactor() {
        return reactor;
    }

    public void setReactor(String reactor) {this.reactor = reactor;}

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
