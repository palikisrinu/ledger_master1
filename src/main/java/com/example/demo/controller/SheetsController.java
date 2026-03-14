package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LedgerRequest;
import com.example.demo.service.GoogleSheetsService;

@RestController
@RequestMapping("/ledger")
@CrossOrigin(origins = "http://localhost:5173")
public class SheetsController {
	 private final GoogleSheetsService googleSheetsService;

	    public SheetsController(GoogleSheetsService googleSheetsService) {
	        this.googleSheetsService = googleSheetsService;
	    }

	 @GetMapping("/")
    public String home(){
        return "Spring Boot backend is running on Render";
    }

	    @PostMapping("/add")
	    public String addLedger(@RequestBody LedgerRequest request, @RequestParam String sheetName) {
//	    	System.out.println("request details "+request);
//	    	System.out.println(" sheetname "+sheetName);
	        try {
	            googleSheetsService.insertLedger(request,sheetName);
	            return "Ledger Saved Successfully!";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error: " + e.getMessage();
	        }
	    }
	    
	    @GetMapping("/get-sheet")
	    public List<List<Object>> getSheet(@RequestParam(required = false) String sheetName) throws Exception {
	    	 if (sheetName == null || sheetName.equals("undefined")) {
	    	        sheetName = "Sheet1";  // default fallback
	    	    }
	        return googleSheetsService.fetchData(sheetName);
	    }
	    
	    @DeleteMapping("/delete/{id}")
	    public String deleteLedger(@PathVariable int id,  @RequestParam String sheetName) throws Exception {
	    	googleSheetsService.deleteRow(id,sheetName);
	        return "Deleted successfully";
	    }
	    
	    @PostMapping("/create-sheet")
	    public String createSheet(@RequestParam String sheetName) throws Exception {
//        String sheetName = body.get("sheetName");
	        googleSheetsService.createSheet(sheetName);
	        return "Sheet created successfully";
	    }
	    @GetMapping("/list-sheets")
	    public ResponseEntity<List<String>> listSheets() {
	        try {
	            List<String> sheets = googleSheetsService.listSheets();
	            return ResponseEntity.ok(sheets);
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError().build();
	        }
	    }
	   
	   


}
