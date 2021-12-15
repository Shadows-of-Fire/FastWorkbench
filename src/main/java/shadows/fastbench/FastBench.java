package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import shadows.fastbench.net.RecipeMessage;
import shadows.placebo.config.Configuration;
import shadows.placebo.network.MessageHelper;

@Mod(FastBench.MODID)
public class FastBench {

	public static final String MODID = "fastbench";
	public static final Logger LOG = LogManager.getLogger(MODID);

	//Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "channel"))
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .networkProtocolVersion(() -> "4.6.0")
            .simpleChannel();
    //Formatter::on

	public static boolean removeBookButton = true;

	public FastBench() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		Configuration c = new Configuration(MODID);
		removeBookButton = c.getBoolean("Remove Recipe Book Button", "general", true, "If the recipe book button is removed.");
		if (c.hasChanged()) c.save();
	}

	@SubscribeEvent
	public void preInit(FMLCommonSetupEvent e) {
		MessageHelper.registerMessage(CHANNEL, 0, new RecipeMessage());
	}

}
