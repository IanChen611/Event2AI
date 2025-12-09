package drivers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import com.google.gson.JsonObject;
import drivers.core.MiroBoardService;
import usecase.GroupToJsonDto;

import java.util.List;

public class Integration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    public static final String TO_AI_JSON_FILE_PATH = "./ToAIJsonFile/Test/";
    private static StickyNoteProcessor stickyNoteProcessor;
    private static List<GroupToJsonDto> groupDtos;

    public static void main(String[] args) throws Exception {
        MiroBoardService miroBoardService = new MiroBoardService();
        JsonObject result = miroBoardService.run();
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result);

        stickyNoteProcessor = new StickyNoteProcessor();
        stickyNoteProcessor.process(MIRO_JSON_PATH);
        groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            jsonFileCreator.create(TO_AI_JSON_FILE_PATH + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
        }
    }
}
