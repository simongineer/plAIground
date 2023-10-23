package com.simongineer.service;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.Prompt;
import com.simongineer.diffuse_match.service.StableDiffusionConnector;
import com.simongineer.diffuse_match.utils.Local;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test class for the StableDiffusionConnector class.
 */
public class StableDiffusionConnectorTest {

    private static final Prompt prompt = new Gson().fromJson(Local.JSON_REQUEST_SAMPLE_DATA, Prompt.class);

    /**
     * Tests the generateTxt2Img method with a valid prompt.
     * 
     * Verifies that the method returns a non-null byte array with a length greater
     * than zero.
     */
    @Test
    public void testGenerateTxt2Img() {
        byte[] imageBytes = StableDiffusionConnector.generateTxt2Img(prompt);
        assertNotNull(imageBytes);
        assertTrue(imageBytes.length > 0);
    }
}