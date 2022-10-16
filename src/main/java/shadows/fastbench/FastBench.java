package shadows.fastbench;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crafttweaker.CraftTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import shadows.fastbench.block.BlockFastBench;
import shadows.fastbench.book.DedRecipeBook;
import shadows.fastbench.gui.ClientContainerFastBench;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.Handler;
import shadows.fastbench.net.LastRecipeMessage;
import shadows.fastbench.proxy.IBenchProxy;

@Mod(modid = FastBench.MODID, name = FastBench.MODNAME, version = FastBench.VERSION)
public class FastBench {

	public static final String MODID = "fastbench";
	public static final String MODNAME = "FastWorkbench";
	public static final String VERSION = "1.7.4";

	public static final Logger LOG = LogManager.getLogger(MODID);

	@Instance
	public static FastBench INSTANCE;

	@SidedProxy(serverSide = "shadows.fastbench.proxy.BenchServerProxy", clientSide = "shadows.fastbench.proxy.BenchClientProxy")
	public static IBenchProxy PROXY;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static final DedRecipeBook SERVER_BOOK = new DedRecipeBook();

	public static boolean removeRecipeBook = true;
	public static boolean experimentalShiftCrafting = true;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new Handler());
		NETWORK.registerMessage(LastRecipeMessage.Handler.class, LastRecipeMessage.class, 0, Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(this);

		NBTTagCompound t = new NBTTagCompound();
		t.setString("ContainerClass", "shadows.fastbench.gui.ContainerFastBench");
		t.setString("AlignToGrid", "west");
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", t);

		Configuration c = new Configuration(e.getSuggestedConfigurationFile());
		removeRecipeBook = c.getBoolean("Disable Recipe Book", "crafting", true, "If the recipe book and all associated functionality are fully removed.");
		experimentalShiftCrafting = c.getBoolean("Experiemental Shift Crafting", "crafting", true, "If a testing variant of shift-click crafting is enabled.");
		if (c.hasChanged()) c.save();

		if (removeRecipeBook) PROXY.registerButtonRemover();

		if (Loader.isModLoaded("extrautils2")) temaWtf();
		if (Loader.isModLoaded("crafttweaker2")) disableCrtSearchTree();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		OreDictionary.registerOre("workbench", Blocks.CRAFTING_TABLE);
		OreDictionary.registerOre("craftingTableWood", Blocks.CRAFTING_TABLE);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void blockBois(Register<Block> e) {
		Block b = new BlockFastBench().setRegistryName("minecraft", "crafting_table");
		e.getRegistry().register(b);
		ForgeRegistries.ITEMS.register(new ItemBlock(b) {
			@Override
			public String getCreatorModId(ItemStack itemStack) {
				return MODID;
			}
		}.setRegistryName(b.getRegistryName()));
	}

	@EventHandler
	public void serverStartRemoval(FMLServerAboutToStartEvent e) {
		if (removeRecipeBook) PROXY.replacePlayerList(e.getServer());
	}

	@SubscribeEvent
	public void normalRemoval(EntityJoinWorldEvent e) {
		if (removeRecipeBook) PROXY.deleteBook(e.getEntity());
	}

	@SuppressWarnings("unchecked")
	public static void temaWtf() {
		try {
			Class<?> c = Class.forName("com.rwtema.extrautils2.items.ItemUnstableIngots");
			Collection<Class<?>> classes = (Collection<Class<?>>) c.getDeclaredField("ALLOWED_CLASSES").get(null);
			classes.add(ClientContainerFastBench.class);
			classes.add(ContainerFastBench.class);
		} catch (Exception noh) {
		}
	}

	public static void disableCrtSearchTree() {
		try {
			if (removeRecipeBook) CraftTweakerAPI.ENABLE_SEARCH_TREE_RECALCULATION = false;
		} catch (Exception noh) {
			noh.printStackTrace();
		}
	}

}
