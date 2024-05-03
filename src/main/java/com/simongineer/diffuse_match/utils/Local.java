package com.simongineer.diffuse_match.utils;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.simongineer.diffuse_match.beans.StableDiffusionPrompt;

/**
 * This class is used to read local files.
 * 
 * @author simongineer
 */
public abstract class Local {
    /**
     * The path to the generated pictures.
     */
    public static final String GENERATED_PICS_PATH = "pics/";
    /**
     * The name of the JSON request template file.
     */
    public static String JSON_STABLEDIFUSSION_REQUEST_TEMPLATE = "json_stablediffusion_template.json";
    /**
     * The JSON Ollama request template file.
     */
    public static String JSON_OLLAMA_REQUEST_TEMPLATE = "json_ollama_template.json";

    static {
        // read the JSON request template files
        JSON_STABLEDIFUSSION_REQUEST_TEMPLATE = printFile(JSON_STABLEDIFUSSION_REQUEST_TEMPLATE);
        JSON_OLLAMA_REQUEST_TEMPLATE = printFile(JSON_OLLAMA_REQUEST_TEMPLATE);
        // check if the generated pictures folder exists, create one otherwise
        File picsFolder = new File(GENERATED_PICS_PATH);
        if (!picsFolder.exists()) {
            picsFolder.mkdir();
        }
    }

    /**
     * Reads a file from the resources folder and returns it as a String.
     * 
     * @param fileName The name of the file to read.
     * @return The file data as a String.
     */
    private static String printFile(String fileName) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(
                    new File(Local.class.getClassLoader().getResource(fileName).toURI()).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("File: " + fileName);
        lines.forEach(System.out::println);
        return String.join("", lines);
    }

    /**
     * Saves specified base64 image data of generative created picture to save as
     * PNG with timestamp as name pattern into {@link #GENERATED_PICS_PATH}
     * 
     * @param imageBytes The base64 image data to save.
     */
    public static void saveGeneratedImage(byte[] imageBytes, StableDiffusionPrompt prompt) {
        File pngFile = new File("pics/" + prompt.getPrompt() + "_"
                + new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date()) + ".png");
        RenderedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(image, "png", pngFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
