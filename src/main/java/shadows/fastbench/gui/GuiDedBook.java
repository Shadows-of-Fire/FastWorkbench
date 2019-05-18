package shadows.fastbench.gui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.inventory.ContainerRecipeBook;
import net.minecraft.inventory.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

public class GuiDedBook extends GuiRecipeBook {

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
	public void render(int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public void renderTooltip(int guiLeft, int guiTop, int mouseX, int mouseY) {
	}

	@Override
	public void renderGhostRecipe(int guiLeft, int guiTop, boolean someOffsetThing, float partialTicks) {
	}

	@Override
	public boolean canFocus() {
		return false;
	}

	@Override
	public boolean func_195604_a(double p_195604_1_, double p_195604_3_, int p_195604_5_, int p_195604_6_, int p_195604_7_, int p_195604_8_, int p_195604_9_) {
		return false;
	}

	@Override
	protected void setVisible(boolean p_193006_1_) {
	}

	@Override
	public void recipesUpdated() {
	}

	@Override
	public void recipesShown(List<IRecipe> recipes) {
	}

	@Override
	public void setupGhostRecipe(IRecipe p_193951_1_, List<Slot> p_193951_2_) {
	}

	@Override
	public void func_201518_a(boolean p_201518_1_) {
	}

	@Override
	public void func_201520_a(int p_201520_1_, int p_201520_2_, Minecraft p_201520_3_, boolean p_201520_4_, ContainerRecipeBook p_201520_5_) {
	}

	@Override
	protected void func_205702_a() {
	}

	@Override
	protected String func_205703_f() {
		return "";
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return false;
	}

	@Override
	public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
		return false;
	}

	@Override
	public void placeRecipe(int width, int height, int outputSlot, IRecipe recipe, Iterator<Ingredient> ingredients, int maxAmount) {
	}

	@Override
	protected void sendUpdateSettings() {
	}

}