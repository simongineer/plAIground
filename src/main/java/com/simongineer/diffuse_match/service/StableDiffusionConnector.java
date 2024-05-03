package com.simongineer.diffuse_match.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simongineer.diffuse_match.beans.StableDiffusionPrompt;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class for connecting to the Stable Diffusion API and parsing the response.
 * Currently, only the txt2img API is supported. The API returns a JSON object
 * containing multiple images, yet just the first one is returned for now.
 * 
 */
public abstract class StableDiffusionConnector extends AIConnector {

    /**
     * Stable Diffusion API URL.
     */
    private static final String STABLE_DIFF_URL = "http://localhost:7860";
    /**
     * Stable Diffusion txt2img API path.
     */
    private static final String STABLE_DIFF_TXT2IMG_PATH = "/sdapi/v1/txt2img";

    /**
     * Generates an image asynchronously from a prompt using the Stable Diffusion
     * txt2img API.
     * 
     * @param prompt prompt for the txt2img API
     * @return image bytes or null if an error occurred
     * 
     * @see #parseResponse(Response)
     * @see #extractImage(JsonObject)
     */
    public static CompletableFuture<byte[]> generateTxt2ImgAsync(StableDiffusionPrompt prompt) {
        return CompletableFuture.supplyAsync(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(HTTP_CONNECT_TIMEOUT_MS, TimeUnit.SECONDS)
                    .readTimeout(HTTP_READ_TIMEOUT_MS, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(STABLE_DIFF_URL + STABLE_DIFF_TXT2IMG_PATH)
                    .addHeader("User-Agent", USER_AGENT)
                    .post(RequestBody.create(new Gson().toJson(prompt), JSON))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                JsonObject responseJson = parseResponse(response);
                if (responseJson.has("error"))
                    System.out.println("JSON-error: " + responseJson);
                return extractImage(responseJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static JsonObject parseResponse(Response response) throws IOException {
        String responseBody = Objects.requireNonNull(response.body()).string();
        return new Gson().fromJson(responseBody, JsonObject.class);
    }

    private static byte[] extractImage(JsonObject responseJson) {
        JsonArray jsonArray = responseJson.getAsJsonArray("images");
        List<byte[]> imgList = new ArrayList<>();
        jsonArray.forEach(jsonElement -> {
            byte[] imageBytes = java.util.Base64.getDecoder().decode((jsonElement.getAsString()));
            imgList.add(imageBytes);
        });
        return imgList.get(0);
    }
}