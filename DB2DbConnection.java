package io.hasbro.samples;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB2DbConnection{

    static final String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
    //String url="jdbc:db2://DSNDGT1:3708/HALSDBT1";
    //String user="@HASBROT";
    //String password="T1X3S9Y2";
    
    static final String url="jdbc:db2://lintdb6:3700/DBQ7";
    static final String user="@HASBROT";
    static final String password="T1X3S9Y2";
    
    public static void main(String[] args) {
        
       List<String> tableNames = new ArrayList<String>();
       
       Connection connection = null;

        try {
            //Load class into memory
            Class.forName(jdbcClassName);
            connection = DriverManager.getConnection(url, user, password);

            Statement stmt = connection.createStatement();                                          
            FileWriter fw = new FileWriter("d:\\temp\\DB2ConfigDetails.txt");
           
            // Execute a query and generate a ResultSet instance
            ResultSet rs = stmt.executeQuery("select * from sysibm.systables where owner = 'T1HASBRD'and type = 'T'"); 
            //ResultSet rs = stmt.executeQuery("select tabname,tabschema from syscat.tables"); 
            
            while(rs.next()) {
                String val = rs.getString(1);
                tableNames.add(val);
                //System.out.println(val);
            }
            
            for(String tname : tableNames) {
               rs = stmt.executeQuery("Select distinct(name), ColType, Length from Sysibm.syscolumns where tbname = '"+ tname +"'"); 
                //ResultSet rs = stmt.executeQuery("select tabname,tabschema from syscat.tables"); 
                Map<String,String> columNames = new HashMap<String,String>();
                List<String> headersList = new ArrayList<>(); 
                
                while(rs.next()) {
                    String val1 = rs.getString(1);
                    String val2 = rs.getString(2);
                    columNames.put(val1, val2);
                    headersList.add(val1);
                }
                
                String colName = columNames.keySet().toString().replace("[", "").replace("]", "");
                String queryNM = "Select "         +colName+ " from " + "T1HASBRD."+tname;
                
                System.out.println("Table Name : "+tname);
                fw.write("Table Name : "+tname);
                System.out.println(queryNM);
                try {
                                rs = stmt.executeQuery(queryNM); 
                                List<List<String>> rowsList = new ArrayList<>();
                                while(rs.next()) {
                                              
                                              List<String> row = new ArrayList<>();
                                              for(String x : headersList) {
                                                              String val1 = rs.getString(x);
                                                              row.add(val1);
                                              }
                                              rowsList.add(row);
                                }
                              
                    //TableGen tableGenerator = new TableGen();
                    //System.out.println(tableGenerator.generateTable(headersList, rowsList));
                   // fw.write(tableGenerator.generateTable(headersList, rowsList));
                              
                }catch(Exception ex) {
                              System.out.println("ignoring table "+ tname );    
                }
            }
            
            
                          
            fw.close();
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
