package shadows.fastbench.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.DumbShitTM;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import shadows.fastbench.FastBench;
import shadows.fastbench.net.RecipeMessage;

@SuppressWarnings("unchecked")
public class ContainerFastBench extends WorkbenchContainer {

	protected final World world;
	protected final BlockPos pos;
	protected PlayerEntity player;

	public ContainerFastBench(int id, PlayerInventory pInv) {
		this(id, pInv.player, pInv.player.world, BlockPos.ZERO);
	}

	public ContainerFastBench(int id, PlayerEntity player, World world, BlockPos pos) {
		super(id, player.inventory, IWorldPosCallable.of(world, pos));
		this.field_75162_e = new CraftingInventoryExt(this, 3, 3);
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();
		this.world = world;
		this.pos = pos;
		this.player = player;

		this.addSlot(new SlotCraftingSucks(player, this.field_75162_e, this.field_75160_f, 0, 124, 35));

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

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		slotChangedCraftingGrid(world, player, (CraftingInventoryExt) field_75162_e, field_75160_f);
	}

	public static void slotChangedCraftingGrid(World world, PlayerEntity player, CraftingInventoryExt inv, CraftResultInventory result) {
		if (!world.isRemote) {

			ItemStack itemstack = ItemStack.EMPTY;

			IRecipe<CraftingInventory> recipe = (IRecipe<CraftingInventory>) result.getRecipeUsed();
			if (recipe == null || !recipe.matches(inv, world)) recipe = findRecipe(inv, world);

			if (recipe != null) {
				itemstack = recipe.getCraftingResult(inv);
			}

			result.setInventorySlotContents(0, itemstack);
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
			FastBench.CHANNEL.sendTo(new RecipeMessage(recipe), entityplayermp.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

			result.setRecipeUsed(recipe);
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		if (index != 0) return super.transferStackInSlot(player, index);
		return handleShiftCraft(player, this, this.inventorySlots.get(index), (CraftingInventoryExt) field_75162_e, field_75160_f, 10, 46);
	}

	public static ItemStack handleShiftCraft(PlayerEntity player, Container container, Slot resultSlot, CraftingInventoryExt field_75162_e, CraftResultInventory field_75160_f, int outStart, int outEnd) {
		ItemStack outputCopy = ItemStack.EMPTY;

		if (resultSlot != null && resultSlot.getHasStack()) {
			field_75162_e.checkChanges = false;
			IRecipe<CraftingInventory> recipe = (IRecipe<CraftingInventory>) field_75160_f.getRecipeUsed();
			while (recipe != null && recipe.matches(field_75162_e, player.world)) {
				ItemStack recipeOutput = resultSlot.getStack().copy();
				outputCopy = recipeOutput.copy();

				recipeOutput.getItem().onCreated(recipeOutput, player.world, player);

				if (!player.world.isRemote && !DumbShitTM.mergeItemStack(container, recipeOutput, outStart, outEnd)) {
					field_75162_e.checkChanges = true;
					return ItemStack.EMPTY;
				}

				resultSlot.onSlotChange(recipeOutput, outputCopy);
				resultSlot.onSlotChanged();

				if (!player.world.isRemote && recipeOutput.getCount() == outputCopy.getCount()) {
					field_75162_e.checkChanges = true;
					return ItemStack.EMPTY;
				}

				ItemStack itemstack2 = resultSlot.onTake(player, recipeOutput);
				player.dropItem(itemstack2, false);
			}
			field_75162_e.checkChanges = true;
			slotChangedCraftingGrid(player.world, player, field_75162_e, field_75160_f);
		}
		field_75162_e.checkChanges = true;
		return field_75160_f.getRecipeUsed() == null ? ItemStack.EMPTY : outputCopy;
	}

	public static IRecipe<CraftingInventory> findRecipe(CraftingInventory inv, World world) {
		return world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, inv, world).orElse(null);
	}

	@Override
	public ContainerType<?> getType() {
		return FastBench.FAST_CRAFTING;
	}

}