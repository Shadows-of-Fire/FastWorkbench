package shadows.fastbench.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;

public class ClientContainerFastBench extends ContainerFastBench {

	public ClientContainerFastBench(EntityPlayer player, World world, int x, int y, int z) {
		super(player, world, x, y, z);
	}

	@Override
	protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result) {
	}

}