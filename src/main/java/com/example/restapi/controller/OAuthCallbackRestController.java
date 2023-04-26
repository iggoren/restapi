package com.example.restapi.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api/oauth")
public class OAuthCallbackRestController {
    public com.google.api.services.calendar.Calendar calendar;
    private static final String APPLICATION_NAME = "testGoogleCalendar";

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final OAuth20Service oauthService;

    public OAuthCallbackRestController(OAuth20Service oauthService) {
        this.oauthService = oauthService;
    }


    @GetMapping("google/auth-url")
    public String getGoogleAuthUrl() {
        return oauthService.getAuthorizationUrl();
    }

    @GetMapping("callback/google")
    public ResponseEntity<Void> handleGoogleCallback(@RequestParam("code") String code) throws IOException, ExecutionException, InterruptedException {
        oauthService.getAccessToken(code);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("callback/calendars")
    public ResponseEntity<List<CalendarListEntry>> getCalendars(@RequestHeader("Authorization") String authHeader) throws IOException, GeneralSecurityException {

        String accessToken = authHeader.replace("Bearer ", "");
        OAuth2AccessToken oauthToken = new OAuth2AccessToken(accessToken);
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        String clientId = oauthService.getApiKey();
        String clientSecret = oauthService.getApiSecret();

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
                .build();
        credential.setAccessToken(oauthToken.getAccessToken());
        credential.setRefreshToken(oauthToken.getRefreshToken());

        if (calendar == null) {
            calendar = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        CalendarList calendarList = calendar.calendarList().list().execute();
        List<CalendarListEntry> calendars = calendarList.getItems();
        return ResponseEntity.ok(calendars);
    }
}
