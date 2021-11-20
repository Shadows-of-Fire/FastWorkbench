package net.minecraft.inventory.container;

import net.minecraft.item.ItemStack;

public class DumbShitTM {

	public static boolean mergeItemStack(Container container, ItemStack stack, int start, int end) {
		return container.moveItemStackTo(stack, start, end, true);
	}

}
