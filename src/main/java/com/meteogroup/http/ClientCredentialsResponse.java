package com.meteogroup.http;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientCredentialsResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonIgnore
  private ZonedDateTime expiredAt;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public ZonedDateTime getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(ZonedDateTime expiredAt) {
    this.expiredAt = expiredAt;
  }

  @JsonSetter("expires_in")
  public void setExpiresIn(long expiresIn) {
    expiredAt = ZonedDateTime.now(ZoneId.of("UTC"))
        .plusSeconds(expiresIn);
  }

  public boolean hasExpired() {
    return expiredAt != null && expiredAt.isBefore(ZonedDateTime.now(ZoneId.of("UTC")));
  }
}
