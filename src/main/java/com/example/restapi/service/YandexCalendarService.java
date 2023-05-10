//package com.example.restapi.service;
//import com.yandex.calendar.sdk.*;
//import com.yandex.calendar.sdk.auth.*;
//import com.yandex.calendar.sdk.event.*;
//import org.springframework.stereotype.Service;
//
//@Service
//public class YandexCalendarService {
//    private static final String CLIENT_ID = "your_client_id";
//    private static final String CLIENT_SECRET = "your_client_secret";
//    private static final String REDIRECT_URI = "http://localhost:8082/yandex/oauth2callback";
//
//    private static final String CALENDAR_API_BASE_URL = "https://calendar.yandex.ru/api/v2";
//    private static final String EVENT_URI = CALENDAR_API_BASE_URL + "/events";
//    public static void createEvent(Event event, String accessToken) throws YandexCalendarSdkException {
//        AuthorizationStorage storage = new SimpleAuthorizationStorage(accessToken);
//
//        ApiClient apiClient = new ApiClient(storage);
//        EventClient eventClient = new EventClient(apiClient);
//
//        EventCreateRequest eventRequest = new EventCreateRequest();
//        eventRequest.setTitle(event.getTitle());
//        eventRequest.setDescription(event.getDescription());
//        eventRequest.setAllDay(event.getAllDay());
//
//        EventDate start = new EventDate();
//        start.setDateTime(event.getStart().toString());
//        eventRequest.setStart(start);
//
//        EventDate end = new EventDate();
//        end.setDateTime(event.getEnd().toString());
//        eventRequest.setEnd(end);
//
//        EventResponse eventResponse = eventClient.create(EVENT_URI, eventRequest);
//        System.out.println("Event created successfully with ID: " + eventResponse.getId());
//    }
//
//    public static String getAuthorizationUrl() throws YandexCalendarSdkException {
//        AuthorizationCodeRequest authRequest = new AuthorizationCodeRequest.Builder()
//                .setClientId(CLIENT_ID)
//                .setRedirectUri(REDIRECT_URI)
//                .setScope("calendar")
//                .build();
//
//        AuthorizationCodeUrl authUrl = new AuthorizationCodeUrl(authRequest);
//
//        return authUrl.toString();
//    }
//
//    public static String getAccessToken(String code) throws YandexCalendarSdkException {
//        AuthorizationCodeRequest authRequest = new AuthorizationCodeRequest.Builder()
//                .setClientId(CLIENT_ID)
//                .setClientSecret(CLIENT_SECRET)
//                .setRedirectUri(REDIRECT_URI)
//                .setCode(code)
//                .build();
//
//        AuthorizationCodeFlow authFlow = new AuthorizationCodeFlow(authRequest);
//        AuthorizationCodeResponse authResponse = authFlow.run();
//
//        return authResponse.getAccessToken();
//    }
//
//
//}
