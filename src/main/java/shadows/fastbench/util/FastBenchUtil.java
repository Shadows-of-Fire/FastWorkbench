package shadows.fastbench.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.DumbShitTM;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import shadows.fastbench.FastBench;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.net.RecipeMessage;
import shadows.placebo.util.NetworkUtils;

@SuppressWarnings("unchecked")
public class FastBenchUtil {

	/**
	 * Called when a slot is changed in the crafting grid in a {@link ICraftingContainer}
	 * Does no work on the client.
	 * On the server, checks if a recipe is cached, and checks if it still matches. If it doesn't match, a new match is attempted to be found.
	 * If any match is found, it gathers the result of that match and presents it in the output slot.
	 * It also notifies the client iff the recipe has changed, or the recipe is not null and the recipe is dynamic (special).
	 * @param world The world
	 * @param player The crafting player
	 * @param inv The crafting grid 
	 * @param result The result inventory
	 */
	public static void slotChangedCraftingGrid(World world, PlayerEntity player, CraftingInventoryExt inv, CraftResultInventory result) {
		if (!world.isClientSide) {

			ItemStack itemstack = ItemStack.EMPTY;

			IRecipe<CraftingInventory> oldRecipe = (IRecipe<CraftingInventory>) result.getRecipeUsed();
			IRecipe<CraftingInventory> recipe = oldRecipe;
			if (recipe == null || !recipe.matches(inv, world)) recipe = findRecipe(inv, world);

			if (recipe != null) itemstack = recipe.assemble(inv);

			if (oldRecipe != recipe) {
				NetworkUtils.sendTo(FastBench.CHANNEL, new RecipeMessage(recipe, itemstack), player);
				result.setItem(0, itemstack);
				result.setRecipeUsed(recipe);
			} else if (recipe != null) {
				//https://github.com/Shadows-of-Fire/FastWorkbench/issues/72 - Some modded recipes may update the output and not mark themselves as special, moderately annoying but... bleh
				if (recipe.isSpecial() || (!recipe.getClass().getName().startsWith("net.minecraft") && !ItemStack.matches(itemstack, result.getItem(0)))) {
					NetworkUtils.sendTo(FastBench.CHANNEL, new RecipeMessage(recipe, itemstack), player);
					result.setItem(0, itemstack);
					result.setRecipeUsed(recipe);
				}
			}
		}
	}

	/**
	 * Handles the shift-click-crafting operation.
	 * 
	 * Assuming the result slot is not null, and an item (a crafting result) is present:
	 * 	We tell the crafting matrix to stop checking changes, and retrieve the recipe.
	 * 	As long as that recipe is not null, and it continues matching:
	 * 	  Retrieve the output and copy it, and notify it that it has been crafted.
	 *    Try and insert that item into the output inventory based on the indicies.
	 *    If it fails, we return empty (stopping this operation, and preventing it from being called again)
	 * 	  Otherwise we notify the slot that however much of that item has been crafted, and take it.
	 *  Once the recipe becomes invalid, we check changes and return the crafted item for vanilla to do... something
	 * @param player The crafting player
	 * @param container The crafting container
	 * @param resultSlot The result (output) slot, should be a CraftResultSlotExt if everything has been applied correctly.
	 * @param craftMatrix The crafting matrix
	 * @param craftResult The crafting result inventory
	 * @param outStart The index of slots to begin trying to place the output
	 * @param outEnd The index of slots to stop trying to place the output.
	 * @return 
	 */
	public static ItemStack handleShiftCraft(PlayerEntity player, Container container, Slot resultSlot, CraftingInventoryExt craftMatrix, CraftResultInventory craftResult, int outStart, int outEnd) {
		ItemStack outputCopy = ItemStack.EMPTY;
		if (resultSlot != null && resultSlot.hasItem()) {
			craftMatrix.checkChanges = false;
			IRecipe<CraftingInventory> recipe = (IRecipe<CraftingInventory>) craftResult.getRecipeUsed();
			while (recipe != null && recipe.matches(craftMatrix, player.level)) {
				ItemStack recipeOutput = resultSlot.getItem().copy();
				outputCopy = recipeOutput.copy();

				recipeOutput.getItem().onCraftedBy(recipeOutput, player.level, player);

				if (!player.level.isClientSide && !DumbShitTM.mergeItemStack(container, recipeOutput, outStart, outEnd)) {
					craftMatrix.checkChanges = true;
					return ItemStack.EMPTY;
				}

				((CraftingResultSlot) resultSlot).removeCount += outputCopy.getCount();
				// Handles the actual work of removing the input items.
				resultSlot.onTake(player, recipeOutput);
			}
			craftMatrix.checkChanges = true;
			slotChangedCraftingGrid(player.level, player, craftMatrix, craftResult);
		}
		return outputCopy;
	}

	public static IRecipe<CraftingInventory> findRecipe(CraftingInventory inv, World world) {
		return world.getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, inv, world).orElse(null);
	}

	public static final void fuckingTellMixinToNotBeStupid(Container c, CraftingInventoryExt ex) {
		if (c instanceof WorkbenchContainer) ((WorkbenchContainer) c).craftSlots = ex;
		if (c instanceof PlayerContainer) ((PlayerContainer) c).craftSlots = ex;
	}

}
