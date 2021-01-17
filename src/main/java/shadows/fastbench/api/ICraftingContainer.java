package shadows.fastbench.api;

import javax.annotation.Nonnull;

import net.minecraft.inventory.CraftResultInventory;

/**
 * ICraftingContainer needs to be implemented on Containers that are declaring FastWorkbench compat.<br>
 * This interface allows the {@link shadows.fastbench.net.RecipeMessage} to update your screen on the client.
 */
public interface ICraftingContainer {

	/**
	 * Allows FastWorkbench to access the result of this crafting container. <br>
	 * @return The container's instance of {@link CraftResultInventory}.
	 */
	@Nonnull
	public CraftResultInventory getResult();

}
