package com.alvo.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * Simple http requesting service,
 * which uses provided authorization server to authorize requests.
 */
public class HttpRequestExecutor {

  private final CloseableHttpClient httpClient;
  private final ObjectMapper jacksonObjectMapper;
  private final AuthorizationServer authServer;
  private ClientCredentialsResponse token;

  public HttpRequestExecutor(CloseableHttpClient httpClient, AuthorizationServer authServer) {
    this.httpClient = httpClient;
    this.authServer = authServer;
    this.jacksonObjectMapper = new ObjectMapper();
  }

  /**
   * Obtain authorization token and execute HTTP request with mapping the response according to the type, <br/>
   * carried with <code>TypeReference</code> parameter.
   * @param request desired http request base to be executed
   * @param typeReference type information carrier for response mapping.
   * @param <T> target type for mapping request result.
   * @return mapped response according to type parameter.
   * @throws IOException
   */
  public <T> T execute(HttpRequestBase request, TypeReference<T> typeReference) throws IOException {
    final String authorizationHeaderValue = String.format("Bearer %s", obtainAccessToken());
    request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);

    try (CloseableHttpResponse response = httpClient.execute(request)) {
      StatusLine statusLine = response.getStatusLine();
      if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
        throw new IOException("fetched with status " + statusLine);
      }

      return jacksonObjectMapper.readValue(response.getEntity().getContent(), typeReference);
    }
  }

  /**
   * Obtain authorization token and execute HTTP request.
   * @param request desired http request base to be executed
   * @return http response.
   * @throws IOException
   */
  public CloseableHttpResponse execute(HttpRequestBase request) throws IOException {
    final String authorizationHeaderValue = String.format("Bearer %s", obtainAccessToken());
    request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
    try (CloseableHttpResponse response = httpClient.execute(request)) {
      return response;
    }
  }

  /**
   * Obtain and cache access token.
   * Useful to remove token acquisition latency from the request.
   * @throws IOException
   */
  public void initAccessTokenIfNeeded() throws IOException {
    if (token == null || token.hasExpired()) {
      token = authServer.auth();
    }
  }

  private String obtainAccessToken() throws IOException {
    initAccessTokenIfNeeded();
    return token.getAccessToken();
  }
}
