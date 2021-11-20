package shadows.fastbench.util;

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
	public ItemStack removeItem(int index, int count) {
		ItemStack itemstack = ItemStackHelper.removeItem(this.items, index, count);
		if (!itemstack.isEmpty()) {
			if (checkChanges) this.container.slotsChanged(this);
		}

		return itemstack;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.items.set(index, stack);
		if (checkChanges) this.container.slotsChanged(this);
	}

}
