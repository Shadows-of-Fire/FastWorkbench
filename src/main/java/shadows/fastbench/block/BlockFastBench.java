package shadows.fastbench.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import shadows.fastbench.gui.ContainerFastBench;

public class BlockFastBench extends BlockWorkbench implements IInteractionObject {

	public BlockFastBench() {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		player.addStat(StatList.INTERACT_WITH_CRAFTING_TABLE);
		NetworkHooks.openGui((EntityPlayerMP) player, this, pos);
		if (player.openContainer instanceof ContainerFastBench) {
			((ContainerFastBench) player.openContainer).setPos(pos);
		}
		return true;
	}

	@Override
	public ITextComponent getName() {
		return new TextComponentTranslation(this.getTranslationKey());
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getCustomName() {
		return getName();
	}

	@Override
	public Container createContainer(InventoryPlayer inv, EntityPlayer player) {
		return new ContainerFastBench(player, player.world, player.getPosition());
	}

	@Override
	public String getGuiID() {
		return this.getRegistryName().toString();
	}
}