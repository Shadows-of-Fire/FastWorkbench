package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.shadowsoffire.placebo.config.Configuration;
import dev.shadowsoffire.placebo.network.MessageHelper;
import dev.shadowsoffire.placebo.util.RunnableReloader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import shadows.fastbench.net.RecipeMessage;

@Mod(FastBench.MODID)
public class FastBench {

    public static final String MODID = "fastbench";
    public static final Logger LOG = LogManager.getLogger(MODID);

    // Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
        .named(new ResourceLocation(MODID, "channel"))
        .clientAcceptedVersions(s -> true)
        .serverAcceptedVersions(s -> true)
        .networkProtocolVersion(() -> "4.6.0")
        .simpleChannel();
    // Formatter::on

    public static boolean removeBookButton = true;
    public static boolean disableToolTip = false;
    public static int gridUpdateInterval = 2;

    public FastBench() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.addListener(this::reloads);
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
        MessageHelper.registerMessage(CHANNEL, 0, new RecipeMessage.Provider());
    }

    public void reloads(AddReloadListenerEvent e) {
        e.addListener(RunnableReloader.of(FastBench::loadConfig));
    }

}
