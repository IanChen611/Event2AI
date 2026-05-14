# Event2AI

**Event Storming 視覺化建模**與 **AI 程式碼生成**之間的橋樑。Event2AI 從 Miro 白板中擷取結構化的使用案例定義，並將其轉換為標準化的 JSON 規格，讓 AI 助理（如 Claude）能夠依此生成符合 DDD 規範的領域程式碼。

## 專案概述

Event Storming 是一種透過在白板上貼便利貼來協作建模複雜業務領域的技術。Event2AI 將這些視覺化圖表自動轉換成精確、機器可讀的規格，省去手動將領域知識傳遞給 AI 的繁瑣工作。

```
Miro 白板（Event Storming 圖）
        │
        ▼  OAuth2 API
MiroBoardService  ─── 取得便利貼、容器與座標資訊
        │
        ▼
  Integration.java  ─── 協調整個處理流程
        │
   ┌────┴────────────────┐
   ▼                     ▼
[NORMAL]         [AGGREGATE / POSITION]
        │
        ▼
StickyNoteProcessor
  ├─ ClusterStickyNotes   ─── 依空間位置群組
  ├─ ClassifyStickNotes   ─── 依顏色與位置 → Group
  └─ Sort（選用）          ─── 依 Aggregate 或 X/Y 座標排序
        │
        ▼
  GroupToJsonDto  ─── 將領域群組轉換為 DTO
        │
        ▼
  JsonFileCreator ─── 輸出格式化 JSON 檔案
        │
        ▼
ToAIJsonFile/Test/*.json  ←── 準備好供 AI 生成程式碼
```

## 功能特色

- **Miro 直接整合** — 透過 OAuth2 直接從 Miro 取得白板資料，無需手動匯出。
- **空間群組** — 以 2D 幾何演算法，依便利貼的相對距離自動分群。
- **顏色分類** — 將便利貼顏色對應至 DDD 概念（指令、事件、聚合根、角色、值物件等）。
- **三種處理模式** — `NORMAL`、`SORT_BY_AGGREGATE`、`SORT_BY_POSITION`。
- **完整 JSON 規格** — 輸出包含聚合根、領域事件、實體、值物件、列舉、建構子前後條件的完整 DDD 規格。
- **MCP 整合** — 透過 [Model Context Protocol](https://modelcontextprotocol.io) 將工具暴露給 Claude Code，實現無縫的 AI 程式碼生成工作流程。

## 專案結構

```
Event2AI/
├── Event2AI-Entity/        # 領域實體：Group、StickyNote、DomainEvent 等
├── Event2AI-UseCase/       # 業務邏輯：群組、分類、排序
├── Event2AI-Adapter/       # JSON 序列化、檔案 I/O、流程協調
├── Event2AI-Drivers/       # 主程式入口、Miro API 客戶端、OAuth2
├── Event2AI-Common/        # 共用測試工具
├── ToAIJsonFile/
│   ├── Example/            # 參考範例：TeddysExample.json
│   └── Test/               # 自動生成的使用案例 JSON（已加入 .gitignore）
├── miro/                   # Miro 白板快照
├── mcp_server.py           # Python MCP 伺服器（供 Claude Code 整合）
├── requirements.txt        # Python 套件依賴
├── start-mcp-server.bat    # Windows 啟動腳本
├── start-mcp-server.sh     # macOS / Linux 啟動腳本
└── pom.xml                 # Maven 父級 POM
```

## 環境需求

| 工具 | 版本需求 |
|------|---------|
| Java JDK | 11 以上 |
| Maven | 3.6 以上 |
| Python | 3.10 以上 |
| Claude Code | 最新版 |

## 安裝與設定

### 1. 複製專案並建置

```bash
git clone https://github.com/IanChen611/Event2AI.git
cd Event2AI
mvn clean package -DskipTests
```

### 2. 設定 Miro 憑證

在專案根目錄建立 `.env` 檔案：

```env
MIRO_CLIENT_ID=your_client_id
MIRO_CLIENT_SECRET=your_client_secret
MIRO_REFRESH_TOKEN=your_refresh_token
MIRO_ACCESS_TOKEN=your_access_token
MIRO_BOARD_ID=your_board_id
MIRO_REDIRECT_URI=your_redirect_uri
MIRO_SCOPES=boards:read
```

前往 [miro.com/app/settings/user-profile/apps](https://miro.com/app/settings/user-profile/apps) 建立 Miro 應用程式並完成 OAuth2 授權流程以取得上述憑證。

### 3. 啟動 MCP 伺服器

**Windows：**
```bat
start-mcp-server.bat
```

**macOS / Linux：**
```bash
chmod +x start-mcp-server.sh
./start-mcp-server.sh
```

啟動腳本會自動建立 Python 虛擬環境、安裝相依套件，並啟動 MCP 伺服器。伺服器啟動時會自動與 Miro 白板同步。

### 4. 在 Claude Code 中註冊 MCP 伺服器

在 Claude Code 的 MCP 設定檔（`.claude/settings.json` 或全域設定）中加入以下內容：

```json
{
  "mcpServers": {
    "Event2AI-mcp-server": {
      "command": "path/to/start-mcp-server.bat",
      "args": []
    }
  }
}
```

## 使用方式

### 透過 Claude Code（建議）

MCP 伺服器啟動並註冊後，可使用以下斜線指令：

| 指令 | 說明 |
|------|------|
| `/process_miro_board` | 從 Miro 取得最新白板並重新生成所有使用案例 JSON |
| `/list_usecases` | 列出所有可用的使用案例（會先自動與 Miro 同步） |
| `/get_usecase <名稱>` | 讀取指定使用案例的完整 JSON 規格 |
| `/generate_code_with_usecase` | 請 Claude 依據使用案例規格生成程式碼 |

### 透過 Java CLI

直接執行 Java 程式，可透過參數指定處理模式：

```bash
java -cp "Event2AI-Drivers/target/Event2AI-Drivers-0.1.0.jar:<dependencies>" \
  drivers.Integration [--mode=normal|aggregate|position]
```

| 模式 | 說明 | 輸出路徑 |
|------|------|---------|
| `normal`（預設） | 所有使用案例輸出至同一目錄 | `ToAIJsonFile/Test/*.json` |
| `aggregate` | 依聚合根分類至子目錄 | `ToAIJsonFile/Test/{Aggregate}/*.json` |
| `position` | 依 Event Storming 流程順序排列（由左至右、由上至下） | `ToAIJsonFile/Test/{x}_{y}_{usecase}.json` |

## 輸出 JSON 格式

每個生成的 JSON 檔案描述一個完整的使用案例，範例如下（`CreateProduct.json`）：

```json
{
  "useCase": "CreateProduct",
  "behavior": "Create a new product",
  "actor": ["User"],
  "input": [
    { "name": "productId", "type": "String" },
    { "name": "name",      "type": "String" }
  ],
  "aggregate": "Product",
  "aggregateId": "ProductId",
  "method": "Product constructor",
  "repository": "ProductRepository",
  "output": "CqrsOutput with productId",
  "domainEvents": [
    {
      "name": "ProductEvents.ProductCreated",
      "attributes": [
        { "name": "id",   "type": "String" },
        { "name": "name", "type": "String" }
      ]
    }
  ],
  "aggregates": [
    {
      "name": "Product",
      "attributes": [
        { "name": "id",    "type": "ProductId",             "constraint": "non-null" },
        { "name": "name",  "type": "ProductName",           "constraint": "non-null" },
        { "name": "state", "type": "ProductLifecycleState", "constraint": "default = DRAFT" }
      ]
    }
  ],
  "valueObjects": [...],
  "enums": [...],
  "constructorPreconditions": [...],
  "constructorPostconditions": [...]
}
```

完整範例請參考 [`ToAIJsonFile/Example/TeddysExample.json`](ToAIJsonFile/Example/TeddysExample.json)。

## 架構設計

Event2AI 遵循**整潔架構（Clean Architecture）**，分為四個層次：

```
┌──────────────────────────────────────┐
│  Drivers     （Integration.java）    │  ← 程式入口、Miro API、CLI
├──────────────────────────────────────┤
│  Adapter     （StickyNoteProcessor） │  ← 流程協調、JSON 輸入輸出
├──────────────────────────────────────┤
│  UseCase     （Group*、Sort*、…）    │  ← 業務邏輯：群組、分類、排序
├──────────────────────────────────────┤
│  Entity      （Group、StickyNote）   │  ← 純領域模型，無外部依賴
└──────────────────────────────────────┘
```

依賴方向僅向內層傳遞：Drivers → Adapter → UseCase → Entity。

## 開發指南

### 執行測試

```bash
mvn test
```

### 專案版本

目前版本：**0.1.0**  
所有模組共用父層 `pom.xml` 中定義的同一版本號。

### 新增處理模式

1. 在 `StickyNoteProcessMode.java` 中新增列舉值。
2. 在 `StickyNoteProcessor.java` 中實作對應的處理方法。
3. 在 `Integration.java` 中加入新的分支邏輯。
4. 在對應模組的 `src/test/` 下補充測試案例。

## 技術堆疊

| 層次 | 技術 |
|------|------|
| 核心邏輯 | Java 11、Maven |
| JSON 序列化 | GSON 2.11.0 |
| 幾何運算 | Java AWT（`java.awt.geom`） |
| 單元測試 | JUnit 5（Jupiter） |
| MCP 伺服器 | Python 3.10+、`mcp >= 1.0.0` |
| 外部 API | Miro REST API（OAuth2） |

## 授權

本專案供研究與學術用途使用。
