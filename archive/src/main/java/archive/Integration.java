package archive;

import app.BoardDump;
import auth.MiroOAuthClient;
import core.MiroApiClient;
import core.MiroItem;
import core.MiroProcessor;
import event2ai.stickynote.Classifier;
import event2ai.stickynote.StickyNote;

import java.util.ArrayList;
import java.util.List;

public class Integration {
    public static final String MIRO_BOARD_OUTPUT_PATH = "miro/miro_component.json";

    public static void main(String[] args) throws Exception {
        // 1. Certification
        String clientId = "3458764636903400125";
        String clientSecret = "nOnSTpg2TGnJ1xnqWhNpRByevYSyhcT1";
        String redirectUri = "http://localhost:8000/callback";
        String scopes = "boards:read boards:write account:read";
        String boardId = "uXjVJVDZJb0=";
        String envAccessToken = "eyJtaXJvLm9yaWdpbiI6ImV1MDEifQ_1BJpUW614_4UlLg03Zwv4AYYT8k";
        String envRefreshToken = System.getenv("MIRO_REFRESH_TOKEN");

        // 2. take JSON
        MiroOAuthClient.TokenResponse token;
        if (envAccessToken != null && !envAccessToken.isBlank()) {
            token = new MiroOAuthClient.TokenResponse();
            token.accessToken = envAccessToken;
            token.refreshToken = envRefreshToken;
            token.tokenType = "bearer";
            System.out.println("Using access token from environment variable MIRO_ACCESS_TOKEN");
        } else {
            MiroOAuthClient oauth = new MiroOAuthClient(clientId, clientSecret, redirectUri, scopes);
            token = oauth.authorizeAndGetToken();
            if (token.accessToken != null) {
                String preview = token.accessToken.length() > 12 ? token.accessToken.substring(0, 12) + "..." : token.accessToken;
                System.out.println("Access token acquired (preview): " + preview);
            }
            System.out.println("Token meta: " + token);
            System.out.println("Access_token: " + token.Get_Access_token());
        }
        MiroApiClient api = new MiroApiClient(token.accessToken);

        BoardDump.run(api, boardId, MIRO_BOARD_OUTPUT_PATH);

        // 3. Processing JSON
        MiroProcessor miroProcessor = new MiroProcessor();
        ArrayList<MiroItem> miroItems = miroProcessor.processFile(MIRO_BOARD_OUTPUT_PATH);
        MiroItemsArchive miroItemsArchive = new MiroItemsArchive();
        miroItemsArchive.save(miroItems);

        // 4. Group
        ArrayList<StickyNote> stickyNotes = miroItemsArchive.getItemsWith("StickyNote");

        Classifier classifier = new Classifier(stickyNotes);
        System.out.println(classifier.getGroupAmount());
        for(int i = 0; i < classifier.getGroupAmount(); i++){
            List<StickyNote> group = classifier.getGroupByGroupIdx(i);
            for(int j = 0; j < group.size(); j++){
                System.out.println("Group " + i + ", index = " + j + "'s Description:" + group.get(j).getDescription());
            }
        }
    }

}
