package shadows.fastbench.util;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CraftingInventoryExt extends CraftingContainer {

	public boolean checkChanges = true;
	protected final AbstractContainerMenu container;

	public CraftingInventoryExt(AbstractContainerMenu container, int width, int height) {
		super(container, width, height);
		this.container = container;
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		ItemStack itemstack = ContainerHelper.removeItem(this.items, index, count);
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
