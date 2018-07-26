-------------------------------
cd ../SpringRestDemo
git init
git remote add origin git@github.com:louising/SpringRestDemo.git
git checkout -b dev
-------------------------------


Spring MVC Demo: build RESTful web service  

Run
-------------------------------------
1) Start DB (H2 DB)
   c:\>java -jar "H:/lib/java/h2-1.4.197.jar"
   
   URL: jdbc:h2:tcp://localhost/~/H2DB-SpringRestDemo
   user: sa
   password: sa
   
2) jdbc.properties
//Config JDBC connection

3) src/main/resources/applicationContext.xml
//Config DataSource
<bean id="dataSource" ...>

//Config MyBatis scan base package
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    ...
    <property name="basePackage" value="com.zero.srd.dao" />
</bean>

4) SpringMVC-servlet.xml
//Config Spring component scan package
<context:component-scan base-package="com.zero.srd" />

5) src/main/webapp/WEB-INF/web.xml
Config Filter and Servlet if necessary

6) Start it in Tomcat
mvn package
Put the war in TOMCAT_HOME/webapps

  POST http://localhost:8080/SpringRestDemo/dummy/addUser BODY { userId: 101, userName: "Alice"}
DELETE http://localhost:8080/SpringRestDemo/dummy/del?dummyName=Alice001 BODY { "userId": 1, "userName": "Alice" }
   PUT http://localhost:8080/SpringRestDemo/dummy/upd?userId=102&userName=Alice02 BODY { "userId": 103, "userName": "Alice03" }  
   GET http://localhost:8080/SpringRestDemo/dummy/list
  POST http://localhost:8080/SpringRestDemo/dummy/page/3/2 BODY { "userId": "1", "userName": "Alice" } 
 
  POST http://localhost:8080/SpringRestDemo/dummy/uploadDoc BODY form-data
   GET http://localhost:8080/SpringRestDemo/dummy/downloadLog
   GET http://localhost:8080/SpringRestDemo/dummy/sysInfo
   
7) API docs
http://localhost:8080/SpringRestDemo/swagger-ui.html

Dev Note
-------------------------------------
1) Dao        //DummyDao.java, DummyDao.xml
2) Service    //DummyServiceImpl.java
3) Controller //DummyController.java

CronJob //ScheduledJob.java
