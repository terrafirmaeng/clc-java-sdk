package com.centurylinkcloud.core.auth.domain;

import java.util.Date;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 * @author ilya.drabenia
 */
public class AuthToken {
    private final String value;
    private final Date receivedAt;

    public AuthToken(String value) {
        this.value = value;
        this.receivedAt = now();
    }

    public String getValue() {
        return value;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public boolean isExpired() {
        return now().getTime() - receivedAt.getTime() > DAYS.toMicros(10);
    }

    public String toHeaderString() {
        return "Bearer " + value;
    }

    private Date now() {
        return new Date();
    }
}