package shadows.fastbench.util;

import dev.shadowsoffire.placebo.network.PacketDistro;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import shadows.fastbench.FastBench;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.net.RecipeMessage;

@SuppressWarnings("unchecked")
public class FastBenchUtil {

    /**
     * Queues an update for the grid that will execute after the next 5-tick window.
     * Multiple queue-ups will not stack, and only the latest one will be processed.
     */
    public static void queueSlotUpdate(Level level, Player player, CraftingInventoryExt inv, ResultContainer result) {
        SlotUpdateManager.queueSlotUpdate(level, player, inv, result);
    }

    /**
     * Called when a slot is changed in the crafting grid in a {@link ICraftingContainer}
     * Does no work on the client.
     * On the server, checks if a recipe is cached, and checks if it still matches. If it doesn't match, a new match is attempted to be found.
     * If any match is found, it gathers the result of that match and presents it in the output slot.
     * It also notifies the client iff the recipe has changed, or the recipe is not null and the recipe is dynamic (special).
     * 
     * @param world  The world
     * @param player The crafting player
     * @param inv    The crafting grid
     * @param result The result inventory
     * @apiNote Use {@link #queueSlotUpdate(Level, Player, CraftingInventoryExt, ResultContainer)} unless you need immediate results for some reason.
     */
    public static void slotChangedCraftingGrid(Level world, Player player, CraftingInventoryExt inv, ResultContainer result) {
        if (!world.isClientSide && inv.checkChanges) {

            ItemStack itemstack = ItemStack.EMPTY;

            Recipe<CraftingContainer> oldRecipe = (Recipe<CraftingContainer>) result.getRecipeUsed();
            Recipe<CraftingContainer> recipe = oldRecipe;
            if (recipe == null || !recipe.matches(inv, world)) recipe = findRecipe(inv, world);

            if (recipe != null) itemstack = recipe.assemble(inv, world.registryAccess());

            // Need to check if the output is empty, because if the recipe book is being used, the recipe will already be set.
            if (oldRecipe != recipe || result.getItem(0).isEmpty()) {
                PacketDistro.sendTo(FastBench.CHANNEL, new RecipeMessage(recipe, itemstack), player);
                result.setItem(0, itemstack);
                result.setRecipeUsed(recipe);
            }
            else if (recipe != null) {
                // https://github.com/Shadows-of-Fire/FastWorkbench/issues/72 - Some modded recipes may update the output and not mark themselves as special, moderately
                // annoying but... bleh
                if (recipe.isSpecial() || !recipe.getClass().getName().startsWith("net.minecraft") && !ItemStack.matches(itemstack, result.getItem(0))) {
                    PacketDistro.sendTo(FastBench.CHANNEL, new RecipeMessage(recipe, itemstack), player);
                    result.setItem(0, itemstack);
                    result.setRecipeUsed(recipe);
                }
            }
        }
    }

    /**
     * Handles the shift-click-crafting operation.
     * Assuming the result slot is not null, and an item (a crafting result) is present:
     * We tell the crafting matrix to stop checking changes, and retrieve the recipe.
     * As long as that recipe is not null, and it continues matching:
     * Retrieve the output and copy it, and notify it that it has been crafted.
     * Try and insert that item into the output inventory based on the indicies.
     * If it fails, we return empty (stopping this operation, and preventing it from being called again)
     * Otherwise we notify the slot that however much of that item has been crafted, and take it.
     * Once the recipe becomes invalid, we check changes and return the crafted item for vanilla to do... something
     * 
     * @param player      The crafting player
     * @param container   The crafting container
     * @param resultSlot  The result (output) slot, should be a CraftResultSlotExt if everything has been applied correctly.
     * @param craftMatrix The crafting matrix
     * @param craftResult The crafting result inventory
     * @param outStart    The index of slots to begin trying to place the output
     * @param outEnd      The index of slots to stop trying to place the output.
     * @return
     */
    public static ItemStack handleShiftCraft(Player player, AbstractContainerMenu container, Slot resultSlot, CraftingInventoryExt craftMatrix, ResultContainer craftResult, int outStart, int outEnd) {
        return handleShiftCraft(player, container, resultSlot, craftMatrix, craftResult, (c, p) -> !DumbShitTM.mergeItemStack(c, p, outStart, outEnd));
    }

    public static ItemStack handleShiftCraft(Player player, AbstractContainerMenu container, Slot resultSlot, CraftingInventoryExt craftMatrix, ResultContainer craftResult, OutputMover mover) {
        ItemStack outputCopy = ItemStack.EMPTY;
        if (resultSlot != null && resultSlot.hasItem()) {
            craftMatrix.checkChanges = false;
            Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) craftResult.getRecipeUsed();
            while (recipe != null && recipe.matches(craftMatrix, player.level())) {
                ItemStack recipeOutput = recipe.assemble(craftMatrix, player.level().registryAccess());
                if (recipeOutput.isEmpty()) throw new RuntimeException("A recipe matched but produced an empty output - Offending Recipe : " + recipe.getId() + " - This is NOT a bug in FastWorkbench!");
                outputCopy = recipeOutput.copy();

                recipeOutput.onCraftedBy(player.level(), player, 1);
                ForgeEventFactory.firePlayerCraftingEvent(player, recipeOutput, craftMatrix);

                if (!player.level().isClientSide && mover.merge(container, recipeOutput)) {
                    craftMatrix.checkChanges = true;
                    return ItemStack.EMPTY;
                }

                ((ResultSlot) resultSlot).removeCount += outputCopy.getCount();
                // Handles the actual work of removing the input items.
                resultSlot.onTake(player, recipeOutput);
            }
            craftMatrix.checkChanges = true;
            slotChangedCraftingGrid(player.level(), player, craftMatrix, craftResult);
        }
        return outputCopy;
    }

    public static Recipe<CraftingContainer> findRecipe(CraftingContainer inv, Level world) {
        return world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inv, world).orElse(null);
    }

    public static interface OutputMover {
        boolean merge(AbstractContainerMenu container, ItemStack recipeOutput);
    }

}
