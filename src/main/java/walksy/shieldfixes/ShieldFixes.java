package walksy.shieldfixes;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import walksy.shieldfixes.configs.ConfigSettings;

public class ShieldFixes implements ModInitializer {
	public static final String MOD_ID = "shieldfixes";

	public static ConfigSettings config = null;

	public static MinecraftClient mc;

	@Override
	public void onInitialize() {
		mc = MinecraftClient.getInstance();
		config = ConfigSettings.load();
	}
}

	

