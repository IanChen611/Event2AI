package drivers.app;

import adapter.dump.MiroJsonObjectComposer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import drivers.auth.MiroOAuthClient;
import drivers.core.MiroJsonResult;
import drivers.core.MiroJsonTransformer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EnvCheck {
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

            // 將 token 寫回 .env 檔案
            updateEnvFile(token.accessToken, token.refreshToken);
            System.out.println("[ok] Tokens saved to .env file");
        }



        MiroJsonTransformer client = new MiroJsonTransformer(token.accessToken);

        JsonObject JsonObject = client.run(boardId);
        MiroJsonObjectComposer miroJsonObjectComposer = new MiroJsonObjectComposer();
        MiroJsonResult result = new MiroJsonResult(JsonObject,miroJsonObjectComposer.compose(JsonObject));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path rawOut = Path.of("target/board_dump.json");
        Path aiOut = Path.of("target/ai_dump.json");
        Files.createDirectories(rawOut.getParent());
        Files.write(rawOut, gson.toJson(result.getRawRoot()).getBytes(StandardCharsets.UTF_8));
        Files.write(aiOut, gson.toJson(result.getAiresult()).getBytes(StandardCharsets.UTF_8));

        JsonObject ai = result.getAiresult();
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

    private static void updateEnvFile(String accessToken, String refreshToken) throws Exception {
        Path envPath = Path.of(".env");
        List<String> lines = new ArrayList<>();

        boolean hasAccessToken = false;
        boolean hasRefreshToken = false;

        if (Files.exists(envPath)) {
            lines = Files.readAllLines(envPath, StandardCharsets.UTF_8);

            // 更新現有的 token 行
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("MIRO_ACCESS_TOKEN")) {
                    lines.set(i, "MIRO_ACCESS_TOKEN=" + accessToken);
                    hasAccessToken = true;
                } else if (line.startsWith("MIRO_REFRESH_TOKEN")) {
                    lines.set(i, "MIRO_REFRESH_TOKEN=" + refreshToken);
                    hasRefreshToken = true;
                }
            }
        }

        if (!hasAccessToken) {
            lines.add("MIRO_ACCESS_TOKEN=" + accessToken);
        }
        if (!hasRefreshToken) {
            lines.add("MIRO_REFRESH_TOKEN=" + refreshToken);
        }

        Files.write(envPath, lines, StandardCharsets.UTF_8);
    }
}
