package shadows.fastbench.block;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shadows.fastbench.FastBench;

public class BlockFastBench extends BlockWorkbench {

	public BlockFastBench() {
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("workbench");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		player.openGui(FastBench.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}