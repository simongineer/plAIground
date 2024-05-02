package com.simongineer.diffuse_match.beans;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OllamaPrompt extends Prompt {

    /**
     * The model to use for generating the output.
     */
    @SerializedName("model")
    private String model;
}
