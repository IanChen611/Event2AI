# Miro Java OOP API Client — 給 AI 看的設計說明 (Bilingual)

> **Purpose / 目的**  
> Convert the prior Python Miro API logic into a clean **Java OOP** structure, and include a **full OAuth2 Authorization Code flow** to obtain an `access_token`.  
> 將既有 Python 的 Miro API 流程，重構為 **Java 物件導向** 架構，並實作 **OAuth2 授權碼流程** 以取得 `access_token`。

---

## 1) Architecture / 架構

**Layers / 分層**
- **Model**: `Frame` — Plain data class for board frame fields.  
  **模型**：`Frame` — 用來封裝 Frame 欄位的資料類別。
- **API Client**: `MiroApiClient` — Low-level HTTP (GET/POST) with `Authorization: Bearer ...`.  
  **API 客戶端**：`MiroApiClient` — 負責 HTTP 請求與加上 `Authorization` 標頭。
- **Service**: `FrameService` — Business logic: create frame, get frame… returns `Frame`.  
  **服務層**：`FrameService` — 商業邏輯：建立 / 讀取 Frame，回傳 `Frame` 物件。
- **Auth**: `MiroOAuthClient` — Implements OAuth2 code flow to get/refresh tokens.  
  **授權**：`MiroOAuthClient` — 實作 OAuth2 授權碼流程與 refresh token。
- **App**: `Main` — Wires everything and demonstrates usage.  
  **應用**：`Main` — 組裝以上元件並示範使用。

**Why / 為什麼**  
- Single Responsibility (SRP) & better testability.  
- 單一職責，易於測試與維護。

---

## 2) Endpoints & Scopes / 端點與權限

- **Authorize URL**: `https://miro.com/oauth/authorize`  
  **Token URL**: `https://api.miro.com/v1/oauth/token` (Auth endpoints are under `v1` but valid.)
- **REST API (v2)** examples:  
  - `GET https://api.miro.com/v2/boards`  
  - `POST https://api.miro.com/v2/boards/{board_id}/frames`
- **Scopes (最少需求)**: `boards:read boards:write`

> 註：`redirect_uri` 必須在 Miro App 設定中登錄，例如：`http://127.0.0.1:51789/callback`。

---

## 3) Project Setup / 專案設定

**Java**: 11+  
**Build**: Maven (示例)  
**Deps**: Gson (JSON)

```xml
<!-- pom.xml (snippets) -->
<dependencies>
  <dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.11.0</version>
  </dependency>
</dependencies>
```

**Structure / 目錄**
```
src/main/java/
  app/Main.java
  auth/MiroOAuthClient.java
  core/MiroApiClient.java
  core/FrameService.java
  model/Frame.java
```

---

## 4) Model — `Frame`

```java
package model;

public class Frame {
    private String id;
    private String title;
    private int x;
    private int y;

    public Frame() {}

    public Frame(String id, String title, int x, int y) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    @Override
    public String toString() {
        return "Frame{id='" + id + "', title='" + title + "', x=" + x + ", y=" + y + "}";
    }
}
```

---

## 5) Core — `MiroApiClient`

- Adds `Authorization: Bearer <token>` automatically.  
- Encapsulates low-level HTTP via Java 11 `HttpClient`.

```java
package core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MiroApiClient {
    private final String token;
    private final HttpClient client = HttpClient.newHttpClient();

    public MiroApiClient(String token) {
        this.token = token;
    }

    public String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public String postJson(String url, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
```

---

## 6) Service — `FrameService`

- Creates a frame and fetches a frame, returning strong-typed `Frame` objects.  
- Uses **Gson** to parse Miro responses.

```java
package core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Frame;

public class FrameService {
    private final MiroApiClient api;
    private final String boardId;

    public FrameService(MiroApiClient api, String boardId) {
        this.api = api;
        this.boardId = boardId;
    }

    public Frame createFrame(String title, int x, int y) throws Exception {
        String body = """
        {
          "data": { "title": "%s" },
          "position": { "x": %d, "y": %d }
        }
        """.formatted(title, x, y);

        String url = "https://api.miro.com/v2/boards/" + boardId + "/frames";
        String response = api.postJson(url, body);

        JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
        String id = obj.get("id").getAsString();
        String frameTitle = obj.getAsJsonObject("data").get("title").getAsString();
        return new Frame(id, frameTitle, x, y);
    }

    public Frame getFrame(String frameId) throws Exception {
        String url = "https://api.miro.com/v2/boards/" + boardId + "/frames/" + frameId;
        String response = api.get(url);

        JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
        String id = obj.get("id").getAsString();
        String title = obj.getAsJsonObject("data").get("title").getAsString();
        int x = obj.getAsJsonObject("position").get("x").getAsInt();
        int y = obj.getAsJsonObject("position").get("y").getAsInt();
        return new Frame(id, title, x, y);
    }
}
```

---

## 7) Auth — `MiroOAuthClient` (Authorization Code Flow)

- **Opens browser** to Miro authorize URL.  
- **Starts a tiny local HTTP server** to capture `code` on the redirect.  
- **Exchanges** the `code` for `access_token` + `refresh_token`.

```java
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
    private final String redirectUri; // e.g. http://127.0.0.1:51789/callback
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
        String state = UUID.randomUUID().toString();
        String authUrl = buildAuthorizeUrl(state);

        // 1) Start local server to capture the authorization code
        URI cb = URI.create(redirectUri);
        int port = cb.getPort() == -1 ? 80 : cb.getPort();
        String path = cb.getPath();
        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder codeHolder = new StringBuilder();
        StringBuilder stateHolder = new StringBuilder();

        HttpServer server = HttpServer.create(new InetSocketAddress(cb.getHost(), port), 0);
        server.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQuery(query);
                codeHolder.append(params.getOrDefault("code", ""));
                stateHolder.append(params.getOrDefault("state", ""));

                String html = "<html><body><h2>Miro auth complete.</h2>You can close this window.</body></html>";
                exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(html.getBytes(StandardCharsets.UTF_8));
                }
                latch.countDown();
            }
        });
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();

        // 2) Open browser to authorize
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI.create(authUrl));
        } else {
            System.out.println("Open this URL in your browser:\n" + authUrl);
        }

        // 3) Wait for callback
        latch.await();
        server.stop(0);

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
```

**Notes / 注意**
- Token endpoint 在 `v1` 路徑，但仍為官方有效端點。  
- 建議以環境變數或安全儲存方式保存 `client_secret` / tokens。

---

## 8) App — `Main` (Wire-up & Demo)

- Reads config from env vars (fallback to placeholders).  
- Runs OAuth, then builds `FrameService` and calls APIs.

```java
package app;

import auth.MiroOAuthClient;
import core.FrameService;
import core.MiroApiClient;
import model.Frame;

public class Main {
    public static void main(String[] args) throws Exception {
        String clientId = getenvOr("MIRO_CLIENT_ID", "YOUR_CLIENT_ID");
        String clientSecret = getenvOr("MIRO_CLIENT_SECRET", "YOUR_CLIENT_SECRET");
        String redirectUri = getenvOr("MIRO_REDIRECT_URI", "http://127.0.0.1:51789/callback");
        String scopes = getenvOr("MIRO_SCOPES", "boards:read boards:write");
        String boardId = getenvOr("MIRO_BOARD_ID", "YOUR_BOARD_ID");

        // 1) OAuth 2.0 — get tokens
        MiroOAuthClient oauth = new MiroOAuthClient(clientId, clientSecret, redirectUri, scopes);
        MiroOAuthClient.TokenResponse token = oauth.authorizeAndGetToken();
        System.out.println("Token: " + token);

        // 2) Build API client & services
        MiroApiClient api = new MiroApiClient(token.accessToken);
        FrameService frames = new FrameService(api, boardId);

        // 3) Create & fetch a frame
        Frame created = frames.createFrame("Java Frame", 100, 200);
        System.out.println("Created: " + created);

        Frame fetched = frames.getFrame(created.getId());
        System.out.println("Fetched: " + fetched);
    }

    private static String getenvOr(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }
}
```

---

## 9) How to run / 執行方式

1. Configure your **Miro app** with:
   - `redirect_uri`: `http://127.0.0.1:51789/callback` (or your choice)
   - Scopes: `boards:read boards:write`
2. Set environment variables / 設定環境變數：
   - `MIRO_CLIENT_ID`, `MIRO_CLIENT_SECRET`, `MIRO_REDIRECT_URI`, `MIRO_SCOPES`, `MIRO_BOARD_ID`
3. Run `Main` / 執行 `Main`：
   - The browser opens; approve permissions; you’ll see "Miro auth complete".  
     瀏覽器會自動打開並授權，頁面顯示完成訊息。
4. Console shows tokens (masked) and frame operations.  
   主控台將輸出 token（部分遮蔽）與 Frame 操作結果。

---

## 10) Differences vs Original Python / 與原 Python 差異

- **Separated concerns**: No `print` inside service; return models.  
  **關注點分離**：服務層不直接輸出，回傳 `Frame`。
- **Strong typing**: Use `Frame` & `TokenResponse` instead of raw JSON maps.  
  **強類型**：以 `Frame` / `TokenResponse` 取代隨意字串 key。
- **Replace PAT with OAuth**: Full Authorization Code flow + Refresh.  
  **改為 OAuth**：完整授權碼流程與 refresh token。

---

## 11) Extensibility / 擴充建議

- Add more services (`ShapeService`, `StickyService`…).  
- Centralize error handling & logging.  
- Persist tokens securely (file/DB/keystore).  
- Add PKCE if you need a public client (no secret).  
- Unit tests for parsing & flows.

---

## 12) Quick Checklist / 快速檢查

- [ ] App scopes include `boards:read` & `boards:write`  
- [ ] `redirect_uri` exactly matches app settings  
- [ ] Using `Authorization: Bearer <access_token>` for v2 calls  
- [ ] Token refresh implemented (optional but recommended)

---

**End of doc** — Ready for AI or teammates to follow.

