package shadows.fastbench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ObjectHolder;
import shadows.fastbench.block.BlockFastBench;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.CraftingInventoryExt;
import shadows.fastbench.gui.SlotCraftingSucks;
import shadows.fastbench.net.RecipeMessage;
import shadows.fastbench.proxy.BenchClientProxy;
import shadows.fastbench.proxy.BenchServerProxy;
import shadows.fastbench.proxy.IBenchProxy;
import shadows.placebo.config.Configuration;
import shadows.placebo.util.NetworkUtils;
import shadows.placebo.util.PlaceboUtil;

@Mod(FastBench.MODID)
public class FastBench {

	public static final String MODID = "fastbench";
	public static final Logger LOG = LogManager.getLogger(MODID);

	public static final IBenchProxy PROXY;
	static {
		PROXY = DistExecutor.unsafeRunForDist(() -> () -> new BenchClientProxy(), () -> () -> new BenchServerProxy());
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
		NetworkUtils.registerMessage(CHANNEL, 0, new RecipeMessage());
		if (removeRecipeBook) PROXY.registerButtonRemover();
	}

	@SubscribeEvent
	public void containers(Register<ContainerType<?>> e) {
		e.getRegistry().register(new ContainerType<>(ContainerFastBench::new).setRegistryName("fastbench"));
	}

	@SubscribeEvent
	public void imc(InterModEnqueueEvent e) {
		CompoundNBT t = new CompoundNBT();
		t.putString("ContainerClass", "shadows.fastbench.gui.ContainerFastBench");
		t.putString("AlignToGrid", "west");
		InterModComms.sendTo("craftingtweaks", "RegisterProvider", () -> t);
	}

	@SubscribeEvent
	public void blockBois(Register<Block> e) {
		PlaceboUtil.registerOverrideBlock(new BlockFastBench().setRegistryName("minecraft", "crafting_table"), MODID);
	}

	@SubscribeEvent
	public void serverStartRemoval(FMLServerAboutToStartEvent e) {
		if (removeRecipeBook) PROXY.replacePlayerList(e.getServer());
	}

	@SubscribeEvent
	public void normalRemoval(EntityJoinWorldEvent e) {
		if (removeRecipeBook) PROXY.deleteBook(e.getEntity());
	}

	@SubscribeEvent
	public void playerContainerStuff(EntityJoinWorldEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof PlayerEntity) {
			PlayerContainer ctr = ((PlayerEntity) ent).container;
			if (ctr.inventorySlots.get(0) instanceof SlotCraftingSucks) return; //Already replaced this one, do nothing.
			CraftingInventoryExt inv = new CraftingInventoryExt(ctr, 2, 2);
			ctr.craftMatrix = inv;
			for (int i = 0; i < 5; i++) {
				Slot s = ctr.inventorySlots.get(i);
				if (i == 0) {
					SlotCraftingSucks craftSlot = new SlotCraftingSucks(ctr.player, ctr.craftMatrix, ctr.craftResult, 0, 154, 28);
					craftSlot.slotNumber = 0;
					ctr.inventorySlots.set(0, craftSlot);
				} else {
					ObfuscationReflectionHelper.setPrivateValue(Slot.class, s, inv, "field_75224_c");
				}
			}
		}
	}

}
