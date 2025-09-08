package auth;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class MiroOAuthClient {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri; // e.g. http://localhost:8000/callback
    private final String scopes;      // e.g. "boards:read boards:write"

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    public MiroOAuthClient(String clientId, String clientSecret, String redirectUri, String scopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    public TokenResponse authorizeAndGetToken() throws Exception {
    // Debug: show which redirect URI is actually used (helps when mismatch with Miro app settings)
    System.out.println("[OAuth] Using redirect URI: " + redirectUri);
        String state = UUID.randomUUID().toString();
        String authUrl = buildAuthorizeUrl(state);
    System.out.println("[OAuth] Authorization URL: " + authUrl);

        // 1) Start local server to capture the authorization code
        URI cb = URI.create(redirectUri);
        int port = cb.getPort() == -1 ? 80 : cb.getPort();
        String path = cb.getPath();
        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder codeHolder = new StringBuilder();
        StringBuilder stateHolder = new StringBuilder();

        HttpServer server = HttpServer.create(new InetSocketAddress(cb.getHost(), port), 0);
        java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();
        server.setExecutor(executor);
        server.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    String query = exchange.getRequestURI().getQuery();
                    Map<String, String> params = parseQuery(query);
                    codeHolder.append(params.getOrDefault("code", ""));
                    stateHolder.append(params.getOrDefault("state", ""));

                    String html = "<html><body><h2>Miro auth complete.</h2>You can close this window.</body></html>";
                    exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(html.getBytes(StandardCharsets.UTF_8));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });
        server.start();

        // 2) Open browser to authorize
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI.create(authUrl));
        } else {
            System.out.println("Open this URL in your browser:\n" + authUrl);
        }

        // 3) Wait for callback
    latch.await();
    server.stop(0); // stop accepting
    executor.shutdown(); // allow JVM to exit

        if (!state.equals(stateHolder.toString())) {
            throw new IllegalStateException("State mismatch, potential CSRF detected");
        }
        String code = codeHolder.toString();
        if (code.isEmpty()) throw new IllegalStateException("Authorization code not found");

        // 4) Exchange code for tokens
        return exchangeCodeForToken(code);
    }

    private String buildAuthorizeUrl(String state) throws Exception {
        String base = "https://miro.com/oauth/authorize";
        return base +
                "?response_type=code" +
                "&client_id=" + url(clientId) +
                "&redirect_uri=" + url(redirectUri) +
                "&scope=" + url(scopes) +
                "&state=" + url(state);
    }

    public TokenResponse refreshAccessToken(String refreshToken) throws Exception {
        String body = form(Map.of(
                "grant_type", "refresh_token",
                "client_id", clientId,
                "client_secret", clientSecret,
                "refresh_token", refreshToken
        ));
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.miro.com/v1/oauth/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 300) {
            throw new RuntimeException("Refresh failed: " + resp.statusCode() + "\n" + resp.body());
        }
        return new Gson().fromJson(resp.body(), TokenResponse.class);
    }

    private TokenResponse exchangeCodeForToken(String code) throws Exception {
        String body = form(Map.of(
                "grant_type", "authorization_code",
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code,
                "redirect_uri", redirectUri
        ));
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.miro.com/v1/oauth/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 300) {
            throw new RuntimeException("Token exchange failed: " + resp.statusCode() + "\n" + resp.body());
        }
        return new Gson().fromJson(resp.body(), TokenResponse.class);
    }

    private static String url(String s) throws Exception {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String form(Map<String, String> kv) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : kv.entrySet()) {
            if (sb.length() > 0) sb.append('&');
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8));
            sb.append('=');
            sb.append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static Map<String, String> parseQuery(String q) {
        Map<String, String> map = new HashMap<>();
        if (q == null || q.isEmpty()) return map;
        for (String pair : q.split("&")) {
            String[] kv = pair.split("=", 2);
            String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String v = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(k, v);
        }
        return map;
    }

    public static class TokenResponse {
        @SerializedName("access_token") public String accessToken;
        @SerializedName("refresh_token") public String refreshToken;
        @SerializedName("expires_in") public long expiresIn;
        @SerializedName("token_type") public String tokenType;
        @SerializedName("scope") public String scope;
        @SerializedName("user_id") public String userId;
        @SerializedName("team_id") public String teamId;

        @Override public String toString() {
            return "TokenResponse{access_token=***, expires_in=" + expiresIn + ", scope='" + scope + "'}";
        }
    }
}
