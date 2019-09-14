package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import shadows.fastbench.block.BlockFastBench;
import shadows.fastbench.book.DedRecipeBook;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.net.LastRecipeMessage;
import shadows.fastbench.proxy.BenchClientProxy;
import shadows.fastbench.proxy.BenchServerProxy;
import shadows.fastbench.proxy.IBenchProxy;
import shadows.placebo.config.Configuration;
import shadows.placebo.util.NetworkUtils;

@Mod(FastBench.MODID)
public class FastBench {

	public static final String MODID = "fastbench";
	public static final Logger LOG = LogManager.getLogger(MODID);

	public static final IBenchProxy PROXY;
	static {
		PROXY = DistExecutor.runForDist(() -> () -> new BenchClientProxy(), () -> () -> new BenchServerProxy());
	}

	//Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "channel"))
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
    //Formatter::on
	public static final DedRecipeBook SERVER_BOOK = new DedRecipeBook();

	public static boolean removeRecipeBook = true;

	@ObjectHolder("fastbench:fastbench")
	public static final ContainerType<ContainerFastBench> FAST_CRAFTING = null;

	public FastBench() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		Configuration c = new Configuration(MODID);
		removeRecipeBook = c.getBoolean("Remove Recipe Book", "general", true, "If the recipe book is removed from the game.  Server-enforced.");
		if (c.hasChanged()) c.save();
	}

	@SubscribeEvent
	public void preInit(FMLCommonSetupEvent e) {
		NetworkUtils.registerMessage(CHANNEL, 0, new LastRecipeMessage());
		if (removeRecipeBook) PROXY.registerButtonRemover();
	}

	@SubscribeEvent
	public void containers(Register<ContainerType<?>> e) {
		e.getRegistry().register(new ContainerType<>(ContainerFastBench::new).setRegistryName("fastbench"));
	}

	@SubscribeEvent
	public void blockBois(Register<Block> e) {
		Block b = new BlockFastBench().setRegistryName("minecraft", "crafting_table");
		e.getRegistry().register(b);
		ForgeRegistries.ITEMS.register(new BlockItem(b, new Item.Properties().group(ItemGroup.DECORATIONS)) {
			@Override
			public String getCreatorModId(ItemStack itemStack) {
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
