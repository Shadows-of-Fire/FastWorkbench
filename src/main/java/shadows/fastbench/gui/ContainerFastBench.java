package shadows.fastbench.gui;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import shadows.fastbench.FastBench;
import shadows.fastbench.net.LastRecipeMessage;

public class ContainerFastBench extends WorkbenchContainer {

	protected final World world;
	public IRecipe<CraftingInventory> lastRecipe;
	protected IRecipe<CraftingInventory> lastLastRecipe;
	protected final int x;
	protected final int y;
	protected final int z;
	protected final BlockPos pos;
	protected boolean checkMatrixChanges = true;
	protected boolean useNormalTransfer = false;
	protected PlayerEntity player;

	public ContainerFastBench(int id, PlayerEntity player, World world, int x, int y, int z) {
		this(id, player, world, new BlockPos(x, y, z));
	}

	public ContainerFastBench(int id, PlayerInventory pInv) {
		this(id, pInv.player, pInv.player.world, 0, 0, 0);
	}

	public ContainerFastBench(int id, PlayerEntity player, World world, BlockPos pos) {
		super(id, player.inventory, IWorldPosCallable.of(world, pos));
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();
		this.world = world;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.pos = pos;
		this.player = player;

		this.addSlot(new SlotCraftingSucks(this, player, this.field_75162_e, this.field_75160_f, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.field_75162_e, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(player.inventory, l, 8 + l * 18, 142));
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		if (this.world.getBlockState(this.pos).getBlock() != Blocks.CRAFTING_TABLE) {
			return false;
		} else {
			return playerIn.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.slotChangedCraftingGrid(world, player, field_75162_e, field_75160_f);
	}

	protected void slotChangedCraftingGrid(World world, PlayerEntity player, CraftingInventory inv, CraftResultInventory result) {
		if (!world.isRemote) {

			ItemStack itemstack = ItemStack.EMPTY;

			if (checkMatrixChanges && (lastRecipe == null || !lastRecipe.matches(inv, world))) lastRecipe = findRecipe(inv, world);

			if (lastRecipe != null) {
				itemstack = lastRecipe.getCraftingResult(inv);
			}

			result.setInventorySlotContents(0, itemstack);
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
			if (lastLastRecipe != lastRecipe) entityplayermp.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, itemstack));
			else if (lastLastRecipe != null && lastLastRecipe == lastRecipe && !ItemStack.areItemStacksEqual(lastLastRecipe.getCraftingResult(inv), lastRecipe.getCraftingResult(inv))) entityplayermp.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, itemstack));
			FastBench.CHANNEL.sendTo(new LastRecipeMessage(lastRecipe), entityplayermp.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

			lastLastRecipe = lastRecipe;
		}
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		if (pos != BlockPos.ZERO) super.onContainerClosed(player);
		else {
			PlayerInventory inv = player.inventory;
			if (!inv.getItemStack().isEmpty()) {
				player.dropItem(inv.getItemStack(), false);
				inv.setItemStack(ItemStack.EMPTY);
			}
			if (!this.world.isRemote) this.clearContainer(player, this.world, this.field_75162_e);
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		if (useNormalTransfer || !FastBench.experimentalShiftCrafting || index != 0) return super.transferStackInSlot(player, index);

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			checkMatrixChanges = false;
			while (lastRecipe != null && lastRecipe.matches(this.field_75162_e, this.world)) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();

				itemstack1.getItem().onCreated(itemstack1, this.world, player);

				if (!world.isRemote && !this.mergeItemStack(itemstack1, 10, 46, true)) {
					checkMatrixChanges = true;
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
				slot.onSlotChanged();

				if (!world.isRemote && itemstack1.getCount() == itemstack.getCount()) {
					checkMatrixChanges = true;
					return ItemStack.EMPTY;
				}

				ItemStack itemstack2 = slot.onTake(player, itemstack1);
				player.dropItem(itemstack2, false);
			}
			checkMatrixChanges = true;
			this.slotChangedCraftingGrid(world, player, field_75162_e, field_75160_f);
		}
		return lastRecipe == null ? ItemStack.EMPTY : itemstack;
	}

	public void updateLastRecipe(IRecipe<CraftingInventory> rec) {
		this.lastLastRecipe = lastRecipe;
		this.lastRecipe = rec;
		if (rec != null) this.field_75160_f.setInventorySlotContents(0, rec.getCraftingResult(field_75162_e));
		else this.field_75160_f.setInventorySlotContents(0, ItemStack.EMPTY);
	}

	public static IRecipe<CraftingInventory> findRecipe(CraftingInventory inv, World world) {
		return world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, inv, world).orElse(null);
	}

	@Override
	public ContainerType<?> getType() {
		return FastBench.FAST_CRAFTING;
	}

}