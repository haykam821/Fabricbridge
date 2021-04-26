package io.github.haykam821.fabricbridge.command;

import java.util.concurrent.Executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import io.github.haykam821.fabricbridge.Message;
import io.github.haykam821.fabricbridge.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class FabricbridgeCommand {
	public static void register() {
		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("fb")
			.then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
			.executes(FabricbridgeCommand::execute)));
	}

	private static int execute(CommandContext<FabricClientCommandSource> context) {
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
	}
}
