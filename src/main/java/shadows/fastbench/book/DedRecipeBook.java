package shadows.fastbench.book;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.RecipeBookServer;

public class DedRecipeBook extends RecipeBookServer {

	@Override
	public void add(List<IRecipe> recipesIn, EntityPlayerMP player) {
	}

	@Override
	public void copyFrom(RecipeBook that) {
	}

	@Override
	public void init(EntityPlayerMP player) {
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
	public NBTTagCompound write() {
		return new NBTTagCompound();
	}

	@Override
	public void read(NBTTagCompound tag) {
	}

	@Override
	public boolean isUnlocked(IRecipe recipe) {
		return true;
	}

	@Override
	public void remove(List<IRecipe> recipes, EntityPlayerMP player) {
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
