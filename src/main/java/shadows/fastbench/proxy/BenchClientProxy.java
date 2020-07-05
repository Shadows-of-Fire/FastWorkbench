package shadows.fastbench.proxy;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shadows.fastbench.book.DedClientBook;
import shadows.fastbench.book.DedRecipeBook;
import shadows.fastbench.net.HijackedPlayerList;

public class BenchClientProxy implements IBenchProxy {

	public static final DedClientBook CLIENT_BOOK = new DedClientBook();

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof ServerPlayerEntity) ((ServerPlayerEntity) e).recipeBook = new DedRecipeBook();
		if (e instanceof ClientPlayerEntity) ((ClientPlayerEntity) e).recipeBook = CLIENT_BOOK;
	}

	@Override
	public void replacePlayerList(MinecraftServer server) {
		if (!(server.getPlayerList() instanceof HijackedPlayerList)) server.setPlayerList(new HijackedPlayerList((IntegratedServer) server));
	}

	@Override
	public void registerButtonRemover() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void removeButton(InitGuiEvent e) {
		// TODO MCP-name field_230710_m_ -> buttons
		for (Widget b : e.getGui().field_230710_m_)
			// TODO MCP-name: field_230694_p_ -> visible
			if (b instanceof ImageButton && isBookButton((ImageButton) b)) b.field_230694_p_ = false;
	}

	private static boolean isBookButton(ImageButton b) {
		return b.resourceLocation.getPath().equals("textures/gui/recipe_button.png");
	}

}
