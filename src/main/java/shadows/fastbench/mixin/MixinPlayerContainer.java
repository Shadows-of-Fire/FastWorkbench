package shadows.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.util.CraftResultSlotExt;
import shadows.fastbench.util.CraftingInventoryExt;
import shadows.fastbench.util.FastBenchUtil;

@Mixin(InventoryMenu.class)
public abstract class MixinPlayerContainer extends RecipeBookMenu<CraftingContainer> implements ICraftingContainer {

	public MixinPlayerContainer(MenuType<?> p_i50067_1_, int p_i50067_2_) {
		super(p_i50067_1_, p_i50067_2_);
	}

	@Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/world/inventory/CraftingContainer"), require = 1)
	private static CraftingContainer makeExtInv(AbstractContainerMenu container, int x, int y) {
		return new CraftingInventoryExt(container, x, y);
	}

	@Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/world/inventory/ResultSlot"), require = 1)
	private static ResultSlot makeExtSlot(Player pPlayer, CraftingContainer pCraftSlots, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
		return new CraftResultSlotExt(pPlayer, pCraftSlots, (ResultContainer) pContainer, pSlot, pXPosition, pYPosition);
	}

	@Overwrite
	public void slotsChanged(Container inv) {
		FastBenchUtil.slotChangedCraftingGrid(ths().owner.level, ths().owner, (CraftingInventoryExt) ths().craftSlots, ths().resultSlots);
	}

	@Inject(at = @At("HEAD"), method = { "quickMoveStack" }, cancellable = true, require = 1)
	public void quickMoveStack(Player pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
		if (pIndex == 0) {
			ci.setReturnValue(FastBenchUtil.handleShiftCraft(ths().owner, ths(), ths().slots.get(0), (CraftingInventoryExt) ths().craftSlots, ths().resultSlots, 9, 45));
		}
	}

	private InventoryMenu ths() {
		return ((InventoryMenu) (Object) this);
	}

	@Override
	public ResultContainer getResult() {
		return ths().resultSlots;
	}
}
