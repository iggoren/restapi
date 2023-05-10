//package com.example.restapi.controller;
//
//
//import com.example.restapi.service.YandexCalendarService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("api/oauth")
//public class YandexCalendarRestController {
//    private final YandexCalendarService calendarService;
//
//    public YandexCalendarRestController(YandexCalendarService calendarService) {
//        this.calendarService = calendarService;
//    }
//
//    @GetMapping("/auth-url")
//    public String getYandexAuthUrl() {
//        return calendarService.getAuthorizationUrl();
//    }
//
//    @GetMapping("/calendars")
//    public ResponseEntity<List<Calendar>> getCalendars(@RequestParam(name = "token") String token) {
//        List<Calendar> calendars = calendarService.getCalendars(token);
//        return ResponseEntity.ok(calendars);
//    }
//
//}
