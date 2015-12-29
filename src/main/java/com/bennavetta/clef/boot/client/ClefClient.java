package com.bennavetta.clef.boot.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ClefClient
{
    private static final String AUTHORIZE_URL = "https://clef.io/api/v1/authorize";
    private static final String INFO_URL = "https://clef.io/api/v1/info?access_token={0}";

    private final String appId;
    private final String appSecret;
    private final RestTemplate restTemplate;

    public ClefClient(String appId, String appSecret, RestTemplate restTemplate)
    {
        this.appId = appId;
        this.appSecret = appSecret;
        this.restTemplate = restTemplate;
    }

    public ClefClient(String appId, String appSecret)
    {
        this(appId, appSecret, new RestTemplate());
    }

    /**
     * Perform a login handshake.
     * @param code the OAuth code
     * @return the access token
     */
    public String handshake(String code) throws ClefClientException
    {
        MultiValueMap<String, String> handshake = new LinkedMultiValueMap<String, String>();
        handshake.add("code", code);
        handshake.add("app_id", appId);
        handshake.add("app_secret", appSecret);

        ResponseEntity<HandshakeResponse> responseEntity =
                restTemplate.postForEntity(AUTHORIZE_URL, handshake, HandshakeResponse.class);
        HandshakeResponse response = responseEntity.getBody();
        if (response.success)
        {
            return response.accessToken;
        }
        else
        {
            throw new ClefClientException(response.error);
        }
    }

    public UserInfo getUserInfo(String accessToken) throws ClefClientException
    {
        InfoResponse response = restTemplate.getForObject(INFO_URL, InfoResponse.class, accessToken);
        if (response.success)
        {
            return response.info;
        }
        else
        {
            throw new ClefClientException(response.error);
        }
    }

    private static class HandshakeResponse
    {
        @JsonProperty
        boolean success;

        @JsonProperty("access_token")
        String accessToken;

        @JsonProperty
        String error;
    }

    private static class InfoResponse
    {
        @JsonProperty
        boolean success;

        @JsonProperty
        UserInfo info;

        @JsonProperty
        String error;
    }
}
