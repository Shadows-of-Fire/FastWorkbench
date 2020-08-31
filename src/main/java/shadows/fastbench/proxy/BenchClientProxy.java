package shadows.fastbench.proxy;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shadows.fastbench.book.DedClientBook;
import shadows.fastbench.book.DedRecipeBook;

public class BenchClientProxy implements IBenchProxy {

	public static final DedClientBook CLIENT_BOOK = new DedClientBook();

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof ServerPlayerEntity) ((ServerPlayerEntity) e).recipeBook = new DedRecipeBook();
		if (e instanceof ClientPlayerEntity) ((ClientPlayerEntity) e).recipeBook = CLIENT_BOOK;
	}

	@Override
	public void registerButtonRemover() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void removeButton(InitGuiEvent e) {
		for (Widget b : e.getGui().buttons)
			if (b instanceof ImageButton && isBookButton((ImageButton) b)) b.visible = false;
	}

	private static boolean isBookButton(ImageButton b) {
		return b.resourceLocation.getPath().equals("textures/gui/recipe_button.png");
	}

}
