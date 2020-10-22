# Demo SSM

### 写在前面

这个项目围绕客户信息接口基础的CRUD功能，扩展了一些服务（如用户信息Excel数据导入导出，http请求日志的记录，数据库操作日志的记录）。

写这个项目代码的过程，也是我对什么是更好的封装代码的探索过程。项目部分内容避免了冗长的传统写法，尝试使用泛型或者行为参数化来改进；但由于时间有限，这个项目仍有许多不足的地方。这些我认为的不足之处，会在文档的最后整理出来。

### 项目结构

#### 请求处理流程图

![image-20201022193442015](http://47.108.68.81/webank-demo/image-20201022193442015.png)

#### 项目结构图

![image-20201022194142942](http://47.108.68.81/webank-demo/image-20201022194142942.png)

### 启动步骤

1. 使用idea 用gradle项目的形式导入项目。
2. 在MySQL数据库中新建 demo_webank 数据库，执行 SQL 初始化文件 ./documents/DBScripts/demo_webank.sql
3. 进入 ./src/resources/application.yaml 中，将spring.datasource下的配置改为您自己数据库的配置
4. build gradle 项目，如果您在 build 过程中遇到问题，可能是gradle版本和项目使用的springboot版本有冲突，请尝试按照提示将gradle升级到 6.3 以上。
5. 启动springboot 项目。
6. 您可以通过访问 http://localhost:8080/swagger-ui.html ，快速了解项目暴露的接口，输入相应参数进行测试。

### 项目存在问题探讨

#### Response 在错误返回时产生raw type

 ![image-20201022192821142](http://47.108.68.81/webank-demo/image-20201022192821142.png)

Response 的泛型指定的是其 data 属性的类型，但在异常返回 Response.ofError(code, message)时，不会为data 设值，此时会产生raw type问题。

该问题不会产生报错，但在使用Response 的过程中，可能会由不清晰的泛型造成调用困难。

#### ExcelUtil中Consumer语意不清

![image-20201022201228884](http://47.108.68.81/webank-demo/image-20201022201228884.png)

这个方法将

1. 得到HttpServletRequest和HttpServletResponse对象
2. 创建excel文件和默认sheet 工作簿
3. 将元数据写入 Response Header。
4. 将Excel数据写入Response中。
5. 异常处理及流关闭

几个步骤封装起来。

而将第2步和第3步中间写什么数据到 Excel 中，怎么写，作为Consumer对象暴露给调用者。

但注释和参数名称不能很好地表达语义，调用者如果没有其它调用参考，可能需要查看源代码才能理解调用规则。

这个方法调用举例如下:

##### 只添加标题行

![image-20201022202037882](http://47.108.68.81/webank-demo/image-20201022202037882.png)

##### 添加标题行和数据行

![image-20201022202255367](http://47.108.68.81/webank-demo/image-20201022202255367.png)

该方法相邻的readExcelIntoObjects 也会有类似的问题。

#### ClassUtil拷贝对象需要判空

![image-20201022202503548](http://47.108.68.81/webank-demo/image-20201022202503548.png)

这个方法会生成 targetClass 对应的对象，并将 source 中的属性拷贝到 对象中。

方法本身是为了语意简单清晰地表达拷贝并生成这一行为，却因为接受时需要判空显得比较累赘。如果没有后面列表的拷贝，我会选择弃用这个方法。

根据源码，classs.newInstance() 这个方法，在 class 为 Class.class或 targetClass没有无参构造时，分别会抛出InstantiationException和IllegalAccessException。导致返回值为null。

#### HttpRequest使用UUID做主键

![image-20201022203447978](http://47.108.68.81/webank-demo/image-20201022203447978.png)

DataBaseOperatePO 选用 HttpRequestPO 的主键作为逻辑外键，但DataBaseOperatePO的保存时机往往在HttpRequestPO 之前。这里无法使用内置的自增主键来解决问题。作为一个可能产生大量数据的表，UUID虽然可以解决重复的问题，但也会为主键索引带来负担。之后可以选用其它轻量级的生成策略。



项目还会有其它不太优雅的实现方式，由于时间原因，这里暂时无法一一列举，感谢您的阅读。