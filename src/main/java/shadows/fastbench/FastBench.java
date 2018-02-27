package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import shadows.fastbench.block.BlockFastBench;
import shadows.fastbench.gui.Handler;
import shadows.fastbench.proxy.BenchProxy;

@Mod(modid = FastBench.MODID, name = FastBench.MODNAME, version = FastBench.VERSION)
public class FastBench {

	public static final String MODID = "fastbench";
	public static final String MODNAME = "FastWorkbench";
	public static final String VERSION = "1.1.2";

	public static final Logger LOG = LogManager.getLogger(MODID);

	@Instance
	public static FastBench INSTANCE;

	@SidedProxy(serverSide = "shadows.fastbench.proxy.BenchProxy", clientSide = "shadows.fastbench.proxy.BenchClientProxy")
	public static BenchProxy PROXY;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new Handler());
		MinecraftForge.EVENT_BUS.register(this);
		NBTTagCompound t = new NBTTagCompound();
		t.setString("ContainerClass", "shadows.fastbench.gui.ContainerFastBench");
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", t);
		Configuration c = new Configuration(e.getSuggestedConfigurationFile());
		delet = !c.getBoolean("is quat here", "crafting", false, "If the recipe book is not deleted");
		if (c.hasChanged()) c.save();
	}

	@SubscribeEvent
	public void blockBois(Register<Block> e) {
		e.getRegistry().register(new BlockFastBench().setRegistryName("minecraft", "crafting_table"));
	}

	public static boolean delet = true;

	@SubscribeEvent
	public void loginBois(EntityJoinWorldEvent e) {
		if (delet) PROXY.deleteBook(e.getEntity());
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}
}
