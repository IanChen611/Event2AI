# Event2AI MCP Server Setup Guide (Python 版本)

這份文件說明如何設定和使用 Event2AI MCP Server。

## 概述

Event2AI MCP Server 是一個 Model Context Protocol (MCP) 伺服器，使用 **Python** 實作，讓您可以透過 Claude Code 與 Event2AI 專案互動。

## 功能

Server 提供以下工具：

1. **process_miro_board** - 處理 Miro board 並產生 usecase JSON 檔案
2. **list_usecases** - 列出所有可用的 usecase 檔案
3. **get_usecase** - 讀取特定 usecase 的內容
4. **get_all_usecases** - 一次讀取所有 usecase 內容

## 系統需求

- **Python 3.10+** - MCP Server 執行環境
- **Java 11+** - 執行 Integration.java
- **Maven 3.6+** - 建置 Java 專案

## 安裝步驟

### 步驟 1: 檢查 Python 是否已安裝

```bash
python --version
# 或在 Mac/Linux 上
python3 --version
```

**說明：** 確認 Python 版本是 3.10 或更高。

如果沒有安裝 Python，請前往 [python.org](https://www.python.org/downloads/) 下載安裝。

### 步驟 2: 建置 Java 專案（首次執行）

```bash
cd D:\Code\Event2AI
mvn clean package -DskipTests
```

**這個指令做什麼：**
- `mvn clean` - 清除之前的建置結果
- `package` - 編譯 Java 程式碼並打包
- `-DskipTests` - 跳過測試以加快建置

**預期結果：**
- 在 Event2AI-Drivers/target/ 建立 JAR 檔案
- Integration.java 已編譯並可執行

### 步驟 3: MCP 設定檔（已自動建立）

MCP 設定檔已經建立在：`C:\Users\ianch\.claude\mcp_settings.json`

內容如下：
```json
{
  "mcpServers": {
    "event2ai": {
      "command": "cmd",
      "args": [
        "/c",
        "D:\\Code\\Event2AI\\start-mcp-server.bat"
      ],
      "env": {},
      "disabled": false
    }
  }
}
```

**注意：** 路徑已設定為您的專案位置。如果專案位置改變，請修改這個檔案中的路徑。

### 步驟 4: 重啟 Claude Code

重新啟動 Claude Code，讓設定生效。

```bash
# 如果使用 CLI
exit

# 然後重新啟動
claude
```

## 首次啟動說明

當您第一次啟動 MCP server 時，啟動腳本會自動執行以下步驟：

1. **檢查 Python** - 確認 Python 已安裝
2. **建立虛擬環境** - 在專案目錄建立 venv/
3. **安裝依賴** - 安裝 `mcp` Python 套件
4. **啟動 MCP Server** - 執行 mcp_server.py
5. **自動同步** - 呼叫 Integration.java 處理 Miro board

**首次啟動可能需要 1-2 分鐘**（下載依賴），之後啟動會快很多（約 5-10 秒）。

## 使用方式

### 在 Claude Code 中使用工具

啟動 Claude Code 後，MCP server 會自動連接。您可以在對話中使用以下指令：

#### 範例 1：列出所有 usecase

```
請列出所有可用的 usecase
```

或明確指定工具：
```
請使用 list_usecases 工具
```

#### 範例 2：讀取特定 usecase

```
請讀取 TeddysExample usecase 的內容
```

#### 範例 3：重新處理 Miro board

```
請重新同步 Miro board
```

#### 範例 4：取得所有 usecase

```
請取得所有 usecase 的完整內容
```

## 目錄結構

```
Event2AI/
├── mcp_server.py                    # Python MCP server 主程式 ⭐
├── requirements.txt                  # Python 依賴套件列表
├── venv/                            # Python 虛擬環境（自動建立）
├── start-mcp-server.bat             # Windows 啟動腳本
├── start-mcp-server.sh              # Linux/Mac 啟動腳本
│
├── Event2AI-Drivers/                # Java 程式碼
│   └── src/main/java/dirvers/
│       └── Integration.java         # 處理 Miro board
│
├── ToAIJsonFile/                    # Usecase JSON 檔案
│   ├── Example/
│   │   └── TeddysExample.json       # 範例 usecase
│   └── Test/                        # 自動產生的 usecase
│
├── miro/
│   └── clean_dump.json              # Miro board 快照
│
└── MCP_SETUP.md                     # 本文件
```

## 運作原理

```
┌──────────────┐
│  Claude Code │
└──────┬───────┘
       │ MCP Protocol (STDIO)
       ▼
┌──────────────────┐
│  mcp_server.py   │ ◄── Python MCP Server
└────────┬─────────┘
         │
         ├─► 讀取 ToAIJsonFile/*.json
         │   (list_usecases, get_usecase, get_all_usecases)
         │
         └─► 呼叫 Java Integration
             (mvn exec:java)
             ▼
       ┌──────────────────┐
       │  Integration.java│
       └────────┬─────────┘
                │
                ├─► Miro API
                └─► 產生 JSON → ToAIJsonFile/Test/
```

## 疑難排解

### Server 無法啟動

**1. Python 找不到**
```bash
# 檢查 Python
python --version

# Windows: 確認 Python 在 PATH 中
where python

# 如果沒有，重新安裝 Python 並勾選 "Add Python to PATH"
```

**2. 依賴安裝失敗**
```bash
# 升級 pip
python -m pip install --upgrade pip

# 手動安裝依賴
cd D:\Code\Event2AI
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
```

**3. Java/Maven 問題**
```bash
# 檢查 Java
java -version

# 檢查 Maven
mvn -version

# 重新建置專案
mvn clean package -DskipTests
```

### 找不到 usecase 檔案

1. 確認 `ToAIJsonFile/` 目錄存在
2. 執行 `process_miro_board` 工具產生檔案：
   ```
   請重新處理 Miro board
   ```
3. 檢查 Miro API 設定（.env 檔案）

### MCP 連線問題

1. **重啟 Claude Code**
   ```bash
   exit
   claude
   ```

2. **檢查設定檔路徑**
   ```
   C:\Users\ianch\.claude\mcp_settings.json
   ```

3. **手動測試 MCP server**
   ```bash
   cd D:\Code\Event2AI
   python mcp_server.py
   ```
   按 Ctrl+C 停止

4. **查看 Claude Code logs**
   - 開啟 Claude Code 的輸出面板
   - 尋找 [Event2AI MCP] 開頭的訊息

### Integration.java 執行失敗

**原因：** Miro API 憑證錯誤或網路問題

**解決方式：**
1. 檢查 `.env` 檔案中的 Miro API 設定
2. 確認網路連線正常
3. 手動測試 Integration.java：
   ```bash
   mvn exec:java -Dexec.mainClass=dirvers.Integration
   ```

## 進階設定

### 修改輸出目錄

編輯 `mcp_server.py`：

```python
# 在檔案開頭修改
TOAI_JSON_DIR = PROJECT_ROOT / "ToAIJsonFile"  # 改成您想要的路徑
```

### 停用自動同步

編輯 `mcp_server.py`，在 `main()` 函數中註解掉以下程式碼：

```python
async def main():
    # 註解掉這些行
    # print("[Event2AI MCP] Server starting, auto-syncing Miro board...", file=sys.stderr)
    # try:
    #     sync_result = await process_miro_board()
    #     print("[Event2AI MCP] Initial sync completed", file=sys.stderr)
    # except Exception as e:
    #     print(f"[Event2AI MCP] Initial sync failed: {e}", file=sys.stderr)
```

### 更新 MCP 套件

```bash
cd D:\Code\Event2AI
venv\Scripts\activate
pip install --upgrade mcp
```

## 技術架構

- **MCP Server:** Python 3.10+ with `mcp` package
- **Transport:** STDIO (標準輸入/輸出)
- **資料格式:** JSON
- **Java 整合:** 透過 subprocess 呼叫 Maven

## 常見問題

### Q: 為什麼使用 Python 而不是 Java？

**A:** Python 的 MCP SDK 更成熟、API 更簡單、文檔更完整。這讓開發和維護 MCP server 更容易。Java 程式碼（Integration.java）仍然負責核心業務邏輯（處理 Miro board）。

### Q: venv 資料夾可以刪除嗎？

**A:** 可以。下次啟動時腳本會自動重新建立。如果要清理環境：
```bash
rmdir /s venv  # Windows
rm -rf venv    # Linux/Mac
```

### Q: 如何更新專案？

```bash
# 1. 更新 Java 程式碼後重新建置
mvn clean package -DskipTests

# 2. 如果修改了 mcp_server.py，只需重啟 MCP server
# 3. Claude Code 會自動重新載入
```

### Q: 可以在沒有網路的環境使用嗎？

**A:** 部分功能可以：
- ✅ list_usecases（離線）
- ✅ get_usecase（離線）
- ✅ get_all_usecases（離線）
- ❌ process_miro_board（需要網路連接 Miro API）

首次安裝需要網路下載 Python 依賴。

## 相關資源

- [Model Context Protocol 官方文件](https://modelcontextprotocol.io/)
- [MCP Python SDK](https://github.com/modelcontextprotocol/python-sdk)
- [Claude Code 文件](https://docs.claude.com/claude-code)
- [Python 官方網站](https://www.python.org/)

## 下一步

1. 確保已完成所有安裝步驟
2. 重啟 Claude Code
3. 在對話中測試：「請列出所有可用的 usecase」
4. 如果成功，MCP server 就設定完成了！

如有任何問題，請參考「疑難排解」章節或查看 Claude Code 的輸出日誌。
