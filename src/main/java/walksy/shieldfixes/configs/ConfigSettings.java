package walksy.shieldfixes.configs;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;
import walksy.shieldfixes.ShieldFixes;

import java.io.File;
import java.io.IOException;

public final class ConfigSettings {
	private transient File file;

	public boolean shieldSounds = true;
	public boolean shieldRendering = true;

	public float shieldSoundVolume = 1.0f;
	public boolean debug = false;

	private ConfigSettings() {}

	public static ConfigSettings load() {
		File file = new File(
			FabricLoader.getInstance().getConfigDir().toString(),
			ShieldFixes.MOD_ID + ".toml"
		);

		ConfigSettings config;
		if (file.exists()) {
			Toml configTOML = new Toml().read(file);
			config = configTOML.to(ConfigSettings.class);
			config.file = file;
		} else {
			config = new ConfigSettings();
			config.file = file;
			config.save();
		}
		return config;
	}

	public void save() {
		TomlWriter writer = new TomlWriter();
		try {
			writer.write(this, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
