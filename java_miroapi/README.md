# Miro Java OOP API Client

Java 11+ OOP 客戶端，包含 OAuth2 授權碼流程、Miro v2 API 範例（Frame 建立/查詢）。

## 結構
```
src/main/java/
  app/Main.java
  auth/MiroOAuthClient.java
  core/MiroApiClient.java
  core/FrameService.java
  model/Frame.java
```

## 先決條件
- 安裝 JDK 11+（非僅 JRE）。
- 已在 Miro Developer 建立 App，設定 redirect_uri 並開啟 scopes：`boards:read boards:write`。

## 設定環境變數（Windows PowerShell）
```powershell
$env:MIRO_CLIENT_ID = "<your_client_id>"
$env:MIRO_CLIENT_SECRET = "<your_client_secret>"
$env:MIRO_REDIRECT_URI = "http://localhost:8000/callback"
$env:MIRO_SCOPES = "boards:read boards:write"
$env:MIRO_BOARD_ID = "<your_board_id>"
```

## 建置與執行
進到專案資料夾後：
```powershell
# 建置（需要 JDK）
mvn -q -DskipTests package

# 直接執行（預設跑 Frame 範例）
mvn -q -DskipTests exec:java
```
流程：會自動開啟瀏覽器 → Miro 授權 → 完成後本地回呼伺服器關閉 → 交換 token → 呼叫 v2 API 建立 & 查詢 Frame。

> 注意：Maven 若出現 `No compiler is provided`，請確認環境為 JDK 而非 JRE，並設定 JAVA_HOME 與 PATH。

## 設定對照
- Token 端點：`https://api.miro.com/v1/oauth/token`
- 授權頁：`https://miro.com/oauth/authorize`
- Frames API：
  - POST `https://api.miro.com/v2/boards/{board_id}/frames`
  - GET  `https://api.miro.com/v2/boards/{board_id}/frames/{frame_id}`

## 執行模式

### A) 使用現有 Access Token（跳過授權）
設定環境變數後直接執行。

```powershell
$env:MIRO_ACCESS_TOKEN = "<your_access_token>"
$env:MIRO_BOARD_ID = "<your_board_id>"
mvn -q -DskipTests package
mvn -q -DskipTests exec:java
```

### B) OAuth 授權後執行
若未提供 `MIRO_ACCESS_TOKEN`，程式會自動走瀏覽器授權流程：

```powershell
$env:MIRO_CLIENT_ID = "<your_client_id>"
$env:MIRO_CLIENT_SECRET = "<your_client_secret>"
$env:MIRO_REDIRECT_URI = "http://localhost:8000/callback"
$env:MIRO_SCOPES = "boards:read boards:write"
$env:MIRO_BOARD_ID = "<your_board_id>"
mvn -q -DskipTests package
mvn -q -DskipTests exec:java
```

### C) Dump 模式（匯出整個板子的 JSON 結構）
將看板 meta 與所有 items 以 pretty JSON 輸出到 `target/board_dump.json`（含分頁、每頁 50 上限，並在 items 為空時補抓 frames）。

```powershell
$env:MIRO_ACCESS_TOKEN = "<your_access_token>"
$env:MIRO_BOARD_ID = "<your_board_id>"
mvn -q -DskipTests exec:java "-Dexec.args=dump"

# 自訂輸出檔名（選）
mvn -q -DskipTests exec:java "-Dexec.args=dump d:\temp\my_board.json"
```

輸出內容：
- fetchedAt：擷取時間
- board：看板基本資訊
- items：看板上各類圖元（sticky、text、shape、frame…）
- itemCount：物件數量

## 延伸
- 可依照 `core/FrameService` 範例擴充 Shapes、Text、Sticky Notes 等服務。
