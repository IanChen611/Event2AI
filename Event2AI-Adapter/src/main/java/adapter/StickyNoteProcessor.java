package adapter;

import com.google.gson.JsonArray;
import entity.Group;
import entity.StickyNote;
import usecase.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickyNoteProcessor {
    private List<StickyNote> stickyNotes;

    private List<Group> groups;
    private List<GroupToJsonDto> groupToJsonDtos;

    private Map<String, List<Group>> groupsSortedByAggregate;
    private Map<String, List<GroupToJsonDto>> groupSortByAggregateToJsonDtos;

    private List<List<Group>> groupsSortByPosition;
    private List<List<GroupToJsonDto>> groupSortByPositionToJsonDtos;

    public StickyNoteProcessor(){}

    public void normalProcess(String filePath) {
        this.stickyNotes = processFile(filePath);
        this.groups = processStickyNotesToGroup(this.stickyNotes);
        this.groupToJsonDtos = processGroupToJsonDto(this.groups);
    }

    public void sortByAggregateProcess(String filePath) {
        this.stickyNotes = processFile(filePath);
        this.groups = processStickyNotesToGroup(this.stickyNotes);
        this.groupsSortedByAggregate = processGroupsToSortByAggregateGroup(this.groups);
        this.groupSortByAggregateToJsonDtos = processSortByAggregateGroupToJsonDto(this.groupsSortedByAggregate);
    }

    public void sortByPositionProcess(String filePath) {
        this.stickyNotes = processFile(filePath);
        this.groups = processStickyNotesToGroup(this.stickyNotes);
        this.groupsSortByPosition = processGroupsToSortByPositionGroup(this.groups);
        this.groupSortByPositionToJsonDtos = processSortByPositionGroupToJsonDto(this.groupsSortByPosition);
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

    private Map<String, List<Group>> processGroupsToSortByAggregateGroup(List<Group> groups){
        SortGroupByAggregateUseCase sortGroupByAggregateUseCase = new SortGroupByAggregateUseCase();
        sortGroupByAggregateUseCase.sort(groups);
        return sortGroupByAggregateUseCase.getSortedGroups();

    }

    private List<List<Group>> processGroupsToSortByPositionGroup(List<Group> groups){
        SortGroupByPositionUseCase sortGroupByPositionUseCase = new SortGroupByPositionUseCase();
        sortGroupByPositionUseCase.sort(groups);
        return sortGroupByPositionUseCase.getSortedGroups();

    }

    // XXX -> XXXDto
    private List<GroupToJsonDto> processGroupToJsonDto(List<Group> groups){
        List<GroupToJsonDto> result = new ArrayList<>();
        for(Group group : groups){
            GroupToJsonDto groupToJsonDto = new GroupToJsonDto(group);
            result.add(groupToJsonDto);
        }
        return result;
    }

    private Map<String, List<GroupToJsonDto>> processSortByAggregateGroupToJsonDto(Map<String, List<Group>> sortedGroups){
        Map<String, List<GroupToJsonDto>> result = new HashMap<>();

        for (Map.Entry<String, List<Group>> entry : sortedGroups.entrySet()) {
            String key = entry.getKey();
            List<Group> groups = entry.getValue();

            List<GroupToJsonDto> dtoList = processGroupToJsonDto(groups);
            result.put(key, dtoList);
        }

        return result;
    }

    private List<List<GroupToJsonDto>> processSortByPositionGroupToJsonDto(List<List<Group>> sortedGroups){
        List<List<GroupToJsonDto>> result = new ArrayList<>();

//        for (Map.Entry<String, List<Group>> entry : sortedGroups.entrySet()) {
//            String key = entry.getKey();
//            List<Group> groups = entry.getValue();
//
//            List<GroupToJsonDto> dtoList = processGroupToJsonDto(groups);
//            result.put(key, dtoList);
//        }
        for (List<Group> groups : sortedGroups) {
            List<GroupToJsonDto> dtoList = processGroupToJsonDto(groups);
            result.add(dtoList);
        }

        return result;
    }

    public List<GroupToJsonDto> getGroupToJsonDtos(){
        return groupToJsonDtos;
    }
    public Map<String, List<GroupToJsonDto>> getGroupSortByAggregateToJsonDtos(){
        return groupSortByAggregateToJsonDtos;
    }
    public List<List<GroupToJsonDto>> getGroupSortByPositionToJsonDtos(){
        return groupSortByPositionToJsonDtos;
    }
}
