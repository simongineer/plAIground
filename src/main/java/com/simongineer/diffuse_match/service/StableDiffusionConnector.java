package com.simongineer.diffuse_match.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simongineer.diffuse_match.beans.Prompt;

import okhttp3.MediaType;
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
public abstract class StableDiffusionConnector {

    /**
     * Stable Diffusion API URL.
     */
    private static final String STABLE_DIFF_URL = "http://localhost:7860";
    /**
     * Stable Diffusion txt2img API path.
     */
    private static final String STABLE_DIFF_TXT2IMG_PATH = "/sdapi/v1/txt2img";
    /**
     * User agent for the HTTP request.
     */
    private static final String USER_AGENT = "Mozilla/5.0";
    /**
     * JSON media type for the request body.
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Parses specified prompt into a JSON object and sends it to the Stable
     * Diffusion API by executing an HTTP POST request. The response is parsed into
     * a JSON object and returned. Note that the response may contain multiple
     * images, yet just the first one is returned for now.
     * 
     * @param promptTxt2Img prompt to be parsed and sent to the API
     * @return the first image returned by the API
     */
    public static byte[] generateTxt2Img(Prompt promptTxt2Img) {
        JsonElement e = JsonParser.parseString(new Gson().toJson(promptTxt2Img));
        JsonObject payload = e.getAsJsonObject();

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20000, TimeUnit.SECONDS)
                .readTimeout(20000, TimeUnit.SECONDS).build();

        RequestBody requestBody = RequestBody.create(new Gson().toJson(payload), JSON);
        Request request = new Request.Builder().url(STABLE_DIFF_URL + STABLE_DIFF_TXT2IMG_PATH)
                .addHeader("User-Agent", USER_AGENT).post(requestBody).build();
        Response response = null;
        String responseBody;
        try {
            response = client.newCall(request).execute();
            responseBody = Objects.requireNonNull(response.body()).string();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            response.close();
        }
        JsonObject responseJson = new Gson().fromJson(responseBody, JsonObject.class);
        if (responseJson.has("error")) {
            System.out.println("error: " + responseJson);
        }

        JsonArray jsonArray = responseJson.getAsJsonArray("images");
        List<byte[]> imgList = new ArrayList<>();
        jsonArray.forEach(jsonElement -> {
            byte[] imageBytes = java.util.Base64.getDecoder().decode((jsonElement.getAsString()));
            imgList.add(imageBytes);
        });

        return imgList.get(0);
    }
}