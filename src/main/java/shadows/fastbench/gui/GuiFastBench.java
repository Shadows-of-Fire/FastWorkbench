package shadows.fastbench.gui;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import shadows.fastbench.FastBench;

public class GuiFastBench extends CraftingScreen {

	public GuiFastBench(ContainerFastBench container, PlayerInventory inv, ITextComponent name) {
		super(container, inv, name);
	}

	@Override
	public void init() {
		super.init();
		if (FastBench.removeRecipeBook) {
			this.buttons.clear();
			this.children.clear();
			this.recipeBookGui = new GuiDedBook();
			this.setFocusedDefault(new IGuiEventListener() {});
		}
	}

	@Override
	public ContainerFastBench getContainer() {
		return (ContainerFastBench) this.container;
	}
}