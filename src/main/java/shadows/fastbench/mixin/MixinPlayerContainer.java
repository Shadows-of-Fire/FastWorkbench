package shadows.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.ItemStack;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.util.CraftResultSlotExt;
import shadows.fastbench.util.CraftingInventoryExt;
import shadows.fastbench.util.FastBenchUtil;

@Mixin(PlayerContainer.class)
public abstract class MixinPlayerContainer extends RecipeBookContainer<CraftingInventory> implements ICraftingContainer {

	public MixinPlayerContainer(ContainerType<?> p_i50067_1_, int p_i50067_2_) {
		super(p_i50067_1_, p_i50067_2_);
	}

	@Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/inventory/CraftingInventory"))
	private static CraftingInventory makeExtInv(Container container, int x, int y) {
		return new CraftingInventoryExt(container, x, y);
	}

	@Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/inventory/container/CraftingResultSlot"))
	private static CraftingResultSlot makeExtSlot(PlayerEntity pPlayer, CraftingInventory pCraftSlots, IInventory pContainer, int pSlot, int pXPosition, int pYPosition) {
		return new CraftResultSlotExt(pPlayer, pCraftSlots, (CraftResultInventory) pContainer, pSlot, pXPosition, pYPosition);
	}

	@Overwrite
	public void slotsChanged(IInventory inv) {
		FastBenchUtil.slotChangedCraftingGrid(ths().owner.level, ths().owner, (CraftingInventoryExt) ths().craftSlots, ths().resultSlots);
	}

	@Inject(at = @At("HEAD"), method = { "quickMoveStack" }, cancellable = true)
	public void quickMoveStack(PlayerEntity pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
		if (pIndex == 0) {
			ci.setReturnValue(FastBenchUtil.handleShiftCraft(ths().owner, ths(), ths().slots.get(0), (CraftingInventoryExt) ths().craftSlots, ths().resultSlots, 9, 45));
		}
	}

	private PlayerContainer ths() {
		return ((PlayerContainer) (Object) this);
	}

	@Override
	public CraftResultInventory getResult() {
		return ths().resultSlots;
	}
}
