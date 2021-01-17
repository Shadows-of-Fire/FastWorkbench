package shadows.fastbench.asm;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.CraftResultSlotExt;
import shadows.fastbench.gui.CraftingInventoryExt;

public class PlayerContainerHooks {

	/**
	 * ASM Hook: Called from {@link PlayerContainer#onCraftMatrixChanged(IInventory)} <br>
	 * Vanilla code is nulled out, this method is invoked and a return is inserted afterwards.
	 * @param ctr The player container.
	 */
	public static void onCraftMatrixChanged(PlayerContainer ctr) {
		ContainerFastBench.slotChangedCraftingGrid(ctr.player.world, ctr.player, (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult);
	}

	/**z	
	 * ASM Hook: Called from {@link PlayerContainer#transferStackInSlot(PlayerEntity, int)} <br>
	 * Vanilla code is nulled out, this method is invoked and a return is inserted afterwards.
	 * @param ctr The player container.
	 * @return Something, the way this method works makes no sense.
	 */
	public static ItemStack transferStackInSlot(PlayerContainer ctr) {
		return ContainerFastBench.handleShiftCraft(ctr.player, ctr, ctr.inventorySlots.get(0), (CraftingInventoryExt) ctr.craftMatrix, ctr.craftResult, 9, 45);
	}

	/**
	 * ASM Hook: Called from {@link PlayerContainer#PlayerContainer(PlayerInventory, boolean, PlayerEntity)} <br>
	 * Used to replace the {@link CraftingInventory} and {@link CraftResultSlot} instances.
	 * @param ctr The player container.
	 */
	public static void updatePlayerContainer(PlayerContainer ctr) {
		if (ctr.inventorySlots.get(0) instanceof CraftResultSlotExt) return; //Already replaced this one, do nothing.
		CraftingInventoryExt inv = new CraftingInventoryExt(ctr, 2, 2);
		ctr.craftMatrix = inv;
		for (int i = 0; i < 5; i++) {
			Slot s = ctr.inventorySlots.get(i);
			if (i == 0) {
				CraftResultSlotExt craftSlot = new CraftResultSlotExt(ctr.player, ctr.craftMatrix, ctr.craftResult, 0, 154, 28);
				craftSlot.slotNumber = 0;
				ctr.inventorySlots.set(0, craftSlot);
			} else {
				ObfuscationReflectionHelper.setPrivateValue(Slot.class, s, inv, "field_75224_c");
			}
		}
	}

}
