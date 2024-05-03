package com.simongineer.diffuse_match.utils;

import java.util.Arrays;

import com.google.gson.Gson;
import com.simongineer.diffuse_match.beans.OllamaPrompt;
import com.simongineer.diffuse_match.beans.StableDiffusionPrompt;
import com.simongineer.diffuse_match.beans.category.Category;

public abstract class Generator {

    private static int randomizerCounter = 0;

    /**
     * Generates a random prompt for the Stable Diffusion txt2img API. Using all
     * {@link #Category()} enums availabe. Those categorie names are context logilly
     * bound to each other in a sentece return String to use within a prompt to
     * generate a good AI-picture.
     * 
     * @return a random prompt
     */
    private static String promptTokens() {
        String[] categories = randomizeEachCategory();

        return "A " + categories[0] + " with a " + categories[1] + " in front of a "
                + categories[2] + " next to a " + categories[3] + " at " + categories[4] + "{{" + ++randomizerCounter
                + "}}";
    }

    private static String[] randomizeEachCategory() {

        String[] categories = new String[5];
        categories[0] = Category.Landscape.values()[(int) (Math.random() * Category.Landscape.values().length)].name();
        categories[1] = Category.Animal.values()[(int) (Math.random() * Category.Animal.values().length)].name();
        categories[2] = Category.Building.values()[(int) (Math.random() * Category.Building.values().length)].name();
        categories[3] = Category.Vehicle.values()[(int) (Math.random() * Category.Vehicle.values().length)].name();
        categories[4] = Category.DayTime.values()[(int) (Math.random() * Category.DayTime.values().length)].name();

        System.out.println("Categories: " + Arrays.toString(categories));

        return categories;
    }

    /**
     * Generates a prompt object with reading the #JSON_REQUEST_SAMPLE_DATA and
     * overiding the prompt value with {@link #promptTokens()}.
     * 
     * @return
     * @see Generator#promptTokens()
     * @see Local#JSON_REQUEST_SAMPLE_DATA
     * @see Generator#promptTokens()
     */
    public static StableDiffusionPrompt generatePrompt() {
        StableDiffusionPrompt prompt = new Gson().fromJson(Local.JSON_STABLEDIFUSSION_REQUEST_TEMPLATE,
                StableDiffusionPrompt.class);
        prompt.setPrompt(Generator.promptTokens());
        System.err.println("StableDiffusionPrompt: " + prompt.getPrompt());
        return prompt;
    }

    public static OllamaPrompt generateOllamaPrompt() {
        OllamaPrompt prompt = new Gson().fromJson(Local.JSON_OLLAMA_REQUEST_TEMPLATE, OllamaPrompt.class);
        prompt.setPrompt(Generator.promptTokens());
        return prompt;
    }
}
