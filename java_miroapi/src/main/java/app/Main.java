package app;

import auth.MiroOAuthClient;
//import core.FrameService;
import core.MiroApiClient;

public class Main {
    public static void main(String[] args) throws Exception {
    Dotenv envFile = Dotenv.load();
    String clientId = firstNonBlank(
        envFile.get("MIRO_CLIENT_ID"),
        System.getenv("MIRO_CLIENT_ID"),
        "YOUR_CLIENT_ID"
    );
    String clientSecret = firstNonBlank(envFile.get("MIRO_CLIENT_SECRET"), System.getenv("MIRO_CLIENT_SECRET"), "YOUR_CLIENT_SECRET");
    String redirectUri = firstNonBlank(envFile.get("MIRO_REDIRECT_URI"), System.getenv("MIRO_REDIRECT_URI"), "http://localhost:8000/callback");
    String scopes = firstNonBlank(envFile.get("MIRO_SCOPES"), System.getenv("MIRO_SCOPES"), "boards:read boards:write account:read");
    String boardId = firstNonBlank(envFile.get("MIRO_BOARD_ID"), System.getenv("MIRO_BOARD_ID"), "YOUR_BOARD_ID");
    String envAccessToken = firstNonBlank(envFile.get("MIRO_ACCESS_TOKEN"), System.getenv("MIRO_ACCESS_TOKEN"), "");
    String envRefreshToken = firstNonBlank(envFile.get("MIRO_REFRESH_TOKEN"), System.getenv("MIRO_REFRESH_TOKEN"), "");
    System.out.println("[config] clientId=" + mask(clientId) + ", boardId=" + boardId + ", redirect=" + redirectUri);

        // 1) Get token — prefer provided access token, otherwise run OAuth flow
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
        }

        // 2) Build API client & services
        MiroApiClient api = new MiroApiClient(token.accessToken);
//        FrameService frames = new FrameService(api, boardId);

        // 3) Router: if arg present and equals "dump", run board dump; else run frame example
        String out = null; // optional output path
        BoardDump.run(api, boardId, out);
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String mask(String v) {
        if (v == null) return "null";
        if (v.length() <= 6) return "***";
        return v.substring(0, 3) + "***" + v.substring(v.length()-3);
    }
}
