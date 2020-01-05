package io.github.haykam821.fabricbridge;

import net.minecraft.text.LiteralText;

import java.io.IOException;
import java.util.HashMap;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import io.github.haykam821.fabricbridge.config.ModConfig;

class Message {
	String username;
	String text;

	Message(String username, String text) {
		this.username = username;
		this.text = text;
	}

	LiteralText getLiteralText() {
		return new LiteralText("§9§lDISCORD §e" + this.username + " §r§f" + this.text);
	}

	HttpResponse send() throws ClientProtocolException, IOException {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		Gson gson = new Gson();
		HashMap<String, String> obj = new HashMap<String, String>();
		obj.put("gateway", config.gateway);
		obj.put("username", username);
		obj.put("text", text);
		String payload = gson.toJson(obj);
		StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(1000).build();

		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost request = new HttpPost("http://" + config.apiHost + "/api/message");
		request.setEntity(entity);

		if (config.token != null && config.token.length() > 0) {
			request.addHeader("Authorization", "Bearer " + config.token);
		}

		return httpClient.execute(request);
	}
}