package shadows.fastbench.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class SlotCraftingSucks extends CraftingResultSlot {

	protected final CraftResultInventory inv;

	public SlotCraftingSucks(PlayerEntity player, CraftingInventory matrix, CraftResultInventory inv, int slotIndex, int xPosition, int yPosition) {
		super(player, matrix, inv, slotIndex, xPosition, yPosition);
		this.inv = inv;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.amountCrafted += Math.min(amount, getStack().getCount());
		}
		return getStack().copy();
	}

	@Override
	public void putStack(ItemStack stack) {
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCrafting(this.player.world, this.player, this.amountCrafted);
			BasicEventHooks.firePlayerCraftingEvent(this.player, stack, craftMatrix);
		}
		this.amountCrafted = 0;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		this.onCrafting(stack);
		ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> list;
		IRecipe<CraftingInventory> recipe = (IRecipe<CraftingInventory>) inv.getRecipeUsed();
		if (recipe != null && recipe.matches(craftMatrix, player.world)) list = recipe.getRemainingItems(craftMatrix);
		else list = craftMatrix.stackList;
		ForgeHooks.setCraftingPlayer(null);

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = list.get(i);

			if (!itemstack.isEmpty()) {
				this.craftMatrix.decrStackSize(i, 1);
				itemstack = this.craftMatrix.getStackInSlot(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
					this.player.dropItem(itemstack1, false);
				}
			}
		}
		return stack;
	}
}