package drivers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import com.google.gson.JsonObject;
import drivers.core.MiroBoardService;
import usecase.GroupToJsonDto;

import java.util.List;
import java.util.Map;

public class Integration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    public static final String TO_AI_JSON_FILE_PATH = "./ToAIJsonFile/Test/";
    private static StickyNoteProcessor stickyNoteProcessor;
    private static List<GroupToJsonDto> groupDtos;
    private static Map<String, List<GroupToJsonDto>> groupSortedByAggregateDtos;
    private static List<List<GroupToJsonDto>> groupSortedByPositionDtos;

    public static void main(String[] args) throws Exception {
        System.out.println(args.length);
        for (String arg : args) {
            System.out.println(arg);
        }

        StickyNoteProcessMode mode = parseMode(args);

        MiroBoardService miroBoardService = new MiroBoardService();
        JsonObject result = miroBoardService.run();
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result);

        stickyNoteProcessor = new StickyNoteProcessor();
        switch (mode) {
            case NORMAL:
                stickyNoteProcessor.normalProcess(MIRO_JSON_PATH);
                groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
                for (GroupToJsonDto groupDto : groupDtos) {
                    jsonFileCreator.create(TO_AI_JSON_FILE_PATH + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
                }
                break;
            case SORT_BY_AGGREGATE:
                stickyNoteProcessor.sortByAggregateProcess(MIRO_JSON_PATH);
                groupSortedByAggregateDtos = stickyNoteProcessor.getGroupSortByAggregateToJsonDtos();
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
                break;
            case SORT_BY_POSITION:
                stickyNoteProcessor.sortByPositionProcess(MIRO_JSON_PATH);
                groupSortedByPositionDtos = stickyNoteProcessor.getGroupSortByPositionToJsonDtos();
                for (List<GroupToJsonDto> groupDtos : groupSortedByPositionDtos) {
                    for (GroupToJsonDto groupDto : groupDtos) {
                    }
                }
                for (int i = 0; i < groupSortedByPositionDtos.size(); i++) {
                    for (int j = 0; j < groupSortedByPositionDtos.get(i).size(); j++) {
                        String path = TO_AI_JSON_FILE_PATH
                                + ( i + 1 ) + "_"
                                + ( j + 1 ) + "_"
                                + groupSortedByPositionDtos.get(i).get(j).getUsecaseName().replace(" ", "_").trim()
                                + ".json";
                        jsonFileCreator.create(path, groupSortedByPositionDtos.get(i).get(j));
                    }
                }
        }

    }

    private static StickyNoteProcessMode parseMode(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--mode=")) {
                switch (arg.substring(7)){
                    case "normal":
                        return StickyNoteProcessMode.NORMAL;
                    case "aggregate":
                        return StickyNoteProcessMode.SORT_BY_AGGREGATE;
                    case "position":
                        return StickyNoteProcessMode.SORT_BY_POSITION;
                }
            }
        }
        return StickyNoteProcessMode.NORMAL; // default
    }
}
