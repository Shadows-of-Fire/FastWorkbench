package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.fastbench.block.BlockFastBench;
import shadows.fastbench.gui.Handler;
import shadows.fastbench.net.LastRecipeMessage;
import shadows.fastbench.proxy.BenchClientProxy;
import shadows.fastbench.proxy.BenchServerProxy;
import shadows.fastbench.proxy.IBenchProxy;

@Mod(FastBench.MODID)
public class FastBench {

	public static final String MODID = "fastbench";

	public static final Logger LOG = LogManager.getLogger(MODID);

	public static IBenchProxy PROXY;
	static {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> PROXY = new BenchClientProxy());
		DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> PROXY = new BenchServerProxy());
	}

	//Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "channel"))
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
    //Formatter::on

	public static boolean removeRecipeBook = true;
	public static boolean experimentalShiftCrafting = true;

	public FastBench() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		ctx.getModEventBus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> Handler::getClientGuiElement);
		CHANNEL.registerMessage(0, LastRecipeMessage.class, LastRecipeMessage::write, LastRecipeMessage::read, LastRecipeMessage::handle);
	}

	@SubscribeEvent
	public void preInit(FMLCommonSetupEvent e) {
		if (removeRecipeBook) PROXY.registerButtonRemover();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void blockBois(Register<Block> e) {
		Block b = new BlockFastBench().setRegistryName("minecraft", "crafting_table");
		e.getRegistry().register(b);
		ForgeRegistries.ITEMS.register(new ItemBlock(b, new Item.Properties()) {
			@Override
			public String getCreatorModId(net.minecraft.item.ItemStack itemStack) {
				return MODID;
			}
		}.setRegistryName(b.getRegistryName()));
	}

	@SubscribeEvent
	public void serverStartRemoval(FMLServerAboutToStartEvent e) {
		if (removeRecipeBook) PROXY.replacePlayerList(e.getServer());
	}

	@SubscribeEvent
	public void normalRemoval(EntityJoinWorldEvent e) {
		if (removeRecipeBook) PROXY.deleteBook(e.getEntity());
	}

}
