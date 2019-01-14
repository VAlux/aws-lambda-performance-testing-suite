package com.alvo.http;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public final class HttpResponse {

  private int statusCode;
  private Map<String, String> headers;
  private String body;

  public HttpResponse() {
    this("");
  }

  public HttpResponse(String body) {
    this(HttpStatus.SC_OK, body);
  }

  public HttpResponse(int statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
    headers = new HashMap<>();
  }

  public HttpResponse(int statusCode, Map<String, String> headers, String body) {
    this.statusCode = statusCode;
    this.headers = headers;
    this.body = body;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}