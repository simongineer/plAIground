package com.simongineer.diffuse_match;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.Prompt;
import com.simongineer.diffuse_match.service.StableDiffusionConnector;
import com.simongineer.diffuse_match.utils.Local;

/**
 * This class is used to start the application.
 */
public class Starter {

    public Starter() {
        init();
    }

    /**
     * Initializes the application by creating a {@link Prompt} object by parsing
     * the JSON sample data and then generating an image from it.
     * 
     * @see Local#JSON_REQUEST_SAMPLE_DATA
     * @see StableDiffusionConnector#generateTxt2Img(Prompt)
     * @see Local#saveGeneratedImage(byte[])
     */
    private void init() {
        Prompt prompt = new Gson().fromJson(Local.JSON_REQUEST_SAMPLE_DATA, Prompt.class);
        System.err.println("prompt JSON: " + prompt.toJson());

        byte[] imageBytes = StableDiffusionConnector.generateTxt2Img(prompt);
        Local.saveGeneratedImage(imageBytes);
    }
}
