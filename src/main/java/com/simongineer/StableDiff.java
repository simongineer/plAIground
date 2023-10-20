package com.simongineer;

import com.google.gson.*;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StableDiff
{
	private static final String STABLE_DIFF_URL = "http://localhost:7860";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static RuntimeException runtimeException;

	public static List<String> generate(String fileFolder)
	{
		List<String> imgList = new ArrayList<>();

		JsonElement e = null;
		try {
			e = JsonParser.parseString(printFile(getFileFromResource("json_request_template.json")));
		}
		catch(URISyntaxException ex) {
			throw runtimeException;
		}
		JsonObject payload = e.getAsJsonObject();

		try {
			OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20000, TimeUnit.SECONDS).readTimeout(20000, TimeUnit.SECONDS).build();

			RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(payload));
			Request request = new Request.Builder().url(STABLE_DIFF_URL + "/sdapi/v1/txt2img").addHeader("User-Agent", USER_AGENT).post(requestBody).build();

			Response response = client.newCall(request).execute();
			String responseBody = Objects.requireNonNull(response.body()).string();
			response.close();

			JsonObject responseJson = new Gson().fromJson(responseBody, JsonObject.class);
			if(responseJson.has("error")) {
				System.out.println("error: " + responseJson);
			}

			JsonArray jsonArray = responseJson.getAsJsonArray("images");
			jsonArray.forEach(jsonElement -> {

				byte[] imageBytes = Base64.getDecoder().decode((jsonElement.getAsString()));
				RenderedImage image = null;
				try {
					image = ImageIO.read(new ByteArrayInputStream(imageBytes));
				}
				catch(IOException ex) {
					throw new RuntimeException(ex);
				}

				JsonObject pngPayload = new JsonObject();
				pngPayload.addProperty("image", "data:image/png;base64," + jsonElement.getAsString());

				RequestBody pngInfoRequestBody = RequestBody.create(JSON, new Gson().toJson(pngPayload));
				Request pngInfoRequest = new Request.Builder().url(STABLE_DIFF_URL + "/sdapi/v1/png-info").addHeader("User-Agent", USER_AGENT)
						.post(pngInfoRequestBody).build();

				Response pngInfoResponse = null;
				try {
					pngInfoResponse = client.newCall(pngInfoRequest).execute();
				}
				catch(IOException ex) {
					throw new RuntimeException(ex);
				}
				String pngInfoResponseBody = null;
				try {
					pngInfoResponseBody = pngInfoResponse.body().string();
				}
				catch(IOException ex) {
					throw new RuntimeException(ex);
				}
				pngInfoResponse.close();

				JsonObject pngInfoResponseJson = new Gson().fromJson(pngInfoResponseBody, JsonObject.class);
				String parameters = pngInfoResponseJson.get("info").getAsString();
				if(parameters != null && !parameters.isEmpty()) {
					BufferedImage pngImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
					pngImage.getGraphics().drawString(parameters, 0, 0);
					pngImage.flush();
					File pngFile = new File(fileFolder + "/" + new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date()) + ".png");
					try {
						ImageIO.write(image, "png", pngFile);
					}
					catch(IOException ex) {
						throw new RuntimeException(ex);
					}
					imgList.add(pngFile.getAbsolutePath());
				}
			});
		}
		catch(IOException ex) {
			System.out.println("generate: " + ex.getMessage());
		}

		return imgList;
	}
	private static String printFile(File file)
	{
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			lines.forEach(System.out::println);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return String.join("", lines);
	}

	private static File getFileFromResource(String fileName) throws URISyntaxException
	{
		ClassLoader classLoader = StableDiff.class.getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if(resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		}
		else {
			// failed if files have whitespaces or special characters
			//return new File(resource.getFile());

			return new File(resource.toURI());
		}
	}
}