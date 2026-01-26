package drivers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import adapter.StickyNoteToGroupSortedByAggregateProcessor;
import com.google.gson.JsonObject;
import drivers.core.MiroBoardService;
import usecase.GroupToJsonDto;

import java.util.List;
import java.util.Map;

public class SortedByAggregateIntegration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    public static final String TO_AI_JSON_FILE_PATH = "./ToAIJsonFile/Test/";
    private static StickyNoteToGroupSortedByAggregateProcessor stickyNoteToGroupSortedByAggregateProcessor;
    private static Map<String, List<GroupToJsonDto>> groupSortedByAggregateDtos;

    public static void main(String[] args) throws Exception {
        MiroBoardService miroBoardService = new MiroBoardService();
        JsonObject result = miroBoardService.run();
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result);

        stickyNoteToGroupSortedByAggregateProcessor = new StickyNoteToGroupSortedByAggregateProcessor();
        stickyNoteToGroupSortedByAggregateProcessor.process(MIRO_JSON_PATH);
        groupSortedByAggregateDtos = stickyNoteToGroupSortedByAggregateProcessor.getGroupSortedByAggregateToJsonDtos();
        for (Map.Entry<String, List<GroupToJsonDto>> entry
                : groupSortedByAggregateDtos.entrySet()) {

            String aggregateName = entry.getKey();
            List<GroupToJsonDto> groupDtos = entry.getValue();

            for (GroupToJsonDto groupDto : groupDtos) {
                String path = TO_AI_JSON_FILE_PATH
                        + aggregateName
                        + "/"
                        + groupDto.getUsecaseName().replace(" ", "_").trim()
                        + ".json";

                jsonFileCreator.create(path, groupDto);
            }
        }
    }
}
