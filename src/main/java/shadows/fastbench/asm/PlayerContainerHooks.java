package shadows.fastbench.asm;

import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.CraftingInventoryExt;

public class PlayerContainerHooks {

	public static void onCraftMatrixChanged(PlayerContainer ctr) {
		ContainerFastBench.slotChangedCraftingGrid(ctr.player.world, ctr.player, (CraftingInventoryExt) ctr.field_75181_e, ctr.field_75179_f);
	}

	public static ItemStack transferStackInSlot(PlayerContainer ctr) {
		return ContainerFastBench.handleShiftCraft(ctr.player, ctr, ctr.inventorySlots.get(0), (CraftingInventoryExt) ctr.field_75181_e, ctr.field_75179_f, 9, 45);
	}

}
