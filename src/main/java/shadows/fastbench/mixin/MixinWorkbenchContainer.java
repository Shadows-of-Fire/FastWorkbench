package shadows.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.util.CraftResultSlotExt;
import shadows.fastbench.util.CraftingInventoryExt;
import shadows.fastbench.util.FastBenchUtil;

@Mixin(WorkbenchContainer.class)
public abstract class MixinWorkbenchContainer extends RecipeBookContainer<CraftingInventory> implements ICraftingContainer {

	public MixinWorkbenchContainer(ContainerType<?> p_i50067_1_, int p_i50067_2_) {
		super(p_i50067_1_, p_i50067_2_);
	}

	@Shadow
	private PlayerEntity player;

	@Shadow
	private IWorldPosCallable access;

	@Redirect(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At(value = "NEW", target = "Lnet/minecraft/inventory/CraftingInventory;"))
	private static CraftingInventory makeExtInv(Container container, int x, int y) {
		return new CraftingInventoryExt(container, x, y);
	}

	@Redirect(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At(value = "NEW", target = "net/minecraft/inventory/container/CraftingResultSlot"))
	private static CraftingResultSlot makeExtSlot(PlayerEntity pPlayer, CraftingInventory pCraftSlots, IInventory pContainer, int pSlot, int pXPosition, int pYPosition) {
		return new CraftResultSlotExt(pPlayer, pCraftSlots, (CraftResultInventory) pContainer, pSlot, pXPosition, pYPosition);
	}

	@Inject(at = @At("HEAD"), method = { "quickMoveStack" }, cancellable = true)
	public void quickMoveStack(PlayerEntity pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
		if (pIndex == 0) {
			ci.setReturnValue(FastBenchUtil.handleShiftCraft(player, ths(), ths().slots.get(0), (CraftingInventoryExt) ths().craftSlots, ths().resultSlots, 9, 45));
		}
	}

	@Overwrite
	public void slotsChanged(IInventory inventoryIn) {
		access.execute((level, pos) -> {
			FastBenchUtil.slotChangedCraftingGrid(level, player, (CraftingInventoryExt) ths().craftSlots, ths().resultSlots);
		});
	}

	private WorkbenchContainer ths() {
		return ((WorkbenchContainer) (Object) this);
	}

	@Override
	public CraftResultInventory getResult() {
		return ths().resultSlots;
	}

}
