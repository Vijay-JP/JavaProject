package io.hasbro.util;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FileUtil
{

    public static void generateXcelFile(JsonArray rows, String opfile, List<String> columnList) throws Exception{
        
        Row row = null;
        Sheet sheet = null;

        Workbook workbook = new XSSFWorkbook();
        
        int excelRow = 0;
        
        sheet = workbook.createSheet("report");
        boolean isFirstRow = true;
        
        for (JsonElement recordObj : rows) {
            JsonObject record = recordObj.getAsJsonObject();
            //System.out.println(record);
            int column = 0;
            if (isFirstRow) {
                row = sheet.createRow(excelRow);
                for (Entry<String, JsonElement> cell : record.entrySet()) {
                    if(columnList.contains(cell.getKey())){
                        Cell excelCell = row.createCell(column);
                        excelCell.setCellValue(cell.getKey());
                        column++;
                    }
                }
                column = 0;
                excelRow++;
                isFirstRow = false;
            }
            row = sheet.createRow(excelRow);
            for (Entry<String, JsonElement> cell : record.entrySet()) {
                if(columnList.contains(cell.getKey())){
                    String value = cell.getValue().isJsonNull() ? "" : cell.getValue().getAsString();
                    row.createCell(column).setCellValue(value);
                    column++;
                }
            }
            excelRow++;
        }
        
        try (FileOutputStream outputStream = new FileOutputStream(opfile)) {
            workbook.write(outputStream);
        }
        finally{
            workbook.close();
        }
        
    }

}
