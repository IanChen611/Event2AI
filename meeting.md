## 2025.11.28(五)

1. 我們做了什麼?
    - 可以成功讓 AI 透過 mcp 使用我們的工具，並使用我們自定義的 mcp server tool。
        - process_miro_board 到 Miro 上面把所有 Event 抓下來，並將 Event轉換成 json檔
        - list_usecases 列出所有被抓下來的 Usecase
        - get_usecase `arg:usecase名稱` 詳細列出特定 Usecase 的 json 內容，也可以使用 `arg:all` 列出所有 Usecase 的詳細內容
    - 當我們在使用 claude 的時候可以直接使用 `/` 呼叫特定我們製作的 command，執行相對應的功能
        - `/process_miro_board` 執行對應的 mcp server tool 
        - `/list_usecase` 執行對應的 mcp server tool
        - `/get_usecase <usecase_nam>` 執行對應的 mcp server tool
        - `/generate_code_with_usecase <usecase_name> <file_path>` 可以讀取到正確 Event 的 json 檔，並生成相關的 code

2. 接下來要做的事情
    - OK tag
    - event storm 的內容需要有x, y軸順序執行
    - 實驗 AI 能生出什麼樣的 code
    - RAG(檢索增強生成) 減少 AI 幻覺

3. 下次開會要做什麼：
    - 