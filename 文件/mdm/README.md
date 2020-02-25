
#### 核心依赖 


依赖 | 版本
---|---
Spring Boot |  2.1.5.RELEASE  
Spring Cloud | Greenwich.RELEASE   
Spring Security OAuth2 | 2.3.3
Mybatis Plus | 3.1.0
hutool | 4.5.10
Avue | 1.5.0
   


#### 模块说明
```lua
mdm
├── clinbrain-ui -- 前端工程[8080]
├── clinbrain-auth -- 授权服务提供[3000]
└── clinbrain-common -- 系统公共模块 
     ├── clinbrain-common-core -- 公共工具类核心包
     ├── clinbrain-common-log -- 日志服务
     └── clinbrain-common-security -- 安全工具类
├── clinbrain-config -- 配置中心[8888]
├── clinbrain-eureka -- 服务注册与发现[8761]
├── clinbrain-gateway -- Spring Cloud Gateway网关[9999]
└── clinbrain-upms -- 通用用户权限管理模块
     └── clinbrain-upms-api -- 通用用户权限管理系统公共api模块
     └── clinbrain-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── clinbrain-visual  -- 图形化模块 
     ├── clinbrain-monitor -- Spring Boot Admin监控 [5001]
     ├── clinbrain-zipkin -- 链路调用监控 [5002]
     └── clinbrain-codegen -- 图形化代码生成[5003]
	 
```
#### 启动说明
##### 1、clinbrain-auth  clinbrain-config clinbrain-eureka clinbrain-upms clinbrain-visual 已经部署至111服务器
##### 2、配置文件默认从111服务器获取，如需本地配置，请将配置注释
X  cloud:

X    config:

X      fail-fast: true

X      name: ${spring.application.name}

X      profile: ${spring.profiles.active}

x      discovery:

x        enabled: true

x        service-id: clinbrain-config

##### 3、本地只需要启动clinbrain-gateway（网关） 和 自己的微服务即可