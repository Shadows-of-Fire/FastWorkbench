package shadows.fastbench.gui;

import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import shadows.fastbench.FastBench;

public class GuiFastBench extends CraftingScreen {

	public GuiFastBench(ContainerFastBench container, PlayerInventory inv, ITextComponent name) {
		super(container, inv, name);
	}

	@Override
	// TODO MCP-name: func_231160_c_ -> init
	public void func_231160_c_() {
		super.func_231160_c_();
		if (FastBench.removeRecipeBook) {
			// TODO MCP-name: field_230710_m_ -> buttons
			this.field_230710_m_.clear();
			// TODO MCP-name: field_230705_e_ -> children
			this.field_230705_e_.clear();
			this.recipeBookGui = new GuiDedBook();
			this.setFocusedDefault(null);
		}
	}

	@Override
	public ContainerFastBench getContainer() {
		return (ContainerFastBench) this.container;
	}
}