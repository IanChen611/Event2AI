package dirvers.app;

import dirvers.auth.MiroOAuthClient;
import dirvers.core.MiroJsonTransformer;

public class SetMiroApiEnv {
    private final String boardId;
    private final MiroOAuthClient.TokenResponse token;

    public SetMiroApiEnv() throws Exception{
        Dotenv envFile = Dotenv.load();
        String clientId = firstNonBlank(envFile.get("MIRO_CLIENT_ID"), System.getenv("MIRO_CLIENT_ID"));
        String clientSecret = firstNonBlank(envFile.get("MIRO_CLIENT_SECRET"), System.getenv("MIRO_CLIENT_SECRET"));
        String redirectUri = firstNonBlank(envFile.get("MIRO_REDIRECT_URI"), System.getenv("MIRO_REDIRECT_URI"), "http://localhost:8000/callback");
        String scopes = firstNonBlank(envFile.get("MIRO_SCOPES"), System.getenv("MIRO_SCOPES"), "boards:read account:read");
        boardId = firstNonBlank(envFile.get("MIRO_BOARD_ID"), System.getenv("MIRO_BOARD_ID"));
        String envAccessToken = firstNonBlank(envFile.get("MIRO_ACCESS_TOKEN"), System.getenv("MIRO_ACCESS_TOKEN"));
        String envRefreshToken = firstNonBlank(envFile.get("MIRO_REFRESH_TOKEN"), System.getenv("MIRO_REFRESH_TOKEN"));

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

    }

    public String getBoardId() {
        return boardId;
    }

//    public

    public MiroJsonTransformer getMiroDumpClient(){
        return new MiroJsonTransformer(token.accessToken);
    }

    private String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}
