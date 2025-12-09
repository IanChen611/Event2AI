## 2025.11.28(五)

1. 我們做了什麼?
    - 可以成功讓 AI 透過 mcp 使用我們的工具，並使用我們自定義的 mcp server tool。
        - process_miro_board 到 Miro 上面把所有 Usecase 抓下來，並將 Usecase json檔
        - list_usecases 列出所有被抓下來的 Usecase
        - get_usecase `arg:usecase名稱` 詳細列出特定 Usecase 的 json 內容，也可以使用 `arg:all` 列出所有 Usecase 的詳細內容
    - 當我們在使用 claude 的時候可以直接使用 `/` 呼叫特定我們製作的 command，執行相對應的功能
        - `/process_miro_board` 執行對應的 mcp server tool 
        - `/list_usecase` 執行對應的 mcp server tool
        - `/get_usecase <usecase_nam>` 執行對應的 mcp server tool
        

2. 接下來要做的事情
    - OK tag
    - event storm 的內容需要有x, y軸順序執行
    - 實驗 AI 能生出什麼樣的 code
        - `/generate_code_with_usecase <usecase_name> <file_path>` 可以讀取到正確 Event 的 json 檔，並生成相關的 code
        - 有結果之後，檢視、改善

3. 這次開會重點
    - 待複查
        - 報告表達問題
            1. 介紹要仔細->講usecase的故事
            2. demo後show程式要用講故事的方式
            3. 我們寫 meeting 的 md 在清單上應要有順序重要之分

        - 技術問題
            1. aggregate (已了解)
                - Board ->(event) Team
                - eventually consistency

        - 開發方式問題
            1. "dump"不要再出現 => 改成我們 Miro 上的名稱
            2. 四人一起 Review code ->
                - 確定要改的部分 MiroBoardDumpService (Final、run 改成 get)
            3. 專案一起做不要分開做
            
        - 額外注意事項
            1. 我們示範用的 policy -> Team 先做 ->(lead) Board CreateBoard
            2. json檔內的名稱是圖上翻譯下來的
                - 檢查 Teddy Example 內容與我們的差別是什麼
            3. usecase 名字是 command 
            4. 弄懂 EzKanban 的 context

    - 教授建議接下來可以做：
        - 可以做XY軸的分類 => Event Storming 的順序是「由左而右，由上而下」
        - 加入 context 後用 AI gen code，還不需要 RAG, 用足夠 context 就好
        - OK tag 可以用「檢查是否有變更」代替，沒變更的話就是OK

    # 進度不錯（？