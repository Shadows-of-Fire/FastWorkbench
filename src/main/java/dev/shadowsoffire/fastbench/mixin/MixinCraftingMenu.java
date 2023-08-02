package dev.shadowsoffire.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.shadowsoffire.fastbench.api.ICraftingContainer;
import dev.shadowsoffire.fastbench.util.CraftResultSlotExt;
import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

@Mixin(CraftingMenu.class)
public abstract class MixinCraftingMenu extends RecipeBookMenu<CraftingContainer> implements ICraftingContainer {

    public MixinCraftingMenu(MenuType<?> type, int id) {
        super(type, id);
    }

    @Shadow
    private Player player;

    @Shadow
    private ContainerLevelAccess access;

    @Redirect(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At(value = "NEW", target = "Lnet/minecraft/world/inventory/TransientCraftingContainer;"), require = 1)
    private static TransientCraftingContainer makeExtInv(AbstractContainerMenu container, int x, int y) {
        return new CraftingInventoryExt(container, x, y);
    }

    @Redirect(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At(value = "NEW", target = "net/minecraft/world/inventory/ResultSlot"), require = 1)
    private static ResultSlot makeExtSlot(Player pPlayer, CraftingContainer pCraftSlots, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        return new CraftResultSlotExt(pPlayer, pCraftSlots, (ResultContainer) pContainer, pSlot, pXPosition, pYPosition);
    }

    @Inject(at = @At("HEAD"), method = { "quickMoveStack" }, cancellable = true, require = 1)
    public void quickMoveStack(Player pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
        if (pIndex == 0) {
            ci.setReturnValue(FastBenchUtil.handleShiftCraft(this.player, this.ths(), this.ths().slots.get(0), (CraftingInventoryExt) this.ths().craftSlots, this.ths().resultSlots, 10, 46));
        }
    }

    /**
     * @author Shadows
     * @reason FB handles recipe updates
     */
    @Override
    @Overwrite
    public void slotsChanged(Container inventoryIn) {
        this.access.execute((level, pos) -> {
            FastBenchUtil.queueSlotUpdate(level, this.player, (CraftingInventoryExt) this.ths().craftSlots, this.ths().resultSlots);
        });
    }

    private CraftingMenu ths() {
        return (CraftingMenu) (Object) this;
    }

    @Override
    public ResultContainer getResult() {
        return this.ths().resultSlots;
    }

}
