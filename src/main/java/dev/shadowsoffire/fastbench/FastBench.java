package dev.shadowsoffire.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.shadowsoffire.fastbench.net.RecipePayload;
import dev.shadowsoffire.placebo.config.Configuration;
import dev.shadowsoffire.placebo.events.ResourceReloadEvent;
import dev.shadowsoffire.placebo.network.PayloadHelper;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(FastBench.MODID)
public class FastBench {

    public static final String MODID = "fastbench";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static boolean removeBookButton = true;
    public static boolean disableToolTip = false;
    public static int gridUpdateInterval = 1;

    public FastBench(IEventBus bus) {
        bus.register(this);
        NeoForge.EVENT_BUS.addListener(this::reloads);
        loadConfig();
    }

    private static void loadConfig() {
        Configuration c = new Configuration(MODID);
        removeBookButton = c.getBoolean("Remove Recipe Book Button", "general", true, "If the recipe book button is removed.");
        disableToolTip = c.getBoolean("Disable tooltip on crafting table", "general", false, "If the crafting table has a tooltip");
        gridUpdateInterval = c.getInt("Grid Update Interval", "general", 1, 1, 100, "The tick interval at which all pooled grid updates will be run. Duplicate updates within the interval will be squashed.");
        if (c.hasChanged()) c.save();
    }

    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent e) {
        PayloadHelper.registerPayload(new RecipePayload.Provider());
    }

    public void reloads(ResourceReloadEvent e) {
        FastBench.loadConfig();
    }

    public static Identifier loc(String string) {
        return Identifier.fromNamespaceAndPath(MODID, string);
    }

}
