
����
1��java1.8
2��mysql 5.5+

���ݿ�
1���ȴ���һ����Ϊtestmap�����ݿ⡣
2����ִ��testmap.sql�����䴴����

�޸������ļ�
config/application.properties
����������ݿ������޸ĳ����Լ������ݿ⣬�û���������
###########���ݿ��д��������################# 
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testmap?characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=123456

�ļ��ϴ�
�޸�config/application.properties���upload_path���޸ĳ���ĵ�ַ����������Ŀ��һ��������ʾ����
#�ϴ�    E:/Workspaces/ecworkspace/20171207/docs/docs_api/upload/
upload_path=E:/Workspaces/ecworkspace/testmap/dist/upload/

����
����start.bat

�˺�
��ͨ�˺ţ�test test 
�����˺ţ�admin admin

��½ҳ�棺
http://127.0.0.1:8065/test/login.html
��ͨҳ�棺
http://127.0.0.1:8065/test/index.html
����ҳ��
http://127.0.0.1:8065/test/admin.html


�ֻ���������ʣ�
1����Ҫ��������ͬһ���������ڡ���������𵽹���������Դ�����
2����127.0.0.1���滻�ɲ���ķ�����ip��
3���ֻ���ͬ�ⶨλ����