package com.simongineer.service;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.StableDiffusionPrompt;
import com.simongineer.diffuse_match.service.StableDiffusionConnector;
import com.simongineer.diffuse_match.utils.Local;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * JUnit 5 test class for the StableDiffusionConnector class.
 */
public class StableDiffusionConnectorTest {

    private static final StableDiffusionPrompt prompt = new Gson()
            .fromJson(Local.JSON_STABLEDIFUSSION_REQUEST_TEMPLATE, StableDiffusionPrompt.class);

    /**
     * Tests the generateTxt2Img method with a valid prompt.
     * 
     * Verifies that the method returns a non-null byte array with a length greater
     * than zero.
     */
    @Test
    public void testGenerateTxt2Img() {
        CompletableFuture<byte[]> futureImage = StableDiffusionConnector.generateTxt2ImgAsync(prompt);
        try {
            assertNotNull(futureImage.get());
            assertTrue(futureImage.get().length > 0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}