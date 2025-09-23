package usecase;

import entity.Group;
import entity.StickyNote;

import java.util.List;

public class GroupStickyNotesUseCase {
    private List<Group> groups;
    private List<StickyNote> stickyNotes;
    private List<List<StickyNote>> clusteredStickyNotes;

    GroupStickyNotesUseCase(List<StickyNote> stickyNotes) {
        this.stickyNotes = stickyNotes;
        ClusterStickyNotesUseCase clusterStickyNotesUseCase = new ClusterStickyNotesUseCase(stickyNotes);
        this.clusteredStickyNotes = clusterStickyNotesUseCase.getAllGroup();
        ClassifyStickNotesUseCase classifyStickNotesUseCase = new ClassifyStickNotesUseCase(this.clusteredStickyNotes);
        this.groups = classifyStickNotesUseCase.getGroups();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<StickyNote> getStickyNotes() {
        return stickyNotes;
    }

    public void setStickyNotes(List<StickyNote> stickyNotes) {
        this.stickyNotes = stickyNotes;
    }

    public List<List<StickyNote>> getClusteredStickyNotes() {
        return clusteredStickyNotes;
    }

    public void setClusteredStickyNotes(List<List<StickyNote>> clusteredStickyNotes) {
        this.clusteredStickyNotes = clusteredStickyNotes;
    }
}
