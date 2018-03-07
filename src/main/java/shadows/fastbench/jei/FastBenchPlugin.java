package shadows.fastbench.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.GuiFastBench.ClientContainerFastBench;

@JEIPlugin
public class FastBenchPlugin implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerFastBench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ClientContainerFastBench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

}
