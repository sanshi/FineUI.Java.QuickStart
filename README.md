# FineUI.Java.QuickStart

**FineUI.Java 快速入门**：一个基于 **Spring Boot + JPA + H2** 的电影管理小应用（Movie CRUD），
演示如何用 **FineUI.Java（社区版）** 快速搭建带增删改查、搜索、分页排序、弹窗、登录、主题切换的后台页面。

它用 **Spring Data JPA + Hibernate**（Code First：实体注解即表结构）访问 **H2 嵌入式数据库**
（零安装、数据存本地文件、开箱即跑），页面用 FineUI.Java 的 `@FineUIPage` + Thymeleaf `f:` 标签编写。

主要页面：电影列表（`movie`）、新增（`movie-new`）、编辑（`movie-edit`）、登录（`login`）、
开始页（`hello`）、主题仓库（`themes`）、首页框架页（`index`）。

## 环境要求

| 依赖 | 说明 |
|------|------|
| JDK 17+ | 项目 `java.version` 为 17（用 JDK 21 编译亦可） |
| Maven 3.6+ | 本机安装的 Maven（用于构建运行与自动解析依赖） |
| fineui-java 社区版 | 见「依赖方式」：由 Maven Central 自动下载 |

装好 JDK 与 Maven 后，先验证环境就绪（两条命令都应正常输出版本号）：

```bash
java -version     # 应输出 17 或更高版本
mvn -version      # 应输出 Maven 版本，且其中 Java version 为 17+
```

> 若提示 `'mvn' 不是内部或外部命令` / `command not found`，说明 Maven 未加入系统 PATH；
> 把 Maven 安装目录下的 `bin` 加入 PATH（并确保 `JAVA_HOME` 指向 JDK）后重开终端即可。

> 无需安装数据库——H2 是嵌入式的，随依赖引入即可。

## 依赖方式

项目文件已声明从公共软件包仓库获取的 Maven 包 `com.fineui:fineui-java`（**社区版**，永久免费商用）。正常联网构建时，包管理器会自动还原依赖；仓库不包含 FineUI.Core.dll、FineUI.Pro.dll、fineui-java.jar，也不包含 FineUI 框架源码。

FineUI 前端运行时（`/F/FineUI.js`、CSS、主题、语言包）已内嵌在 jar 里，`GET /F/FineUI.js` 自动命中——**无需单独部署前端资源**。

页面默认开启严格脚本 CSP：只允许同源脚本文件和当前请求授权的模板脚本，不允许原生事件属性或字符串代码执行。`f:` 模板中的普通 `<script>` 由 FineUI.Java 自动加 nonce；升级 `fineui.version` 时须选用包含自动 nonce 处理器的版本。H2 控制台是开发工具，其页面不经过 FineUI 模板渲染。

## 构建

安装 JDK 17 与 Maven 后，在仓库根目录运行：

```bash
mvn package
```

## 运行

在仓库根目录启动：

```bash
mvn spring-boot:run
```

启动后浏览器打开 <http://localhost:8081/>（端口由 `src/main/resources/application.properties` 的 `server.port` 决定）。
首次启动会自动在 H2 里建 `movie` 表并灌入示例数据。

**不需要授权文件**：本仓库引用的是公共软件包仓库中的社区版，社区版不做授权校验，克隆下来就能直接跑。

## H2 数据库与 JPA 说明

### H2 简介

**H2** 是一个用 Java 写的嵌入式数据库，无需安装数据库服务，随 Maven 依赖引入即可用。
本项目用**文件模式**（数据存本地 `./data/moviedb.mv.db`，重启不丢），相当于 Java 版的“开箱即用本地库”。

启动后可访问 H2 Web 控制台查看/编辑数据：**http://localhost:8081/h2-console**
（连接串 `jdbc:h2:file:./data/moviedb`，用户名 `sa`，密码空）。

### 连接与 JPA 配置（`application.properties`）

```properties
spring.datasource.url=jdbc:h2:file:./data/moviedb;AUTO_SERVER=TRUE
spring.jpa.hibernate.ddl-auto=update          # 实体注解自动建表（Code First）
spring.jpa.defer-datasource-initialization=true  # data.sql 在建表之后执行
spring.sql.init.mode=always                   # 总是执行 data.sql（脚本自身幂等）
```

### Code First 与自动建表

用 **Spring Data JPA（Hibernate）** 的 Code First：先写实体类，Hibernate 据注解自动建表。

- 实体：[`Movie.java`](src/main/java/com/fineui/java/quickstart/model/Movie.java)
  （`@Entity` + `@Id @GeneratedValue` + `@Display`/`@NotBlank`/`@DisplayFormat`）
- 仓库：[`MovieRepository.java`](src/main/java/com/fineui/java/quickstart/repository/MovieRepository.java)
  （`extends JpaRepository`，自带增删改查 + 分页排序）
- `ddl-auto=update`：库/表不存在则按实体建，已存在只增量改、**不删已有数据**——不需要手动写建表 SQL、也不用迁移工具。

### 种子数据（`data.sql`）

[`data.sql`](src/main/resources/data.sql) 用“**仅当表空时插入**”的幂等写法灌入几条示例电影：
首次启动种子入库，之后启动跳过（既不重复、又保留你后来新增/编辑的数据）。

> 想换成 MySQL / PostgreSQL？JPA 让切换几乎零成本：改 `pom.xml` 的驱动依赖 + `application.properties` 的连接串即可，
> 业务代码一行不用改。生产环境建议用 Flyway / Liquibase 做版本化迁移。

## 常见问题

**启动报找不到 `com.fineui:fineui-java`？** 先检查 Maven 网络、代理与中央仓库镜像。

**页面样式/脚本 404（/F/FineUI.js 加载不到）？** 确认依赖已经成功解析（前端资源已内嵌其中，无需单独部署）。

**电影列表为空？** 确认 `spring.sql.init.mode=always` 已配置（H2 连接串带 `AUTO_SERVER=TRUE` 会被 Spring
判为“非嵌入式”而默认跳过 `data.sql`，用 `always` 强制执行）。

**如何重置数据？** 停应用后删除 `./data/moviedb.mv.db`，重启即重新建表 + 灌种子。

## 许可边界

本仓库中由合肥三生石上软件有限公司拥有著作权的示例或应用项目源代码采用 [MIT 许可证](LICENSE)。FineUI 各端框架源码、二进制软件包、内嵌的 FineUI.js 运行时以及 FineUI 名称、标识和商标不属于 MIT 授权范围，仍适用各自的商业或社区版许可。具体边界见 [NOTICE.md](NOTICE.md)。

## 参与贡献

请先阅读 `CONTRIBUTING.md`。安全问题请按 `SECURITY.md` 私下报告。
