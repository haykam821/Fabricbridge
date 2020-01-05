package io.github.haykam821.fabricbridge.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.*;

@Config(name = "fabricbridge")
@Config.Gui.Background("minecraft:textures/block/light_blue_wool.png")
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip(count = 1)
	public String apiHost = "localhost:4242";

	@ConfigEntry.Gui.Tooltip(count = 1)
	public String token = "";

	@ConfigEntry.Gui.Tooltip(count = 1)
	public String gateway = "";

	@ConfigEntry.Gui.Tooltip(count = 2)
	public String username = "";

	@ConfigEntry.Gui.Tooltip(count = 2)
	public Boolean hoverText = true;
}
