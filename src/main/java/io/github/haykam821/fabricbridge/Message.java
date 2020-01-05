package io.github.haykam821.fabricbridge;

import net.minecraft.client.MinecraftClient;

import net.minecraft.text.LiteralText;
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

	Text getLiteralText() {
		// Prefix
		LiteralText prefixText = new LiteralText("§9§lDISCORD");
		LiteralText hoverString = new LiteralText(
			((gateway != null && gateway.length() > 0) ? "§6Gateway: §7" + gateway : "") +
			((protocol != null && protocol.length() > 0) ? "\n§6Protocol: §7" + protocol : "") +
			((channel != null && channel.length() > 0)? "\n§6Channel: §7" + channel : "")
		);
		Style style = new Style();
		style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverString));
		prefixText.setStyle(style);

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