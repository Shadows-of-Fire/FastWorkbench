package shadows.fastbench;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.GuiFastBench;

@EventBusSubscriber(modid = FastBench.MODID, value = Dist.CLIENT, bus = Bus.MOD)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClientHandler {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent e) {
		ScreenManager.registerFactory(FastBench.FAST_CRAFTING, new Factory());
	}

	private static class Factory implements IScreenFactory {

		@Override
		public GuiFastBench create(Container container, PlayerInventory pInv, ITextComponent name) {
			return new GuiFastBench((ContainerFastBench) container, pInv, name);
		}

	}
}
