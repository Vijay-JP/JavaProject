package io.hasbro.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DB2Schema {

    final static String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
    final static String url="jdbc:db2://lintdb6:3700/DBQ7";
    final static String user="@HASBROT";
    final static String password="T1X3S9Y2";
    static Map<String, ResultSet> columnMap = new HashMap<>();
    
    Connection connection = null;
    
    public static void main(String args[]) {
        
        Connection connection = null;
        
        try {
            //Load class into memory
            Class.forName(jdbcClassName);
            //Establish connection
            connection = DriverManager.getConnection(url, user, password);

            Statement stmt = connection.createStatement();                                          

            //Get table names of the schema
           /* ResultSet rs = stmt.executeQuery("select distinct sc.NAME, st.owner, sc.TBNAME, trim(sc.COLTYPE) COLTYPE, sc.LENGTH" +
            		" from sysibm.systables st join SYSIBM.SYSCOLUMNS sc on st.name=sc.tbname where owner = 'T1HASBRD'" +
            		"order by sc.tbname");*/
            
            ResultSet rs = stmt.executeQuery("select  DB_NAME, DB_PWD, DB_URL, DB_USER, JDBC_ID from T1HASBRD.JDBC_PRVD_DFN");
            
            String schemaName = "T1HASBRD";

            columnMap.put(schemaName, rs);
            
            Util.generateTableStructure(columnMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
