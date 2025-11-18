---
name: generate_code_with_usecase
description: 生成特定的usecase
arguments:
  - name: usecase_name
    description: 1. 生成usecase的名字
    required: true
    order: 1
  - name: file_path
    description: 2. 生成程式碼的檔案路徑
    required: true
    order: 2
---

請依照以下順序提供參數：
1. usecase_name: 要生成的usecase名稱
2. file_path: 程式碼檔案路徑

使用Event2AI-mcp-server 中的 get_usecase tool ，並在 {{file_path}} 生成此 {{usecase_name}} 的 production code
