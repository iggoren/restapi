package com.example.restapi.controller;


import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.stereotype.Controller;

@Controller
public class OauthController {

    private String clientId = "823444065671-s81c2u14s4bc5src79g91aqtmhgjh5ug.apps.googleusercontent.com";
    private String clientSecret = "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";
    private String callbackUrl = "http://localhost:8082/login/oauth2/code/google";


    OAuth20Service service = new ServiceBuilder(clientId)
            .apiSecret(clientSecret)
            .callback(callbackUrl)
            .build(GoogleApi20.instance());


}