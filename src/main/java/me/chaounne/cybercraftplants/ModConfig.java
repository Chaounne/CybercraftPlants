package me.chaounne.cybercraftplants;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("CybercraftPlantsConfig");
    private static final String CONFIG_FILE = "cybercraftplants.properties";

    // Default
    public static int GROWTH_CHANCE = 2; // 1 chance sur 3 par random tick
    public static int MIN_SUGAR_CANE_DROP = 2;
    public static int MAX_SUGAR_CANE_DROP = 3;
    public static boolean ALLOW_BONEMEAL = false;
    public static boolean SHOW_GROWTH_PARTICLES = true;

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        if (!Files.exists(configPath)) {
            createDefaultConfig(configPath);
        }

        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(configPath));

            GROWTH_CHANCE = Integer.parseInt(props.getProperty("growth_chance", "3"));
            MIN_SUGAR_CANE_DROP = Integer.parseInt(props.getProperty("min_drop", "1"));
            MAX_SUGAR_CANE_DROP = Integer.parseInt(props.getProperty("max_drop", "2"));
            ALLOW_BONEMEAL = Boolean.parseBoolean(props.getProperty("allow_bonemeal", "false"));
            SHOW_GROWTH_PARTICLES = Boolean.parseBoolean(props.getProperty("show_particles", "true"));

            LOGGER.info("Configuration loaded successfully");
        } catch (IOException e) {
            LOGGER.error("Failed to load config file, using defaults", e);
        }
    }

    private static void createDefaultConfig(Path path) {
        Properties props = new Properties();
        props.setProperty("growth_chance", "3");
        props.setProperty("min_drop", "1");
        props.setProperty("max_drop", "2");
        props.setProperty("allow_bonemeal", "false");
        props.setProperty("show_particles", "true");

        try {
            Files.createDirectories(path.getParent());
            props.store(Files.newOutputStream(path),
                    "Sugarcane Pot Mod Configuration\n" +
                            "growth_chance: Chance de croissance (1 chance sur X) - défaut: 3\n" +
                            "min_drop: Nombre minimum de cannes récoltées - défaut: 1\n" +
                            "max_drop: Nombre maximum de cannes récoltées - défaut: 2\n" +
                            "allow_bonemeal: Permettre l'utilisation de poudre d'os - défaut: false\n" +
                            "show_particles: Afficher des particules lors de la croissance - défaut: true");
            LOGGER.info("Default configuration file created");
        } catch (IOException e) {
            LOGGER.error("Failed to create default config file", e);
        }
    }
}
