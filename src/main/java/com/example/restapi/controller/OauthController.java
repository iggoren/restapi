package com.example.restapi.controller;


import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class OauthController {


    private String clientId = "823444065671-s81c2u14s4bc5src79g91aqtmhgjh5ug.apps.googleusercontent.com";
    private String clientSecret = "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";
    private String callbackUrl = "http://localhost:8082/callback";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar calendar;

    private final OAuth20Service service;

    public OauthController(OAuth20Service service) {
        this.service = service;
    }

    @GetMapping("oauth2/google/auth-url")
    public String getGoogleAuthUrl() {


        String authorizationUrl = service.getAuthorizationUrl();
        return "{\"authUrl\": \"" + authorizationUrl + "\"}";
    }






//    OAuth20Service service = new ServiceBuilder(clientId)
//            .apiSecret(clientSecret)
//            .callback(callbackUrl)
//            .defaultScope("https://www.googleapis.com/auth/calendar")
//            .build(GoogleApi20.instance());


}