package com.alvo.check.concreete;

import com.alvo.check.CheckResult;
import com.alvo.check.PerformanceCheck;
import com.alvo.http.AuthorizationServer;
import com.alvo.http.HttpRequestExecutor;
import com.alvo.infrastructure.PropertiesLoader;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class LatencyCheck implements PerformanceCheck {

  private static final Logger LOGGER = LoggerFactory.getLogger(LatencyCheck.class);

  private static final String ACCESS_TOKEN_URL = PropertiesLoader.getProperty("auth.token.url");
  private static final String CLIENT_ID = PropertiesLoader.getProperty("auth.username");
  private static final String CLIENT_SECRET = PropertiesLoader.getProperty("auth.usersecret");

  private final StopWatch stopWatch;
  private final CloseableHttpClient client;
  private final AuthorizationServer authorizationServer;
  private final HttpRequestExecutor requestExecutor;

  public LatencyCheck() {
    this.stopWatch = new StopWatch();
    this.client = HttpClients.createDefault();
    this.authorizationServer = new AuthorizationServer(ACCESS_TOKEN_URL, CLIENT_ID, CLIENT_SECRET, client);
    this.requestExecutor = new HttpRequestExecutor(client, authorizationServer);
  }

  @Override
  public Optional<CheckResult> execute(String url) {
    try {
      requestExecutor.initAccessTokenIfNeeded();
      return Optional.of(checkLatency(new HttpGet(url)));
    } catch (IOException e) {
      LOGGER.error("problem with executing latency test for URL: [{}] message: [{}]", url, e.getMessage());
    }
    return Optional.empty();
  }

  private CheckResult checkLatency(HttpRequestBase request) throws IOException {
    stopWatch.reset();
    stopWatch.start();
    final CloseableHttpResponse response = requestExecutor.execute(request);
    EntityUtils.consume(response.getEntity());
    stopWatch.stop();

    final boolean requestSuccessful = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    return new LatencyCheckResult(stopWatch.getTime(), requestSuccessful);
  }
}
