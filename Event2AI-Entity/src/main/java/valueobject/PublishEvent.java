package valueobject;

import java.util.Objects;

public class PublishEvent {
    private String eventName;
    private String notifier;
    private String behavior;
    public PublishEvent(String eventName, String notifier, String Behavior) {
        if (Objects.equals(notifier, "")){
            this.notifier = "(no statement)";
        }
        if (Objects.equals(Behavior, "")){
            this.behavior = "(no statement)";
        }
        this.eventName = eventName;
        this.notifier = notifier;
        this.behavior = Behavior;
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

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
