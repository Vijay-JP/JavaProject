package io.hasbro.samples;

import io.hasbro.util.TableGenerator;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDBConn{

    /*static final String jdbcClassName="oracle.jdbc.OracleDriver";
    static final String url="jdbc:oracle:thin:@l4sidd03:1526:QOXYGEN1";
    static final String user="Oxyusrt";
    static final String password="Cjs#h376";
    static final String schema = "OXYGEN";*/
    
    static final String jdbcClassName="oracle.jdbc.OracleDriver";
    static final String url="jdbc:oracle:thin:@l4dvipdb8171.bolt.alight.com:11857:plbu0220";
    static final String user="a0668627";
    static final String password="India@4567";
    static final String schema = "CL0220LRPU_CLIENT";
    
    public static void main(String[] args){
        
        List<String> tableNames = new ArrayList<String>();
        Connection connection = null;
        
        try {
            //Load class into memory
            Class.forName(jdbcClassName);
            //Establish connection
            connection = DriverManager.getConnection(url, user, password);

            Statement stmt = connection.createStatement();                                          
            FileWriter fw = new FileWriter("C:/Alight/OneDrive - Alight Solutions/Desktop/DBDetails.txt");
            System.out.println("**** Created JDBC Statement object");

            ResultSet rs = stmt.executeQuery("SELECT table_name, num_rows counter from DBA_TABLES WHERE owner = '"+schema+"'");
            
            while(rs.next()) {
                String val = rs.getString(1);
                tableNames.add(val);
                //System.out.println(val);
            }
            
            for(String tname : tableNames) {
                
                if(tname.equals("AUDIT_EVNT_CNFG"))
                    continue;
                
                rs = stmt.executeQuery("select distinct(column_name), DATA_TYPE from all_tab_cols where  table_name = '"+ tname +"'"); 
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
                String queryNM = "Select "+colName+ " from " + schema+"."+tname;
                 
                System.out.println("Table Name : "+tname);
                fw.write("Table Name : "+tname);
                //System.out.println(queryNM);
                
                //Part where condition is added to fetch only pepsi client data
                if(queryNM.contains("LINEAGE")){
                    try {
                        
                        queryNM = queryNM+" WHERE LINEAGE like '01698%'";
                        System.out.println(queryNM);
                        rs = stmt.executeQuery(queryNM); 
                        List<List<String>> rowsList = new ArrayList<>();
                        while(rs.next()) {
                                       
                              List<String> row = new ArrayList<>();
                              for(String x : headersList) {
                                  String val1 = rs.getString(x);
                                  //if(Util.isEmpty(val1)){
                                  if (val1 == null || val1.length() == 0)  {
                                      // System.out.println("line 76");
                                      val1 = "dummy";
                                  }
                                  row.add(val1);
                              }
                              rowsList.add(row);
                        }
                        
                        //System.out.println(headersList);
                       // System.out.println(rowsList);
                        
                        TableGenerator tableGenerator = new TableGenerator();
                         //System.out.println(tableGenerator.generateTable(headersList, rowsList));
                        fw.write(tableGenerator.generateTable(headersList, rowsList));
                                   
                     }catch(Exception ex) {
                         ex.printStackTrace();
                         System.out.println("ignoring table "+ tname );    
                     }
                }
             }
            
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
