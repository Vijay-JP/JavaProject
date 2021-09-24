package io.hasbro.samples;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

public class Util {

    public static void generateTableStructure(Map<String, ResultSet> aColumnMap){
        
        FileWriter writer = null; 
        try{
            writer = new FileWriter("D:/Users/A1029653/Downloads/output.csv");
            
            for(Map.Entry<String, ResultSet> es : aColumnMap.entrySet()){
                writer.write("Schema Name : "+es.getKey()+"\n\n");
                
                ResultSet rs = es.getValue();
                writer.write("TBL NAME, SCHEMA NAME, COLUMN NAME, DATATYPE, LENGTH\n");
                while(rs.next()) {
                   //System.out.println(rs.getString("DB_NAME")+", "+rs.getString("DB_PWD")+", "+rs.getString("DB_URL")+", "+rs.getString("DB_USER")+", "+rs.getString("JDBC_ID"));
                   writer.write(rs.getString("DB_NAME")+", "+rs.getString("DB_PWD")+", "+rs.getString("DB_URL")+", "+rs.getString("DB_USER")+", "+rs.getString("JDBC_ID"));
                }
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
    
}
