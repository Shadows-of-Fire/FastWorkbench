package shadows.fastbench.book;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerRecipeBook;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.ServerRecipeBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class DedRecipeBook extends ServerRecipeBook {

	public DedRecipeBook(RecipeManager mgr) {
		super(mgr);
	}

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
	public void setFilteringCraftable(boolean shouldFilter) {
	}

	@Override
	public void setGuiOpen(boolean open) {
	}

	@Override
	public void unlock(IRecipe recipe) {
	}

	@Override
	public int add(Collection<IRecipe> p_197926_1_, EntityPlayerMP p_197926_2_) {
		return 0;
	}

	@Override
	public void init(EntityPlayerMP player) {
	}

	@Override
	public boolean isFilteringCraftable(ContainerRecipeBook p_203432_1_) {
		return false;
	}

	@Override
	public boolean isFurnaceFilteringCraftable() {
		return false;
	}

	@Override
	public boolean isFurnaceGuiOpen() {
		return false;
	}

	@Override
	protected void lock(ResourceLocation p_209119_1_) {
	}

	@Override
	protected void markNew(ResourceLocation p_209120_1_) {
	}

	@Override
	public int remove(Collection<IRecipe> p_197925_1_, EntityPlayerMP p_197925_2_) {
		return 0;
	}

	@Override
	public void setFurnaceFilteringCraftable(boolean p_202882_1_) {
	}

	@Override
	public void setFurnaceGuiOpen(boolean p_202881_1_) {
	}
}
