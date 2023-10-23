package com.simongineer.diffuse_match.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * This POJO class represents json-keys and values for StableDiffusion API.
 */
@Getter
@Setter
public class Prompt {
    /**
     * The prompt text to use for generating the output
     */
    private String prompt;

    /**
     * The negative prompt text to use for generating the output.
     */
    @SerializedName("negative_prompt")
    private String negativePrompt;

    /**
     * The number of steps to use for generating the output.
     */
    private int steps;

    /**
     * The CFG scale to use for generating the output.
     */
    @SerializedName("CFGScale")
    private int cfgScale;

    /**
     * The width of the output image.
     */
    private int width;

    /**
     * The height of the output image.
     */
    private int height;

    /**
     * Whether to send the output images in the response.
     */
    @SerializedName("send_images")
    private boolean sendImages;

    /**
     * The name of the sampler to use for generating the output.
     */
    @SerializedName("sampler_name")
    private String samplerName;

    /**
     * Whether to enable high-resolution mode for generating the output.
     */
    @SerializedName("enable_hr")
    private boolean enableHr;

    /**
     * The denoising strength to use for generating the output.
     */
    @SerializedName("denoising_strength")
    private double denoisingStrength;

    /**
     * The upscaler to use for generating high-resolution output.
     */
    @SerializedName("hr_upscaler")
    private String hrUpscaler;

    /**
     * The scale factor to use for generating high-resolution output.
     */
    @SerializedName("hr_scale")
    private int hrScale;

    /**
     * The number of steps to use for the second pass of high-resolution output
     * generation.
     */
    @SerializedName("hr_second_pass_steps")
    private int hrSecondPassSteps;

    /**
     * Returns a JSON string representation of this Prompt object.
     *
     * @return a JSON string representation of this Prompt object
     */
    public String toJson() {
        return new Gson().toJson(this);
    }
}
