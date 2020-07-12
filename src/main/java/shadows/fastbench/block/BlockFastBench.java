package shadows.fastbench.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import shadows.fastbench.gui.ContainerFastBench;

public class BlockFastBench extends CraftingTableBlock {

	private static final ITextComponent NAME = new TranslationTextComponent("container.crafting");

	public BlockFastBench() {
		super(Block.Properties.from(Blocks.CRAFTING_TABLE));
	}

	@Override
	public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, pInv, player) -> {
			return new ContainerFastBench(id, player, world, pos);
		}, NAME);
	}
}