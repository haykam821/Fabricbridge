package io.github.haykam821.fabricbridge;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import io.github.haykam821.fabricbridge.config.ModConfig;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.GsonConfigSerializer;

class Message {
	String username = "user";
	String text = "[msg]";
}

public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		Gson gson = new Gson();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("http://" + config.apiHost + "/api/messages");

		if (config.token != null && config.token.length() > 0) {
			request.addHeader("Authorization", "Bearer " + config.token);
		}

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					HttpResponse response = httpClient.execute(request);
					String responseString = new BasicResponseHandler().handleResponse(response);
					JsonParser parser = new JsonParser();
					JsonArray array = parser.parse(responseString).getAsJsonArray();

					for (JsonElement item : array) {
						Message message = gson.fromJson(item, Message.class);
						MinecraftClient.getInstance().player.sendMessage(new LiteralText("§9§lDISCORD §e" + message.username + " §r§f" + message.text));
					}
				} catch (Exception err) {
					if (err instanceof HttpHostConnectException) return;
					System.out.println(err);
				}
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
}
