package shadows.fastbench.gui;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class CraftingInventoryExt extends CraftingInventory {

	public boolean checkChanges = true;
	protected final Container container;

	public CraftingInventoryExt(Container container, int width, int height) {
		super(container, width, height);
		this.container = container;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);
		if (!itemstack.isEmpty()) {
			if (checkChanges) this.container.onCraftMatrixChanged(this);
		}

		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stackList.set(index, stack);
		if (checkChanges) this.container.onCraftMatrixChanged(this);
	}

}
