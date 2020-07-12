package net.minecraft.inventory.container;

import net.minecraft.item.ItemStack;
import shadows.fastbench.gui.ContainerFastBench;

/**
 * This class is needed so I can invoke this for any container from {@link ContainerFastBench#handleShiftCraft}
 */
public class DumbShitTM {

	public static boolean mergeItemStack(Container container, ItemStack stack, int start, int end) {
		return container.mergeItemStack(stack, start, end, true);
	}

}
