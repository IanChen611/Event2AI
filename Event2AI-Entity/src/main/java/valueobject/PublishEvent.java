package valueobject;

import java.util.Objects;

public class PublishEvent {
    private String eventName;
    private String notifier;
    private String Behavior;
    public PublishEvent(String eventName, String notifier, String Behavior) {
        if (Objects.equals(notifier, "")){
            this.notifier = "(no statement)";
        }
        if (Objects.equals(Behavior, "")){
            this.Behavior = "(no statement)";
        }
        this.eventName = eventName;
        this.notifier = notifier;
        this.Behavior = Behavior;
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
        return Behavior;
    }

    public void setBehavior(String behavior) {
        Behavior = behavior;
    }
}
