package com.zero.srd.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SrdDemoMain {
    public static void main(String[] args) throws SQLException {
        //testJDBC();
        //System.out.printf("%1$tF %1$tT.%1$tL", System.currentTimeMillis());
    }

    protected static void testJDBC() throws SQLException {
        /*         
         Access in JDBC
             Connection conn = DriverManager.getConnection("jdbc:h2:~/H2DB-SpringRestDemo", "sa", "sa");
         Access in both browser and JDBC: 
             1) java -jar "C:\Users\Administrator\.m2\repository\com\h2database\h2\1.4.197\h2-1.4.197.jar"
             2) By browser: http://192.168.1.103:8082  url: jdbc:h2:~/H2DB-SpringRestDemo 
                By JDBC: Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/H2DB-SpringRestDemo", "sa", "sa");          
         */
        Connection conn = DriverManager.getConnection("jdbc:h2:~/H2DB-SpringRestDemo", "sa", "sa");        
        Statement state = conn.createStatement();
        /*
        state.executeUpdate("DROP TABLE IF EXISTS T_USER");
        state.executeUpdate("CREATE TABLE T_USER("
                + "USER_ID INT PRIMARY KEY AUTO_INCREMENT, "
                + "USER_NAME VARCHAR(20), "
                + "LOGIN_NAME VARCHAR(20), "
                + "USER_EMAIL VARCHAR(50), "               
                
                + "CREATE_TIME TIMESTAMP,"
                + "CREATE_BY VARCHAR(20),"
                + "UPDATE_TIME DATE,"
                + "UPDATE_BY VARCHAR(20) );");        

        state.addBatch("insert into t_user(USER_NAME,CREATE_TIME) values('Alice', CURRENT_TIMESTAMP);");
        state.addBatch("insert into t_user(USER_NAME,CREATE_TIME) values('Bob', CURRENT_DATE);");
        state.addBatch("insert into t_user(USER_NAME,CREATE_TIME) values('Chris', CURRENT_TIMESTAMP);");
        state.executeBatch();
        */
        
        String str = "";        
        ResultSet rs = state.executeQuery("select * from t_user");
        while (rs.next()) {
            str += rs.getInt(1) + ", " + rs.getString(2) + "," + String.format("%1$tF %1$tT.%1$tL", rs.getTimestamp("CREATE_TIME")) + "\n";            
        }
        System.out.println(str);
    }
}
