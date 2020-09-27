package shadows.fastbench.book;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeBookStatus;
import net.minecraft.util.ResourceLocation;

public class DedClientBook extends ClientRecipeBook {

	public DedClientBook() {
		super();
	}

	@Override
	public void copyFrom(RecipeBook that) {
	}

	@Override
	public boolean isUnlocked(ResourceLocation p_226144_1_) {
		return false;
	}

	@Override
	public void func_243196_a(Iterable<IRecipe<?>> p_243196_1_) {
	}

	@Override
	public void lock(IRecipe<?> recipe) {
	}

	@Override
	public void markNew(IRecipe<?> recipe) {
	}

	@Override
	public void markSeen(IRecipe<?> recipe) {
	}

	@Override
	public boolean isNew(IRecipe<?> recipe) {
		return false;
	}

	@Override
	public boolean isUnlocked(IRecipe<?> recipe) {
		return true;
	}

	@Override
	public void unlock(IRecipe<?> recipe) {
	}

	@Override
	public List<RecipeList> getRecipes() {
		return Collections.emptyList();
	}

	@Override
	public List<RecipeList> getRecipes(RecipeBookCategories p_202891_1_) {
		return Collections.emptyList();
	}

	@Override
	protected void lock(ResourceLocation p_209119_1_) {
	}

	@Override
	protected void markNew(ResourceLocation p_209120_1_) {
	}

	@Override
	protected void unlock(ResourceLocation p_209118_1_) {
	}

	@Override
	public void func_242140_a(RecipeBookStatus p_242140_1_) {
	}

	@Override
	public boolean func_242141_a(RecipeBookContainer<?> p_242141_1_) {
		return false;
	}

	@Override
	public boolean func_242142_a(RecipeBookCategory p_242142_1_) {
		return false;
	}

	@Override
	public void func_242143_a(RecipeBookCategory p_242143_1_, boolean p_242143_2_) {

	}

	@Override
	public void func_242144_a(RecipeBookCategory p_242144_1_, boolean p_242144_2_, boolean p_242144_3_) {

	}

	@Override
	public boolean func_242145_b(RecipeBookCategory p_242145_1_) {
		return false;
	}

	@Override
	public void func_242146_b(RecipeBookCategory p_242146_1_, boolean p_242146_2_) {

	}

}
