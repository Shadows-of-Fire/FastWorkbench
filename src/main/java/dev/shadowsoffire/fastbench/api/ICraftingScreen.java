package dev.shadowsoffire.fastbench.api;

import javax.annotation.Nonnull;

/**
 * ICraftingScreen needs to be implemented on ContainerScreens that are declaring FastWorkbench compat.<br>
 * This interface allows the {@link dev.shadowsoffire.fastbench.net.RecipeMessage} to update your screen on the client.
 */
public interface ICraftingScreen {

	/**
	 * Allows FastWorkbench to access your underlying Container object to store the recipe on the client.
	 * @return The Container object currently held by this ContainerScreen.
	 */
	@Nonnull
	public ICraftingContainer getContainer();
}
