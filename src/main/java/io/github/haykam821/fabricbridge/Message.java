package io.github.haykam821.fabricbridge;

import net.minecraft.client.MinecraftClient;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.text.HoverEvent;

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
	String protocol;
	String gateway;
	String channel;

	Message(String username, String text, String gateway, String protocol, String channel) {
		this.username = username;
		this.text = text;
		this.protocol = protocol;
		this.gateway = gateway;
		this.channel = channel;
	}

	Text getHoverRow(String value, String lang, boolean first) {
		// Prefix
		LiteralText prefixText = new LiteralText(first ? "§6" : "\n§6");

		// Key
		TranslatableText keyText = new TranslatableText(lang);

		// Value
		LiteralText valueText = new LiteralText(" §7" + value);

		// Merge three components together
		LiteralText fullText = new LiteralText("");
		return fullText.append(prefixText).append(keyText).append(valueText);
	}

	Text getHoverText() {
		// Merge all rows together
		LiteralText fullText = new LiteralText("");
		return fullText
			.append(getHoverRow(gateway, "fabricbridge.info.gateway", true))
			.append(getHoverRow(protocol, "fabricbridge.info.protocol", false))
			.append(getHoverRow(channel, "fabricbridge.info.channel", false));
	}

	Text getLiteralText() {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	
		// Prefix
		LiteralText prefixText = new LiteralText("§9§lDISCORD");
		if (config.hoverText) {
			Text hoverText = getHoverText();
			Style style = new Style();
			style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
			prefixText.setStyle(style);
		}

		// Username
		LiteralText usernameText = new  LiteralText(" §e" + username);

		// Text
		LiteralText textText = new LiteralText(" §r§f" + text);

		// Merge three components together
		LiteralText fullText = new LiteralText("");
		return fullText.append(prefixText).append(usernameText).append(textText);
	}

	void sendLiteralText() {
		MinecraftClient.getInstance().player.sendMessage(this.getLiteralText());
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