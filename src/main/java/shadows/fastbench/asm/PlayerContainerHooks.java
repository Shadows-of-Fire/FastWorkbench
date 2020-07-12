package shadows.fastbench.asm;

import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.CraftingInventoryExt;

public class PlayerContainerHooks {

	public static void onCraftMatrixChanged(PlayerContainer ctr) {
		ContainerFastBench.slotChangedCraftingGrid(ctr.player.world, ctr.player, (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult);
	}

	public static ItemStack transferStackInSlot(PlayerContainer ctr) {
		return ContainerFastBench.handleShiftCraft(ctr.player, ctr, ctr.inventorySlots.get(0), (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult, 9, 45);
	}

}
