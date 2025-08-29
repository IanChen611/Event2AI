package app;

import auth.MiroOAuthClient;
import core.FrameService;
import core.MiroApiClient;

public class Main {
    public static void main(String[] args) throws Exception {
        String clientId = getenvOr("MIRO_CLIENT_ID", "YOUR_CLIENT_ID");
        String clientSecret = getenvOr("MIRO_CLIENT_SECRET", "YOUR_CLIENT_SECRET");
        String redirectUri = getenvOr("MIRO_REDIRECT_URI", "http://127.0.0.1:51789/callback");
        String scopes = getenvOr("MIRO_SCOPES", "boards:read boards:write");
        String boardId = getenvOr("MIRO_BOARD_ID", "YOUR_BOARD_ID");
        String envAccessToken = getenvOr("MIRO_ACCESS_TOKEN", "");
        String envRefreshToken = getenvOr("MIRO_REFRESH_TOKEN", "");

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
            System.out.println("Token: " + token);
        }

        // 2) Build API client & services
        MiroApiClient api = new MiroApiClient(token.accessToken);
        FrameService frames = new FrameService(api, boardId);

        // 3) Router: if arg present and equals "dump", run board dump; else run frame example
        if (args != null && args.length > 0 && "dump".equalsIgnoreCase(args[0])) {
            String out = args.length > 1 ? args[1] : null; // optional output path
            BoardDump.run(api, boardId, out);
        } else {
            FrameExample.run(frames);
        }
    }

    private static String getenvOr(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }
}
