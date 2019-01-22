
环境
1、java1.8
2、mysql 5.5+

数据库
1、先创建一个名为testmap的数据库。
2、再执行testmap.sql里的语句创建表。

修改配置文件
config/application.properties
把里面的数据库连接修改成你自己的数据库，用户名，密码
###########数据库读写分离配置################# 
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testmap?characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=123456

文件上传
修改config/application.properties里的upload_path，修改成你的地址，建议与项目放一起。下面是示例。
#上传    E:/Workspaces/ecworkspace/20171207/docs/docs_api/upload/
upload_path=E:/Workspaces/ecworkspace/testmap/dist/upload/

启动
运行start.bat

账号
普通账号：test test 
管理账号：admin admin

登陆页面：
http://127.0.0.1:8065/test/login.html
普通页面：
http://127.0.0.1:8065/test/index.html
管理页面
http://127.0.0.1:8065/test/admin.html


手机浏览器访问：
1、需要跟电脑在同一个局域网内。（如果部署到公网，请忽略此条）
2、把127.0.0.1，替换成部署的服务器ip。
3、手机需同意定位请求。