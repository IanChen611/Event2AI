package dirvers.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dirvers.auth.MiroOAuthClient;
import dirvers.core.MiroJsonResult;
import dirvers.core.MiroJsonTransformer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnvTest {
    public static void main(String[] args) throws Exception {
        Dotenv envFile = Dotenv.load();
        String clientId = firstNonBlank(envFile.get("MIRO_CLIENT_ID"), System.getenv("MIRO_CLIENT_ID"));
        String clientSecret = firstNonBlank(envFile.get("MIRO_CLIENT_SECRET"), System.getenv("MIRO_CLIENT_SECRET"));
        String redirectUri = firstNonBlank(envFile.get("MIRO_REDIRECT_URI"), System.getenv("MIRO_REDIRECT_URI"),
                "http://localhost:8000/callback");
        String scopes = firstNonBlank(envFile.get("MIRO_SCOPES"), System.getenv("MIRO_SCOPES"),
                "boards:read account:read");
        String boardId = firstNonBlank(envFile.get("MIRO_BOARD_ID"), System.getenv("MIRO_BOARD_ID"));
        String envAccessToken = firstNonBlank(envFile.get("MIRO_ACCESS_TOKEN"), System.getenv("MIRO_ACCESS_TOKEN"));
        String envRefreshToken = firstNonBlank(envFile.get("MIRO_REFRESH_TOKEN"), System.getenv("MIRO_REFRESH_TOKEN"));

        System.out.println("[config] boardId=" + boardId);

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
            System.out.println("Access token acquired:" + token.Get_Access_token());
        }

        MiroJsonTransformer client = new MiroJsonTransformer(token.accessToken);
        MiroJsonResult result = client.dumpBoard(boardId);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path rawOut = Path.of("target/board_dump.json");
        Path aiOut = Path.of("target/ai_dump.json");
        Files.createDirectories(rawOut.getParent());
        Files.write(rawOut, gson.toJson(result.getRawRoot()).getBytes(StandardCharsets.UTF_8));
        Files.write(aiOut, gson.toJson(result.getAiDump()).getBytes(StandardCharsets.UTF_8));

        JsonObject ai = result.getAiDump();
        System.out.println("[ok] Board dump saved:   " + rawOut.toAbsolutePath());
        System.out.println("[ok] AI dump saved:      " + aiOut.toAbsolutePath());
        System.out.println("[ok] StickyNotes parsed: " + ai.getAsJsonArray("stickyNotes").size());
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null)
            return null;
        for (String v : vals) {
            if (v != null && !v.isBlank())
                return v;
        }
        return null;
    }
}
