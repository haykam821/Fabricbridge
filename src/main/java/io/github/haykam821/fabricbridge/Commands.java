package io.github.haykam821.fabricbridge;

import java.util.concurrent.Executors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import io.github.haykam821.fabricbridge.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

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
								
								String username = (config.username != null && config.username.length() > 0) ? config.username : MinecraftClient.getInstance().player.getName().asString();
								String text = StringArgumentType.getString(context, "message");

								Message message = new Message(username, text, config.gateway, "fabricbridge", "minecraft");
								message.send();
								message.sendLiteralText();
							} catch (Exception err) {
								context.getSource().sendError(new TranslatableText("commands.fabricbridge.failed"));
							}
						});
						return 1;
				})
		));
	}
}