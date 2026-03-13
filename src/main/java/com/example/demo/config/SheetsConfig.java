package com.example.demo.config;

import java.io.InputStream;
import java.util.Collections;

import org.springframework.core.io.ClassPathResource;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class SheetsConfig {
	
	 public static Sheets getSheetsService() throws Exception {

	        InputStream in = new ClassPathResource("ledger-app-487612-7d4f196f2ee3.json").getInputStream();

	        GoogleCredentials credentials = GoogleCredentials
	                .fromStream(in)
	                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

	        HttpCredentialsAdapter requestInitializer =
	                new HttpCredentialsAdapter(credentials);

	        return new Sheets.Builder(
	                GoogleNetHttpTransport.newTrustedTransport(),
	                GsonFactory.getDefaultInstance(),
	                requestInitializer)
	                .setApplicationName("Ledger App")
	                .build();
	    }
}
