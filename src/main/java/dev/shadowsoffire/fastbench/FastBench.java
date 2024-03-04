package dev.shadowsoffire.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.shadowsoffire.fastbench.net.RecipeMessage;
import dev.shadowsoffire.placebo.config.Configuration;
import dev.shadowsoffire.placebo.network.PayloadHelper;
import dev.shadowsoffire.placebo.util.RunnableReloader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

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
        gridUpdateInterval = c.getInt("Grid Update Interval", "general", 2, 1, 100, "The tick interval at which all pooled grid updates will be run. Duplicate updates within the interval will be squashed.");
        if (c.hasChanged()) c.save();
    }

    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent e) {
        PayloadHelper.registerPayload(new RecipeMessage.Provider());
    }

    public void reloads(AddReloadListenerEvent e) {
        e.addListener(RunnableReloader.of(FastBench::loadConfig));
    }

    public static ResourceLocation loc(String string) {
        return new ResourceLocation(MODID, string);
    }

}
