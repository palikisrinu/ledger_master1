package com.example.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.config.SheetsConfig;
import com.example.demo.dto.LedgerRequest;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleSheetsService {
	
	
	private static final String SHEET_ID = "1sqA_gK7edCdt_2twG4hBbGl6dKgA_y8PAjlmExyTXms";

	//insert ledger
	 public void insertLedger(LedgerRequest request,String sheetName) throws Exception {

	        Sheets sheets = SheetsConfig.getSheetsService();
//	    	String sheetName=request.getSheetName();

	        // 🔥 Step 1: Read existing ID column
	        ValueRange response = sheets.spreadsheets().values()
	                .get(SHEET_ID, sheetName + "!A:A")
	                .execute();

	        List<List<Object>> values = response.getValues();

	        int nextId = 1;

	        if (values != null && values.size() > 1) {
	            List<Object> lastRow = values.get(values.size() - 1);
	            nextId = Integer.parseInt(lastRow.get(0).toString()) + 1;
	        }

	        // 🔥 Step 2: Prepare new row
	        ValueRange body = new ValueRange()
	                .setValues(List.of(
	                        List.of(
	                                nextId,
	                                request.getDate(),
	                                request.getAmount(),
	                                request.getCommission(),
	                                request.getProfit(),
	                                request.getBalance(),
	                                request.getLoss(),
	                                request.getPhonepay(),
	                                request.getExpenses()
	                        )
	                ));

	        // 🔥 Step 3: Append row
	        sheets.spreadsheets().values()
	        		.append(SHEET_ID, sheetName + "!A:I", body)
	                .setValueInputOption("RAW")
	                .execute();
	        System.out.println("Inserted into sheet: " + sheetName);
	    }

    //fetching data from the google sheets
    public List<List<Object>> fetchData(String sheetName) throws Exception {

     Sheets sheets = SheetsConfig.getSheetsService();
  
    
        String range =  sheetName +"!A:I";
        System.out.println("Fetching from sheet: " + sheetName);
        System.out.println("Range: "+range);
        ValueRange response = sheets.spreadsheets().values()
                .get(SHEET_ID, range)
                .execute();
       
        if (response.getValues() == null) {
            return List.of();
        }

        return response.getValues();
    }

    //delete sheets from the google sheets 
    public void deleteRow(int id,String sheetName) throws Exception {

        Sheets sheets = SheetsConfig.getSheetsService();

        // 1️⃣ Read all rows including header
        ValueRange response = sheets.spreadsheets().values()
                .get(SHEET_ID, sheetName +"!A:I")
                .execute();

        List<List<Object>> rows = response.getValues();

        if (rows == null || rows.size() <= 1) {
            return; // no data
        }

        int rowIndexToDelete = -1;

        // 2️⃣ Start from index 1 (skip header)
        for (int i = 1; i < rows.size(); i++) {

            int rowId = Integer.parseInt(rows.get(i).get(0).toString());

            if (rowId == id) {
                rowIndexToDelete = i;
                break;
            }
            System.out.println(rowId);
       
        }

        if (rowIndexToDelete == -1) {
            return; // id not found
        }
        
        Spreadsheet spreadsheet = sheets.spreadsheets()
                .get(SHEET_ID)
                .execute();
        
        int realSheetId = spreadsheet.getSheets().stream()
                .filter(s -> s.getProperties().getTitle().equals(sheetName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sheet not found"))
                .getProperties()
                .getSheetId();
        		
        // 3️⃣ Prepare delete request
        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(new DimensionRange()
                        .setSheetId(realSheetId) // Usually 0 for first sheet
                        .setDimension("ROWS")
                        .setStartIndex(rowIndexToDelete)
                        .setEndIndex(rowIndexToDelete + 1)
                );

        BatchUpdateSpreadsheetRequest batchRequest =
                new BatchUpdateSpreadsheetRequest()
                        .setRequests(Arrays.asList(
                                new Request().setDeleteDimension(deleteRequest)
                        ));

        // 4️⃣ Execute deletion
        sheets.spreadsheets()
                .batchUpdate(SHEET_ID, batchRequest)
                .execute();
    }
    
    //create new google sheet
    public void createSheet(String sheetName) throws Exception {
    	Sheets sheets = SheetsConfig.getSheetsService();

        // 1️⃣ Create Sheet
        AddSheetRequest addSheetRequest = new AddSheetRequest()
                .setProperties(new SheetProperties().setTitle(sheetName));

        BatchUpdateSpreadsheetRequest batchUpdateRequest =
                new BatchUpdateSpreadsheetRequest()
                        .setRequests(List.of(
                                new Request().setAddSheet(addSheetRequest)
                        ));

        sheets.spreadsheets()
                .batchUpdate(SHEET_ID, batchUpdateRequest)
                .execute();

        // 2️⃣ Add Header Row Automatically
        List<Object> headers = List.of(
                "ID",
                "Date",
                "Collection",
                "Total Charges",
                "Payment",
                "BF",
                "Pending",
                "Phonepay",
                "Expenses"
        );

        ValueRange body = new ValueRange()
                .setValues(List.of(headers));

        sheets.spreadsheets().values()
                .update(SHEET_ID,
                        sheetName + "!A1:I1",
                        body)
                .setValueInputOption("RAW")
                .execute();
    }
    
    public List<String> listSheets() throws Exception {

        Sheets service = SheetsConfig.getSheetsService();

        Spreadsheet spreadsheet = service.spreadsheets()
                .get(SHEET_ID)
                .execute();

        return spreadsheet.getSheets()
                .stream()
                .map(sheet -> sheet.getProperties().getTitle())
                .toList();
    }
    


}
