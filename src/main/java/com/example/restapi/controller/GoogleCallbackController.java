package com.example.restapi.controller;

import com.example.restapi.service.UserService;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private String callbackUrl = "http://localhost:8082/callback";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar calendar;
    private final OAuth20Service service;
    private final UserService userService;

    public GoogleCallbackController(OAuth20Service service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public RedirectView handleGoogleCallback(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {

        String code = request.getParameter("code");

        if (code == null) {
            // Ошибка авторизации
            // ...
        }

        // Выполняем необходимые действия для получения access token
        OAuth2AccessToken accessToken = service.getAccessToken(code);
        // Создаем новый Credential объект
        Credential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setAccessToken(accessToken.getAccessToken())
                .setRefreshToken(accessToken.getRefreshToken());

        // Сохраняем credential в HttpSession
        request.getSession().setAttribute("credential", credential);

        return new RedirectView("/admin");
    }


    @GetMapping("/calendars")
    public ResponseEntity<List<CalendarListEntry>> getCalendars(HttpServletRequest request) throws IOException, GeneralSecurityException {
        // Получаем credential из HttpSession
        Credential credential = (Credential) request.getSession().getAttribute("credential");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // Создаем экземпляр Calendar API
        calendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName("testGoogleCalendar")
                .build();

        // Получаем список календарей
        CalendarList calendarList = calendar.calendarList().list().execute();
        List<CalendarListEntry> calendars = calendarList.getItems();


        return ResponseEntity.ok(calendars);
    }
    @PostMapping("/calendars")
    public ResponseEntity<String> saveCalendar(){
   //     userService.update();


        return ResponseEntity.ok().build();
    }



    @PostMapping("/calendars/{calendarId}/events")
    public ResponseEntity<Void> postEventToCalendar(@PathVariable String calendarId, @RequestBody Event event, HttpServletRequest request) throws IOException, GeneralSecurityException {
        // Получаем credential из HttpSession
        Credential credential = (Credential) request.getSession().getAttribute("credential");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // Создаем экземпляр Calendar API
        calendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName("testGoogleCalendar")
                .build();

        // Создаем новое событие и устанавливаем его параметры
        com.google.api.services.calendar.model.Event calendarEvent = new com.google.api.services.calendar.model.Event();
        calendarEvent.setSummary(event.getSummary());
        calendarEvent.setDescription(event.getDescription());


        DateTime startDateTime = new DateTime(event.getStart().getDateTime().toString());
        //DateTime startDateTime = new DateTime("2023-04-20T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("UTC");
        calendarEvent.setStart(start);

          DateTime endDateTime = new DateTime(event.getEnd().getDateTime().toString());
        //DateTime endDateTime = new DateTime("2023-04-20T10:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("UTC");
        calendarEvent.setEnd(end);

        // Отправляем событие в календарь
        calendar.events().insert(calendarId, calendarEvent).execute();

        return ResponseEntity.ok().build();
    }


}