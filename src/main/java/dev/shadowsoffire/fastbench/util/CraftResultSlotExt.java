package dev.shadowsoffire.fastbench.util;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;

public class CraftResultSlotExt extends ResultSlot {

    protected final ResultContainer inv;

    public CraftResultSlotExt(Player player, CraftingContainer matrix, ResultContainer inv, int slotIndex, int xPosition, int yPosition) {
        super(player, matrix, inv, slotIndex, xPosition, yPosition);
        this.inv = inv;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }
        return this.getItem().copy();
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        super.onSwapCraft(numItemsCrafted);
        this.inv.setItem(0, this.getItem().copy()); // https://github.com/Shadows-of-Fire/FastWorkbench/issues/62 - Vanilla's SWAP action will leak this stack here.
    }

    @Override
    public void set(ItemStack stack) {}

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.removeCount > 0) {
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            EventHooks.firePlayerCraftingEvent(this.player, stack, this.craftSlots);
        }
        this.removeCount = 0;

        // Have to copy this code because vanilla nulls out the recipe, which shouldn't be done.
        RecipeHolder<?> recipe = this.inv.getRecipeUsed();
        if (recipe != null) {
            this.player.triggerRecipeCrafted(this.inv.getRecipeUsed(), this.craftSlots.getItems());
            if (!recipe.value().isSpecial()) {
                this.player.awardRecipes(Collections.singleton(recipe));
            }
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        CommonHooks.setCraftingPlayer(player);
        List<ItemStack> list;
        RecipeHolder<CraftingRecipe> recipe = (RecipeHolder<CraftingRecipe>) this.inv.getRecipeUsed();
        if (recipe != null && recipe.value().matches(this.craftSlots, player.level())) list = recipe.value().getRemainingItems(this.craftSlots);
        else list = this.craftSlots.getItems();
        CommonHooks.setCraftingPlayer(null);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack current = this.craftSlots.getItem(i);
            ItemStack remaining = list.get(i);

            if (!current.isEmpty()) {
                this.craftSlots.removeItem(i, 1);
                current = this.craftSlots.getItem(i);
            }

            if (!remaining.isEmpty()) {
                if (current.isEmpty()) {
                    this.craftSlots.setItem(i, remaining);
                }
                else if (ItemStack.isSameItemSameTags(current, remaining)) {
                    remaining.grow(current.getCount());
                    this.craftSlots.setItem(i, remaining);
                }
                else if (!this.player.getInventory().add(remaining)) {
                    this.player.drop(remaining, false);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemStack getItem() {
        if (player.level().isClientSide) return super.getItem();
        // Crafting Tweaks fakes 64x right click operations to right-click craft a stack to the "held" item, so we need to verify the recipe here.
        RecipeHolder<CraftingRecipe> recipe = (RecipeHolder<CraftingRecipe>) this.inv.getRecipeUsed();
        if (recipe != null && recipe.value().matches(this.craftSlots, player.level())) return super.getItem();
        return ItemStack.EMPTY;
    }
}
