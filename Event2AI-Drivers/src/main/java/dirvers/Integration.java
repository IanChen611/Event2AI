package dirvers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import dirvers.core.MiroJsonResult;
import dirvers.core.MiroBoardService;
import usecase.GroupToJsonDto;

import java.util.List;

public class Integration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    private static StickyNoteProcessor stickyNoteProcessor;
    private static List<GroupToJsonDto> groupDtos;

    public static void main(String[] args) throws Exception {
        MiroBoardService runMiroClient = new MiroBoardService();
        MiroJsonResult result = runMiroClient.run();
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result.getAiDump());

        stickyNoteProcessor = new StickyNoteProcessor(MIRO_JSON_PATH);
        groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            jsonFileCreator.create("./ToAIJsonFile/Test/" + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
        }
    }
}
