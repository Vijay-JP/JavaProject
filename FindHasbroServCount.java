package io.hasbro.logs;

import io.hasbro.util.FileUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//Class read hasbro usage file and give service count
public class FindHasbroServCount
{

    static final String ip_file = "L:/MY_HR/VJ/UpointHasbro/HasbroProdServers/L4DREPAP2669/usage-2021-09-16.log";
    static final String op_file = "C:/Alight/OneDrive - Alight Solutions/Desktop/Result.xlsx";
    public static List<String> columnList;
    
    static{
        columnList = new ArrayList<String>();
        columnList.add("Time");
        columnList.add("clientId");
        columnList.add("subjectId");
        columnList.add("svcName");
    }
    
    public static void main(String[] args)
    {
        try{
            JsonArray ja = readFile();
            getUniqueServiceName(ja, columnList);
            //getSvcCount(ja, columnList);
            //FileUtil.generateXcelFile(ja, op_file, columnList);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private static JsonArray readFile() throws Exception
    {
        // Open the file
        FileInputStream fstream = new FileInputStream(ip_file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
        JsonArray jsonArrayRecord = new JsonArray();

        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            if(strLine.trim().length()>0){
                
                //System.out.println (strLine.substring(51, strLine.length() - 1));
                String tempStr = strLine.substring(51, strLine.length() - 1).replace("=", "^");
                
                JsonObject js=new JsonObject();
                if(tempStr.contains("01698")){
                    
                    String [] items = tempStr.split("\\s*,\\s*");
                    List<String> container = Arrays.asList(items);
                    for(String d:container){
                        String[] vArr = d.split("\\^");
                        String key, value = null;
                        if(vArr.length == 2){
                            key = vArr[0];
                            value = vArr[1];
                        }else{
                            key = vArr[0];
                            value = "";
                        }
                        //System.out.println(key+"=="+value);
                        js.addProperty(key, value);
                    }
                    jsonArrayRecord.add(js);
                    
                }
                
            }
        }
        //Close the input stream
        fstream.close();
        return jsonArrayRecord;
    }

    private static void getUniqueServiceName(JsonArray rows, List<String> aColumnList){

        Set<String> uniqueSvcName = new HashSet<String>();
        
        for (JsonElement recordObj : rows) {
            JsonObject record = recordObj.getAsJsonObject();
            for (Entry<String, JsonElement> cell : record.entrySet()) {
                if("svcName".equals(cell.getKey())){
                    uniqueSvcName.add(cell.getValue().getAsString());
                }
            }
        }
        for(String value : uniqueSvcName){
            System.out.println(value);
        }
    }
    
    private static void getSvcCount(JsonArray rows, List<String> aColumnList){

        HashMap<String, Integer> svcCt = new HashMap<String, Integer>();
        
        for (JsonElement recordObj : rows) {
            JsonObject record = recordObj.getAsJsonObject();
            for (Entry<String, JsonElement> cell : record.entrySet()) {
                if("svcName".equals(cell.getKey())){
                    if(svcCt.containsKey(cell.getValue().getAsString())){
                        int count = svcCt.get(cell.getValue().getAsString());
                        count=count+1;
                        svcCt.remove(cell.getValue().getAsString());
                        svcCt.put(cell.getValue().getAsString(), count);
                    }else{
                        svcCt.put(cell.getValue().getAsString(), 1);
                    }
                }
            }
        }
        for (Map.Entry<String, Integer> entry : svcCt.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

}
