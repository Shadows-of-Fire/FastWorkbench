package shadows.fastbench.book;

import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class DedClientBook extends RecipeBookClient {

	@Override
	public void copyFrom(RecipeBook that) {
	}

	@Override
	public boolean isFilteringCraftable() {
		return false;
	}

	@Override
	public boolean isGuiOpen() {
		return false;
	}

	@Override
	public void lock(IRecipe recipe) {
	}

	@Override
	public void markNew(IRecipe recipe) {
	}

	@Override
	public void markSeen(IRecipe recipe) {
	}

	@Override
	public boolean isNew(IRecipe recipe) {
		return false;
	}

	@Override
	public boolean isUnlocked(IRecipe recipe) {
		return true;
	}

	@Override
	public void setFilteringCraftable(boolean shouldFilter) {
	}

	@Override
	public void setGuiOpen(boolean open) {
	}

	@Override
	public void unlock(IRecipe recipe) {
	}
}
