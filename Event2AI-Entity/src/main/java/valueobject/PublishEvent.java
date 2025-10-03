package valueobject;

import java.util.Objects;

public class PublishEvent {
    private String eventName;
    private String notifier;
    private String policy;
    public PublishEvent(String eventName, String notifier, String policy) {
        if (Objects.equals(notifier, "")){
            this.notifier = "(no statement)";
        }
        if (Objects.equals(policy, "")){
            this.policy = "(no statement)";
        }
        this.eventName = eventName;
        this.notifier = notifier;
        this.policy = policy;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
