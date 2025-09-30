package dirvers;

import adapter.JsonFileCreator;
import adapter.StickyNoteProcessor;
import dirvers.app.Dotenv;
import dirvers.auth.MiroOAuthClient;
import dirvers.core.DumpResult;
import dirvers.core.MiroDumpClient;
import usecase.GroupToJsonDto;

import java.util.List;

public class Integration {
    private static final JsonFileCreator jsonFileCreator = new JsonFileCreator();
    public static final String MIRO_JSON_PATH = "./miro/clean_dump.json";
    private static StickyNoteProcessor stickyNoteProcessor;
    private static List<GroupToJsonDto> groupDtos;

    public static void main(String[] args) throws Exception {
        Dotenv envFile = Dotenv.load();
        String clientId = firstNonBlank(envFile.get("MIRO_CLIENT_ID"), System.getenv("MIRO_CLIENT_ID"));
        String clientSecret = firstNonBlank(envFile.get("MIRO_CLIENT_SECRET"), System.getenv("MIRO_CLIENT_SECRET"));
        String redirectUri = firstNonBlank(envFile.get("MIRO_REDIRECT_URI"), System.getenv("MIRO_REDIRECT_URI"), "http://localhost:8000/callback");
        String scopes = firstNonBlank(envFile.get("MIRO_SCOPES"), System.getenv("MIRO_SCOPES"), "boards:read account:read");
        String boardId = firstNonBlank(envFile.get("MIRO_BOARD_ID"), System.getenv("MIRO_BOARD_ID"));
        String envAccessToken = firstNonBlank(envFile.get("MIRO_ACCESS_TOKEN"), System.getenv("MIRO_ACCESS_TOKEN"));
        String envRefreshToken = firstNonBlank(envFile.get("MIRO_REFRESH_TOKEN"), System.getenv("MIRO_REFRESH_TOKEN"));

        MiroOAuthClient.TokenResponse token;
        if (envAccessToken != null && !envAccessToken.isBlank()) {
            token = new MiroOAuthClient.TokenResponse();
            token.accessToken = envAccessToken;
            token.refreshToken = envRefreshToken;
            token.tokenType = "bearer";
            System.out.println("Using access token from env");
        } else {
            MiroOAuthClient oauth = new MiroOAuthClient(clientId, clientSecret, redirectUri, scopes);
            token = oauth.authorizeAndGetToken();
            System.out.println("Access token acquired");
        }

        MiroDumpClient client = new MiroDumpClient(token.accessToken);
        DumpResult result = client.dumpBoard(boardId);
        // ######################################################################
        jsonFileCreator.create(MIRO_JSON_PATH, result.getAiDump());

        stickyNoteProcessor = new StickyNoteProcessor(MIRO_JSON_PATH);
        groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            jsonFileCreator.create("./ToAIJsonFile/" + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
        }
    }


    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}
