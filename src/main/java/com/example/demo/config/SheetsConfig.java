package com.example.demo.config;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Collections;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class SheetsConfig {

    public static Sheets getSheetsService() throws Exception {

        String json = System.getenv("GOOGLE_CREDENTIALS");

        InputStream stream = new ByteArrayInputStream(json.getBytes());

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(stream)
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
