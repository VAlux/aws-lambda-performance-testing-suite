package com.meteogroup.http;

import com.amazonaws.util.json.Jackson;
import org.apache.http.HttpStatus;

public final class HttpResponseTranslator {
  private static final HttpResponse NOT_MODIFIED_RESPONSE =
      new HttpResponse(HttpStatus.SC_NOT_MODIFIED, null);

  private HttpResponseTranslator() { }

  public static HttpResponse from(PerformanceCheckResultResponse response) {
    if (response == null) {
      return NOT_MODIFIED_RESPONSE;
    } else {
      return new HttpResponse(HttpStatus.SC_OK, Jackson.toJsonString(response));
    }
  }
}
