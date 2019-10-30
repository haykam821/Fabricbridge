package io.github.haykam821.fabricbridge;

import java.util.HashMap;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import io.github.haykam821.fabricbridge.config.ModConfig;

public class Commands implements ClientCommandPlugin {
	@Override
	public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
		dispatcher
			.register(ArgumentBuilders.literal("fb")
			.then(ArgumentBuilders.argument("message", StringArgumentType.greedyString())
				.executes(context -> {
						Executors.newSingleThreadExecutor().submit(() -> {
							try {
								ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
								
								Gson gson = new Gson();
								HashMap<String, String> obj = new HashMap<String, String>();
								obj.put("gateway", config.gateway);
								obj.put("username", (config.username != null && config.username.length() > 0) ? config.username : MinecraftClient.getInstance().player.getName().asString());
								obj.put("text", StringArgumentType.getString(context, "message"));
								String payload = gson.toJson(obj);
								StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

								RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(1000).build();

								HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
								HttpPost request = new HttpPost("http://" + config.apiHost + "/api/message");
								request.setEntity(entity);

								if (config.token != null && config.token.length() > 0) {
									request.addHeader("Authorization", "Bearer " + config.token);
								}

								httpClient.execute(request);
								MinecraftClient.getInstance().player.sendMessage(new LiteralText("§9§lDISCORD §e" + obj.get("username") + " §r§f" + obj.get("text")));
							} catch (Exception err) {
								System.out.println(err);
								context.getSource().sendError(new LiteralText("Could not send message."));
							}
						});
						return 1;
				})
		));
	}
}