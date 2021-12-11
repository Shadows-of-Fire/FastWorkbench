package shadows.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.api.ICraftingScreen;

@Mixin(CraftingScreen.class)
public class MixinCraftingScreen implements ICraftingScreen {

	@Override
	public ICraftingContainer getContainer() {
		return (ICraftingContainer) ths().getMenu();
	}

	private CraftingScreen ths() {
		return (CraftingScreen) (Object) this;
	}
}
