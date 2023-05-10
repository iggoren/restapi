package com.example.restapi.controller;

import com.example.restapi.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;

import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping("/api/oauth")
public class OAuthCallbackRestController {

    private Calendar calendar;
    private static final String APPLICATION_NAME = "testGoogleCalendar";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final OAuth20Service oauthService;

    private final UserService userService;

    public OAuthCallbackRestController(OAuth20Service oauthService, UserService userService) {
        this.oauthService = oauthService;
        this.userService = userService;
    }
   // префикс адресов  @RequestMapping("/api/oauth")
    //получение адреса для авторизации гугл
    @GetMapping("/google/auth-url")
    public String getGoogleAuthUrl() {
        return oauthService.getAuthorizationUrl();
    }

    //адрес коллбека от гугла
//    @GetMapping("/callback/google")
//    public RedirectView handleGoogleCallback(@RequestParam("code") String code) throws IOException, ExecutionException, InterruptedException {
//        oauthService.getAccessToken(code);
//        return new RedirectView("/admin");
//    }
    @GetMapping("/callback/google")
    public RedirectView handleGoogleCallback(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {

        String code = request.getParameter("code");

        if (code == null) {
            // Ошибка авторизации
            // ...
        }

        // Выполняем необходимые действия для получения access token
        OAuth2AccessToken accessToken =   oauthService.getAccessToken(code);
        // Создаем новый Credential объект
        Credential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(oauthService.getApiKey(), oauthService.getApiSecret())
                .build()
                .setAccessToken(accessToken.getAccessToken())
                .setRefreshToken(accessToken.getRefreshToken());

        // Сохраняем credential в HttpSession
        request.getSession().setAttribute("credential", credential);

        return new RedirectView("/admin");
    }

    //получаем список календарей
    @GetMapping("google/calendars")
    public ResponseEntity<List<CalendarListEntry>> getCalendars(@RequestHeader("Authorization") String authHeader) throws IOException, GeneralSecurityException {


        getCredential(authHeader);

        CalendarList calendarList = calendar.calendarList().list().execute();
        List<CalendarListEntry> calendars = calendarList.getItems();
        return ResponseEntity.ok(calendars);
    }

    //сохраняем Id-календаря в базу
    @PostMapping("google/calendarId")
    public ResponseEntity<String> saveCalendarId(@RequestParam("calendarId") String calendarId, @RequestHeader("Authorization") String authHeader) throws IOException, ExecutionException, InterruptedException {
        String oauthToken = authHeader.replace("Bearer ", "");
        OAuth2AccessToken accessToken = oauthService.getAccessToken(oauthToken);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo");
        oauthService.signRequest(accessToken, request);

        request.setPayload(String.valueOf(accessToken));
        Response response = oauthService.execute(request);
        //  JSONObject jsonResponse = new JSONObject(response.getBody());

        //  request.addBodyParameter(accessToken,request);
//        // Создаем запрос на получение информации о пользователе
//        // OAuth2AccessToken accessToken = oauthService.getAccessToken(oauthToken, oauthVerifier);
//        // String email = ((OAuth20Service) oauthService).gccessToken(authHeader);
        //  System.out.println(token);etApi().getEmail(accessToken);
        //        //   OAuth2AccessToken token = oauthService.getA
        return ResponseEntity.ok("Calendar Id saved successfully.");
    }



    //отправка события в календарь
    @PostMapping("google/calendar/{calendarId}/events")
    public ResponseEntity<Void> postEventToCalendar(@PathVariable String calendarId,
                                                    @RequestBody Event event,
                                                    @RequestHeader("Authorization") String authHeader) throws IOException, GeneralSecurityException {

        getCredential(authHeader);
        // Создаем новое событие и устанавливаем его параметры
        Event calendarEvent = new Event();
        calendarEvent.setSummary(event.getSummary());
        calendarEvent.setDescription(event.getDescription());

        EventDateTime start = new EventDateTime()
                .setDateTime(event.getStart().getDateTime());
        //.setTimeZone("UTC");
        calendarEvent.setStart(start);


        EventDateTime end = new EventDateTime()
                .setDateTime(event.getEnd().getDateTime());
        //  .setTimeZone("UTC");
        calendarEvent.setEnd(end);


        // Отправляем событие в календарь
        calendar.events().insert(calendarId, calendarEvent).execute();

        return ResponseEntity.ok().build();
    }

    private Credential getCredential(String authHeader) throws GeneralSecurityException, IOException {
        String accessToken = authHeader.replace("Bearer ", "");
        OAuth2AccessToken oauthToken = new OAuth2AccessToken(accessToken);
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
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
            calendar = new Calendar.Builder(
                    httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }


        return credential;
    }
}
