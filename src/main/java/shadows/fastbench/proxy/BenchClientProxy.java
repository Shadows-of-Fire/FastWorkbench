package shadows.fastbench.proxy;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.fastbench.FastBench;
import shadows.fastbench.book.DedClientBook;

public class BenchClientProxy implements IBenchProxy {

	public static final DedClientBook CLIENT_BOOK = new DedClientBook();

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof EntityPlayerMP) ((EntityPlayerMP) e).recipeBook = FastBench.SERVER_BOOK;
		if (e instanceof EntityPlayerSP) ((EntityPlayerSP) e).recipeBook = CLIENT_BOOK;
	}

	@Override
	public void registerButtonRemover() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void removeButton(InitGuiEvent e) {
		for (GuiButton b : e.getButtonList())
			if (b instanceof GuiButtonImage && isBookButton((GuiButtonImage) b)) b.visible = false;
	}

	private static boolean isBookButton(GuiButtonImage b) {
		//Vanilla Player Inventory
		if (b.id == 10 && b.resourceLocation == GuiInventory.INVENTORY_BACKGROUND) return true;
		//Vanilla Crafting Inventory
		if (b.id == 10 && b.resourceLocation == GuiCrafting.CRAFTING_TABLE_GUI_TEXTURES) return true;
		return false;
	}

}
