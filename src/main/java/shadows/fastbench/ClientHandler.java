package shadows.fastbench;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent.InitScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static shadows.fastbench.FastBench.disableToolTip;

@EventBusSubscriber(modid = FastBench.MODID, value = Dist.CLIENT)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClientHandler {

	private static final int tooltipIdx = ThreadLocalRandom.current().nextInt(7);

	@SubscribeEvent
	public static void removeButton(InitScreenEvent e) {
		if (!FastBench.removeBookButton) return;
		for (Widget b : e.getScreen().renderables)
			if (b instanceof ImageButton ib && isBookButton(ib)) ib.visible = false;
	}

	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		if(!disableToolTip){
			if (e.getItemStack().getItem() == Items.CRAFTING_TABLE) {
				e.getToolTip().add(new TranslatableComponent("info.fb.very_fast" + tooltipIdx).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
			}
		}
	}

	private static boolean isBookButton(ImageButton b) {
		return b.resourceLocation.getPath().equals("textures/gui/recipe_button.png");
	}

}
