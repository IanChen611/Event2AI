package adapter;

import com.google.gson.JsonArray;
import entity.Group;
import entity.StickyNote;
import usecase.GroupStickyNotesUseCase;
import usecase.GroupToJsonDto;
import usecase.JsonToStickyNote;
import usecase.SortGroupByAggregateUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickyNoteToGroupSortedByAggregateProcessor {
    private List<StickyNote> stickyNotes;
    private List<Group> groups;
    private Map<String, List<Group>> groupsSortedByAggregate;
    private Map<String, List<GroupToJsonDto>> groupSortedByAggregateToJsonDtos;

    public StickyNoteToGroupSortedByAggregateProcessor(){}

    public void process(String filePath) {
        this.stickyNotes = processFile(filePath);
        this.groups = processStickyNotesToGroup(this.stickyNotes);
        this.groupsSortedByAggregate = processGroupsToSortedGroup(this.groups);
        this.groupSortedByAggregateToJsonDtos = processSortedGroupToJsonDto(this.groupsSortedByAggregate);
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

    private Map<String, List<Group>> processGroupsToSortedGroup(List<Group> groups){
        SortGroupByAggregateUseCase sortGroupByAggregateUseCase = new SortGroupByAggregateUseCase();
        sortGroupByAggregateUseCase.sort(groups);
        return sortGroupByAggregateUseCase.getSortedGroups();

    }

    private Map<String, List<GroupToJsonDto>> processSortedGroupToJsonDto(Map<String, List<Group>> sortedGroups){
        Map<String, List<GroupToJsonDto>> result = new HashMap<>();

        for (Map.Entry<String, List<Group>> entry : sortedGroups.entrySet()) {
            String key = entry.getKey();
            List<Group> groups = entry.getValue();

            List<GroupToJsonDto> dtoList = processGroupToJsonDto(groups);
            result.put(key, dtoList);
        }

        return result;
    }

    private List<GroupToJsonDto> processGroupToJsonDto(List<Group> groups){
        List<GroupToJsonDto> groupToJsonDtos = new ArrayList<>();
        for(Group group : groups){
            GroupToJsonDto groupToJsonDto = new GroupToJsonDto(group);
            groupToJsonDtos.add(groupToJsonDto);
        }
        return groupToJsonDtos;
    }

    public Map<String, List<GroupToJsonDto>> getGroupSortedByAggregateToJsonDtos(){
        return groupSortedByAggregateToJsonDtos;
    }
}
