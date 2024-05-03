package com.simongineer.diffuse_match.service;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.OllamaPrompt;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class OllamaConnector extends AIConnector {
    private static final String OLLAMA_URL = "http://localhost:11434";
    private static final String OLLAMA_API_PATH = "/api/generate";

    public static CompletableFuture<String> generateAsync(OllamaPrompt prompt) {
        return CompletableFuture.supplyAsync(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(HTTP_CONNECT_TIMEOUT_MS, TimeUnit.SECONDS)
                    .readTimeout(HTTP_READ_TIMEOUT_MS, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(OLLAMA_URL + OLLAMA_API_PATH)
                    .addHeader("User-Agent", USER_AGENT)
                    .post(RequestBody.create(new Gson().toJson(prompt), JSON))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}