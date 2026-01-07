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
    - event storm 的內容需要有x, y軸順序執行
    - 實驗 AI 能生出什麼樣的 code
        - `/generate_code_with_usecase <usecase_name> <file_path>` 可以讀取到正確 Event 的 json 檔，並生成相關的 code
        - 有結果之後，檢視、改善
    - OK tag

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
            3. usecase 名字是 command => Teddy Example中的也叫做 usecase
            4. 弄懂 EzKanban 的 context

    - 教授建議接下來可以做：
        - 可以做XY軸的分類 => Event Storming 的順序是「由左而右，由上而下」
        - 加入 context 後用 AI gen code，還不需要 RAG, 用足夠 context 就好
        - OK tag 可以用「檢查是否有變更」代替，沒變更的話就是OK

    # 進度不錯（？

## 預計2025-12-19(Teddy)
1. 關於 Event Stroming 的順序問題
2. 我們專題這個專案是否合適拿來當我們實驗範本
3. 關於 Input type 的定義，我們需要新增一個功能
    - 功能描述：miro 的 input 卡片中，對不同的 input 後面增加 `新功能要用`：`來限制type，例如：`：所限制的type)` 、 `[筆記]` 來對ai說明專案需求的type & note
4. 我們需要製作 UML 轉成 Json 文件的說明嗎？

我們的 json 缺少的部分
- aggregateId
- method
- repository
- output
- constructorPreconditions
- constructorPostconditions

## 2026.01.07 (三)
1. 關於 json 要改的東西 and 缺的東西
    - user 要改成 actor
    - input ： 新功能要用`：`來限制type，例如：`productId：string`代表 productId 的 type 要 string
    - aggregateId = aggregate + "Id"
    - domainEvent = XXXEvents.XXXAAAed
    - repository = `<aggregate>`Repository
    - output = CqrsOutput with aggregateId
    - 我們的 comments 要改成 domainModelNotes
    - method(目前沒有想到要怎麼做) ： if (usecase == 'CreateXXX') => method = "XXX consturctor"()
    - domainEvents、aggregates：可能有另外的便利貼來負責輸入這兩者的 attributes
2. 先後順序的表示方式
    - 先照著位置排序：最左上角為第一，先做完左側才可以做右側，上方下方沒特別順序之分
    - 實作方式：先利用 aggregate 分資料夾，判斷便條紙的 x, y 座標，
