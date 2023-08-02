package dev.shadowsoffire.fastbench.api;

import javax.annotation.Nonnull;

import net.minecraft.world.inventory.ResultContainer;

/**
 * ICraftingContainer needs to be implemented on Containers that are declaring FastWorkbench compat.<br>
 * This interface allows the {@link dev.shadowsoffire.fastbench.net.RecipeMessage} to update your screen on the client.
 */
public interface ICraftingContainer {

	/**
	 * Allows FastWorkbench to access the result of this crafting container. <br>
	 * @return The container's instance of {@link CraftResultInventory}.
	 */
	@Nonnull
	public ResultContainer getResult();

}
