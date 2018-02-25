package shadows.fastbench.gui;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.fastbench.FastBench;

@SideOnly(Side.CLIENT)
public class GuiFastBench extends GuiCrafting {

	public GuiFastBench(InventoryPlayer inv, World world) {
		this(inv, world, BlockPos.ORIGIN);
	}

	public GuiFastBench(InventoryPlayer inv, World world, BlockPos pos) {
		super(inv, world, pos);
		this.inventorySlots = new ContainerFastBench(inv.player, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (FastBench.delet) {
			this.buttonList.clear();
			this.recipeButton = null;
			this.recipeBookGui = new GuiDedBook();
		}
	}
}