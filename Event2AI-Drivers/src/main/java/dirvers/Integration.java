package dirvers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import dirvers.core.DumpResult;
import dirvers.core.MiroBoardDumpService;
import usecase.GroupToJsonDto;

import java.util.List;

public class Integration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    private static StickyNoteProcessor stickyNoteProcessor;
    private static List<GroupToJsonDto> groupDtos;

    public static void main(String[] args) throws Exception {
        MiroBoardDumpService runMiroClient = new MiroBoardDumpService();
        DumpResult result = runMiroClient.run();
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result.getAiDump());

        stickyNoteProcessor = new StickyNoteProcessor(MIRO_JSON_PATH);
        groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            jsonFileCreator.create("./ToAIJsonFile/" + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
        }
    }
}
