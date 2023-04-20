package com.example.restapi.controller;


import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    private String clientId = "823444065671-s81c2u14s4bc5src79g91aqtmhgjh5ug.apps.googleusercontent.com";
    private String clientSecret = "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";
    private String callbackUrl = "http://localhost:8082/admin";



//        private static HttpTransport httpTransport;
//        private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        @GetMapping("oauth2/google/auth-url")
                public String getGoogleAuthUrl() {



            String authorizationUrl = service.getAuthorizationUrl();
            return "{\"authUrl\": \"" + authorizationUrl + "\"}";
        }

    @GetMapping("admin/oauth")
    public String getAuth() {




        return  "redirect:/admin";
    }
    OAuth20Service service = new ServiceBuilder(clientId)
            .apiSecret(clientSecret)
            .callback(callbackUrl)
            .defaultScope("https://www.googleapis.com/auth/calendar")
            .build(GoogleApi20.instance());


}