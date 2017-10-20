package com.meteogroup.http;

import com.amazonaws.util.json.Jackson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.stream.Collectors;

public final class AuthorizationServer {

  private final String accessTokenUrl;
  private final String clientId;
  private final String clientSecret;
  private final CloseableHttpClient httpClient;

  public AuthorizationServer(String accessTokenUrl,
                             String clientId,
                             String clientSecret,
                             CloseableHttpClient httpClient) {
    this.accessTokenUrl = accessTokenUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.httpClient = httpClient;
  }

  public ClientCredentialsResponse auth() throws IOException {
    HttpPost httpPost = new HttpPost(accessTokenUrl);
    httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " +
        new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes())));

    httpPost.setEntity(new StringEntity("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED));

    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
      HttpEntity entity = response.getEntity();

      return Jackson.fromJsonString(
          new BufferedReader(new InputStreamReader(entity.getContent()))
              .lines()
              .collect(Collectors.joining(System.lineSeparator())),
          ClientCredentialsResponse.class);
    }
  }
}