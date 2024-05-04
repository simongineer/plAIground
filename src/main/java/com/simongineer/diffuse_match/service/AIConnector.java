package com.simongineer.diffuse_match.service;

import okhttp3.MediaType;

public abstract class AIConnector {
    /**
     * Timeout in milliseconds for establishing an HTTP connection.
     */
    public static final long HTTP_CONNECT_TIMEOUT_MS = 30L;
    /**
     * Timeout in milliseconds for reading an HTTP response.
     */
    public static final long HTTP_READ_TIMEOUT_MS = 20000L;
    /**
     * User agent for the HTTP request.
     */
    public static final String USER_AGENT = "Mozilla/5.0";
    /**
     * JSON media type for the request body.
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
