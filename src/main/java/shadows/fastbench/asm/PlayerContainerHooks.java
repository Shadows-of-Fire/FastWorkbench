package shadows.fastbench.asm;

import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.CraftingInventoryExt;

public class PlayerContainerHooks {

	/**
	 * ASM Hook: Called from {@link PlayerContainer#onCraftMatrixChanged(net.minecraft.inventory.IInventory)}
	 * Vanilla code is nulled out, this method is invoked and a return is inserted afterwards.
	 * @param ctr The player container.
	 */
	public static void onCraftMatrixChanged(PlayerContainer ctr) {
		ContainerFastBench.slotChangedCraftingGrid(ctr.player.world, ctr.player, (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult);
	}

	/**
	 * ASM Hook: Called from {@link PlayerContainer#transferStackInSlot(net.minecraft.entity.player.PlayerEntity, int)}
	 * Vanilla code is nulled out, this method is invoked and a return is inserted afterwards.
	 * @param ctr The player container.
	 * @return Something, the way this method works makes no sense.
	 */
	public static ItemStack transferStackInSlot(PlayerContainer ctr) {
		return ContainerFastBench.handleShiftCraft(ctr.player, ctr, ctr.inventorySlots.get(0), (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult, 9, 45);
	}

}
