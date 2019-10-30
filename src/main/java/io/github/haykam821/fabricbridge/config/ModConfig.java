package io.github.haykam821.fabricbridge.config;

import me.sargunvohra.mcmods.autoconfig1.ConfigData;
import me.sargunvohra.mcmods.autoconfig1.annotation.*;

@Config(name = "fabricbridge")
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip(count = 1)
	public String apiHost = "localhost:4242";

	@ConfigEntry.Gui.Tooltip(count = 1)
	public String token = "";

	@ConfigEntry.Gui.Tooltip(count = 1)
	public String gateway = "";

	@ConfigEntry.Gui.Tooltip(count = 2)
	public String username = "";
}
