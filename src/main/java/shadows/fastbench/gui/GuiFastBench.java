package shadows.fastbench.gui;

import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shadows.fastbench.FastBench;

public class GuiFastBench extends GuiCrafting {

	public GuiFastBench(InventoryPlayer inv, World world) {
		this(inv, world, BlockPos.ORIGIN);
	}

	public GuiFastBench(InventoryPlayer inv, World world, BlockPos pos) {
		super(inv, world, pos);
		this.inventorySlots = new ContainerFastBench(inv.player, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void initGui() {
		super.initGui();
		if (FastBench.removeRecipeBook) {
			this.buttons.clear();
			this.buttons.removeIf(b -> b.id == 10 && b instanceof GuiButtonImage);
			this.recipeBookGui = new GuiDedBook();
		}
	}

	public ContainerFastBench getContainer() {
		return (ContainerFastBench) this.inventorySlots;
	}
}