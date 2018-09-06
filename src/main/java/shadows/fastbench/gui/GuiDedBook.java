package shadows.fastbench.gui;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDedBook extends GuiRecipeBook {

	@Override
	public void func_194303_a(int width, int height, Minecraft minecraft, boolean widthTooNarrow, InventoryCrafting craftingSlots) {
	}

	@Override
	public void initVisuals(boolean widthTooNarrow, InventoryCrafting craftingSlots) {
	}

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
	public boolean mouseClicked(int p_191862_1_, int p_191862_2_, int p_191862_3_) {
		return false;
	}

	@Override
	public boolean hasClickedOutside(int p_193955_1_, int p_193955_2_, int p_193955_3_, int p_193955_4_, int p_193955_5_, int p_193955_6_) {
		return true;
	}

	@Override
	public boolean keyPressed(char typedChar, int keycode) {
		return false;
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

}