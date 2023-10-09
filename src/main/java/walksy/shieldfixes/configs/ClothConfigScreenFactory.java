package walksy.shieldfixes.configs;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import walksy.shieldfixes.ShieldFixes;

import static walksy.shieldfixes.util.Localization.localized;

public final class ClothConfigScreenFactory {
	static Screen genConfig(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(localized("config", "title"))
				.setSavingRunnable(ShieldFixes.config::save);
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		builder.getOrCreateCategory(localized("config", "category.general"))
				.addEntry(entryBuilder
						.startTextDescription(
								localized("config", "empty")
						)
						.build()
				)
				.addEntry(entryBuilder
						.startBooleanToggle(
								localized("config", "shield_sounds"),
								ShieldFixes.config.shieldSounds
						)
						.setSaveConsumer(value -> ShieldFixes.config.shieldSounds = value)
						.build()
				)
				.addEntry(entryBuilder
						.startBooleanToggle(
								localized("config", "shield_rendering"),
								ShieldFixes.config.shieldRendering
						)
						.setSaveConsumer(value -> ShieldFixes.config.shieldRendering = value)
						.build()
				)
				.addEntry(entryBuilder
						.startFloatField(
								localized("config", "shield_volume"),
								ShieldFixes.config.shieldSoundVolume
						)
						.setSaveConsumer(value -> ShieldFixes.config.shieldSoundVolume = value)
						.build()
				)
				.addEntry(entryBuilder
						.startBooleanToggle(
								localized("config", "shield_debug"),
								ShieldFixes.config.debug
						)
						.setSaveConsumer(value -> ShieldFixes.config.debug = value)
						.build()
				);
		return builder.build();
	}
}