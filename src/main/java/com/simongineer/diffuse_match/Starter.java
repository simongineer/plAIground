package com.simongineer.diffuse_match;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.Prompt;
import com.simongineer.diffuse_match.service.StableDiffusionConnector;
import com.simongineer.diffuse_match.utils.Generator;
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
        Prompt prompt = Generator.generatePrompt();
        Prompt clone = Generator.generatePrompt();
        clone.setPrompt(prompt.getPrompt());

        CompletableFuture<byte[]> futureImage = StableDiffusionConnector.generateTxt2ImgAsync(prompt);
        try {
            Local.saveGeneratedImage(futureImage.get(), prompt);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<byte[]> futureImage2 = StableDiffusionConnector.generateTxt2ImgAsync(clone);
        try {
            Local.saveGeneratedImage(futureImage2.get(), clone);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
