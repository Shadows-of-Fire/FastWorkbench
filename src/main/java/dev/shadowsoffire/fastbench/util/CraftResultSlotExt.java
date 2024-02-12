package dev.shadowsoffire.fastbench.util;

import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.ForgeHooks;

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

    @SuppressWarnings({ "unchecked" })
    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        ForgeHooks.setCraftingPlayer(player);
        List<ItemStack> list;
        Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) this.inv.getRecipeUsed();
        if (recipe != null && recipe.matches(this.craftSlots, player.level())) list = recipe.getRemainingItems(this.craftSlots);
        else list = this.craftSlots.getItems();
        ForgeHooks.setCraftingPlayer(null);

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
        Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) this.inv.getRecipeUsed();
        if (recipe != null && recipe.matches(this.craftSlots, player.level())) return super.getItem();
        return ItemStack.EMPTY;
    }
}
