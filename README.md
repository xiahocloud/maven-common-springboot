# springboot项目脚手架 ( Stpringboot Project Archetype )
## 项目名（Project Name）maven-common-springboot
### 项目中框架及插件选用（编码统一使用UTF-8）
   安装
   
    下载项目后，进入项目根目录执行以下命令 
    1.    mvn clean install
    2.    mvn archetype:crawl
    3.    mvn archetype:update-local-catalog 
    4.    cd ../  
    5.    mvn archetype:generate
    选择名称为 maven-common-springboot的编号， 正常填写group，archetype
    和package就可以了
    
    注意：
    由于maven会自动使用velocity, 会与一些组件库或插件库发生冲突， 所以讲lib了plugins
    目录放到了 web-lib 目录中， 使用时需等生成项目后自行替换目录

**需要自己创建数据库， sql目录下的demo脚本可以测试是否可以连通数据库**
测试地址：（[http://localhost:10001/demos/demo?id=demo1](http://localhost:10001/demos/demo?id=demo1)）
#### 后端
1.  Mybatis
2.  PageHelper
3.  Freemarker 模板引擎
4.  slf4j 日志
5.  alibaba druid 数据库连接池 （[localhost:10001/druid/index.html](localhost:10001/druid/index.html)）
6.  消息转换使用FastJson
7.  easyexcel 表格导出插件
### 脚本
**bin目录下提供了两个脚本**

*需要修改一下 ${package} 和 ${artifactId} 为当前项目的对应内容。*
1.  打包后 windows  下， 
直接双击control.bat
2.  linux -> control.sh
./control.sh start
./control.sh stop
./constrol.sh restart
./control.sh .status
