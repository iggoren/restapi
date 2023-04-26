package com.example.restapi.controller;


import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {


    private final OAuth20Service service;

    public OauthController(OAuth20Service service) {
        this.service = service;
    }

    @GetMapping("oauth2/google/auth-url")
    public String getGoogleAuthUrl() {


        String authorizationUrl = service.getAuthorizationUrl();
        return "{\"authUrl\": \"" + authorizationUrl + "\"}";
    }


}