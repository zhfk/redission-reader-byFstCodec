# redission-reader-byFstCodec
redsssion 读取 redis，序列化库为FstCodec

# 目前功能
  - 序列化框架 FstCodec 
  - 读取部分 redis 数据
  

# 目前所用框架
  - springboot 2.1.1 
  - thymeleaf 前端框架
  - layui 极简UI

# 部署
 - gradle bootJar
 - java -jar build/libs/redission-reader-0.0.1-SNAPSHOT.jar

# 访问 
  localhost:8080 
  

# TODO：
    redission 序列化的选择
    redis 查询结果分页优化
    支持复杂类型的数据结构
# 更新历史
### 2019-01-07
    - 利用框架layui对页面进行美化
### 2019-01-09
    - 添加完整的layui库依赖
    - 更新查询redis逻辑页面, 按钮状态更新