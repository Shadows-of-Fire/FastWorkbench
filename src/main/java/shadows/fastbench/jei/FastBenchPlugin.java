package shadows.fastbench.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.util.ResourceLocation;
import shadows.fastbench.FastBench;
import shadows.fastbench.gui.ContainerFastBench;

@JeiPlugin
public class FastBenchPlugin implements IModPlugin {

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ContainerFastBench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FastBench.MODID, FastBench.MODID);
	}

}
