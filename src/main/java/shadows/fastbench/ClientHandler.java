package shadows.fastbench;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = FastBench.MODID, value = Dist.CLIENT)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClientHandler {

	private static final int tooltipIdx = ThreadLocalRandom.current().nextInt(7);

	@SubscribeEvent
	public static void removeButton(InitGuiEvent e) {
		if (!FastBench.removeBookButton) return;
		for (Widget b : e.getGui().buttons)
			if (b instanceof ImageButton && isBookButton((ImageButton) b)) b.visible = false;
	}

	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		if (e.getItemStack().getItem() == Items.CRAFTING_TABLE) {
			e.getToolTip().add(new TranslationTextComponent("info.fb.very_fast" + tooltipIdx).withStyle(TextFormatting.DARK_GRAY, TextFormatting.ITALIC));
		}
	}

	private static boolean isBookButton(ImageButton b) {
		return b.resourceLocation.getPath().equals("textures/gui/recipe_button.png");
	}

}
