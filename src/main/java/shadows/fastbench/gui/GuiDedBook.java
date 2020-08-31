package shadows.fastbench.gui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

public class GuiDedBook extends RecipeBookGui {

	@Override
	public void removed() {
	}

	@Override
	public int updateScreenPosition(boolean widthTooNarrow, int width, int xSize) {
		return (width - xSize) / 2;
	}

	@Override
	public void toggleVisibility() {
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void slotClicked(@Nullable Slot slotIn) {
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(MatrixStack p_230430_1_, int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public void recipesUpdated() {
	}

	@Override
	public void initSearchBar(boolean p_201518_1_) {
	}

	@Override
	public void init(int p_201520_1_, int p_201520_2_, Minecraft p_201520_3_, boolean p_201520_4_, RecipeBookContainer<?> p_201520_5_) {
	}

	@Override
	protected void func_205702_a() {
	}

	@Override
	public void placeRecipe(int width, int height, int outputSlot, IRecipe<?> recipe, Iterator<Ingredient> ingredients, int maxAmount) {
	}

	@Override
	protected void setVisible(boolean p_193006_1_) {
	}

	@Override
	public void setSlotContents(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {
	}

	@Override
	public void setupGhostRecipe(IRecipe<?> p_193951_1_, List<Slot> p_193951_2_) {
	}

	@Override
	protected void sendUpdateSettings() {
	}

	@Override
	public void recipesShown(List<IRecipe<?>> recipes) {
	}

	@Override
	public boolean func_195604_a(double mouseX, double mouseY, int guiLeft, int guiTop, int xSize, int ySize, int mouseButton) {
		return false;
	}

	@Override
	public void func_230477_a_(MatrixStack p_230477_1_, int p_230477_2_, int p_230477_3_, boolean p_230477_4_, float p_230477_5_) {
	}

	@Override
	public void func_238924_c_(MatrixStack p_238924_1_, int p_238924_2_, int p_238924_3_, int p_238924_4_, int p_238924_5_) {
	}

}