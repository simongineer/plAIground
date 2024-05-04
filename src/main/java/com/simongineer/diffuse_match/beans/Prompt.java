package com.simongineer.diffuse_match.beans;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

/**
 * This POJO class represents json-keys and values for AI-APIs.
 */
@Getter
@Setter
public abstract class Prompt {
    /**
     * The prompt text to use for generating the output
     */
    private String prompt;

    /**
     * Returns a JSON string representation of this Prompt object.
     *
     * @return a JSON string representation of this Prompt object
     */
    public String toJson() {
        return new Gson().toJson(this);
    }
}
