package shadows.fastbench.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class SlotCraftingSucks extends CraftingResultSlot {

	protected final ContainerFastBench container;

	public SlotCraftingSucks(ContainerFastBench container, PlayerEntity player, CraftingInventory inv, IInventory holder, int slotIndex, int xPosition, int yPosition) {
		super(player, inv, holder, slotIndex, xPosition, yPosition);
		this.container = container;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.amountCrafted += Math.min(amount, getStack().getCount());
		}
		return getStack();
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCrafting(this.player.world, this.player, this.amountCrafted);
			BasicEventHooks.firePlayerCraftingEvent(this.player, stack, field_75239_a);
		}

		this.amountCrafted = 0;
	}

	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		this.onCrafting(stack);
		ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> list;
		if (container.lastRecipe != null && container.lastRecipe.matches(field_75239_a, container.world)) list = container.lastRecipe.getRemainingItems(field_75239_a);
		else list = field_75239_a.stackList;
		ForgeHooks.setCraftingPlayer(null);

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = this.field_75239_a.getStackInSlot(i);
			ItemStack itemstack1 = list.get(i);

			if (!itemstack.isEmpty()) {
				this.field_75239_a.decrStackSize(i, 1);
				itemstack = this.field_75239_a.getStackInSlot(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.field_75239_a.setInventorySlotContents(i, itemstack1);
				} else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.field_75239_a.setInventorySlotContents(i, itemstack1);
				} else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
					this.player.dropItem(itemstack1, false);
				}
			}
		}

		return stack;
	}
}