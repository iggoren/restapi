package com.example.restapi.controller;

import com.example.restapi.model.User;
import com.example.restapi.service.UserService;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/callback")
public class GoogleCallbackController {
    private String clientId = "823444065671-s81c2u14s4bc5src79g91aqtmhgjh5ug.apps.googleusercontent.com";
    private String clientSecret = "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";
    //  private String callbackUrl = "http://localhost:8082/callback";
    private HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private com.google.api.services.calendar.Calendar calendar;
    private final OAuth20Service service;
    private final UserService userService;

    public GoogleCallbackController(OAuth20Service service, UserService userService) throws GeneralSecurityException, IOException {
        this.service = service;
        this.userService = userService;
    }


    @GetMapping("/google/auth-url")
    public String getGoogleAuthUrl() {
        return service.getAuthorizationUrl();
    }

    @GetMapping
    public RedirectView handleGoogleCallback(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException, GeneralSecurityException {

        String code = request.getParameter("code");

        OAuth2AccessToken accessToken = service.getAccessToken(code);
        //httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
                .build();
        credential.setAccessToken(accessToken.getAccessToken());
        credential.setRefreshToken(accessToken.getRefreshToken());

        // Сохраняем credential в HttpSession
        request.getSession().setAttribute("credential", credential);

        return new RedirectView("/admin");
    }

    @GetMapping("/calendars")
    public ResponseEntity<List<CalendarListEntry>> getCalendars(HttpServletRequest request) throws IOException, GeneralSecurityException {
        // Получаем credential из HttpSession
        Credential credential = (Credential) request.getSession().getAttribute("credential");

        calendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName("testGoogleCalendar")
                .build();

        CalendarList calendarList = calendar.calendarList().list().execute();
        List<CalendarListEntry> calendars = calendarList.getItems();


        return ResponseEntity.ok(calendars);
    }


    @PostMapping("/saveCalendarId")
    public ResponseEntity<?> saveCalendarId(@RequestParam("calendarId") String calendarId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setLastName(calendarId);
        userService.update(user.getId(), user);

        return ResponseEntity.ok("Calendar Id saved successfully.");
    }



    @PostMapping("/calendars/events")
    public ResponseEntity<Void> postEventToCalendar(@RequestBody Event event, HttpServletRequest request) throws IOException, GeneralSecurityException {
        // Получаем credential из HttpSession
        Credential credential = (Credential) request.getSession().getAttribute("credential");

        calendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName("testGoogleCalendar")
                .build();

        // Создаем новое событие и устанавливаем его параметры
        Event calendarEvent = new Event();
        calendarEvent.setSummary(event.getSummary());
        calendarEvent.setDescription(event.getDescription());

        EventDateTime start = new EventDateTime()
                .setDateTime(event.getStart().getDateTime())
                .setTimeZone("UTC");
        calendarEvent.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(event.getEnd().getDateTime())
                .setTimeZone("UTC");
        calendarEvent.setEnd(end);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String calendarId = user.getLastName();
        calendar.events().insert(calendarId, calendarEvent).execute();

        return ResponseEntity.ok().build();
    }


}