package cc.carm.plugin.ultradepository.configuration.values;

import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.configuration.file.FileConfig;
import cc.carm.plugin.ultradepository.manager.ConfigManager;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigSound {


	FileConfig source;
	String configSection;

	Sound defaultValue;

	public ConfigSound(String configSection) {
		this(configSection, null);
	}

	public ConfigSound(String configSection, Sound defaultValue) {
		this(ConfigManager.getPluginConfig(), configSection, defaultValue);
	}

	public ConfigSound(FileConfig source, String configSection, Sound defaultValue) {
		this.source = source;
		this.configSection = configSection;
		this.defaultValue = defaultValue;
	}

	public FileConfiguration getConfiguration() {
		return this.source.getConfig();
	}

	public void set(Sound value, float volume) {
		getConfiguration().set(this.configSection, value.name() + ":" + volume);
		this.save();
	}

	public void set(Sound value, float volume, float pitch) {
		getConfiguration().set(this.configSection, value.name() + ":" + volume + ":" + pitch);
		this.save();
	}

	public void play(Player player) {
		Sound finalSound = defaultValue;
		float pitch = 1;
		float volume = 1;
		String soundString = getConfiguration().getString(this.configSection);
		if (soundString != null) {
			String[] args = soundString.contains(":") ? soundString.split(":") : new String[]{soundString};
			try {
				if (args.length >= 1) finalSound = Sound.valueOf(args[0]);
				if (args.length >= 2) volume = Float.parseFloat(args[1]);
				if (args.length >= 3) volume = Float.parseFloat(args[2]);
			} catch (Exception exception) {
				Main.log("声音 " + this.configSection + " 配置错误，不存在 " + soundString + " ，请检查。");
				Main.log("In " + this.configSection + " (" + soundString + ") doesn't match any sound name.");
			}
		}
		if (finalSound != null) {
			player.playSound(player.getLocation(), finalSound, volume, pitch);
		}

	}

	public void save() {
		this.source.save();
	}

}
