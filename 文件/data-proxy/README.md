
#### 核心技术 


依赖 | 版本
---|---
Netty NIO 
JDBC协议   
Spring Security OAuth2 认证

#### 模块说明
```lua
mdm
├── core -- 核心公共包
     ├── core-api -- API
     ├── core-common -- 通用
     ├── core-entry -- 殷勤
     ├── core-excute -- 命令执行
     ├── core-merge -- 合并结果
     ├── core-optimize -- 条件 分页  结果集 差异化处理
     ├── core-parse -- SQL解析
     ├── core-rewrite -- SQL重写
     ├── core-route -- 路由
├── data-proxy-backed-- 中间件后端（中间件与数据源通信）
├── data-proxy-bootstrap-- 中间件启动
├── data-proxy-common-- 公共部分（配置，异常，工具）
├── data-proxy-fronted-- 中间件前端（应用与中间件通信）
├── data-proxy-transport-- 传输协议
```
#### 启动说明
##### 1、安全认证在柯林布瑞大数据平台认证
##### 2、运行data-proxy-bootstrap 即可启动测试