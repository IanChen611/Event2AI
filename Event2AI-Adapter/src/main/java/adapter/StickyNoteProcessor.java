package adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Group;
import entity.StickyNote;
import usecase.GroupStickyNotesUseCase;
import usecase.GroupToJsonDto;
import usecase.JsonToStickyNote;

import java.util.ArrayList;
import java.util.List;

public class StickyNoteProcessor {
    private List<StickyNote> stickyNotes;
    private List<Group> groups;
    private List<GroupToJsonDto> groupToJsonDtos;

    public StickyNoteProcessor(){}

    public void process(String filePath) {
        this.stickyNotes = processFile(filePath);
        this.groups = processStickyNotesToGroup(this.stickyNotes);
        this.groupToJsonDtos = processGroupToJsonDto(this.groups);
    }

    private List<StickyNote> processFile(String filePath){
        JsonReader jsonReader = new JsonReader();
        JsonArray jsonItems = jsonReader.readBoardItem(filePath).get("stickyNotes").getAsJsonArray();
        List<StickyNote> items = JsonToStickyNote.convert(jsonItems);

        return items;
    }

    private List<Group> processStickyNotesToGroup(List<StickyNote> stickyNotes){
        GroupStickyNotesUseCase groupStickyNotesUseCase = new GroupStickyNotesUseCase();
        groupStickyNotesUseCase.group(stickyNotes);
        return groupStickyNotesUseCase.getGroups();
    }

    private List<GroupToJsonDto> processGroupToJsonDto(List<Group> groups){
        List<GroupToJsonDto> groupToJsonDtos = new ArrayList<>();
        for(Group group : groups){
            GroupToJsonDto groupToJsonDto = new GroupToJsonDto(group);
            groupToJsonDtos.add(groupToJsonDto);
        }
        return groupToJsonDtos;
    }

    public List<GroupToJsonDto> getGroupToJsonDtos(){
        return groupToJsonDtos;
    }
}
