package dev.shadowsoffire.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@Mixin(value = InventoryMenu.class, remap = false)
public abstract class MixinInventoryMenu extends AbstractCraftingMenu {

    public MixinInventoryMenu(MenuType<?> type, int id, int w, int h) {
        super(type, id, w, h);
    }

    @Inject(at = @At("HEAD"), method = { "quickMoveStack" }, cancellable = true, require = 1)
    public void quickMoveStack(Player pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
        if (pIndex == 0) {
            ci.setReturnValue(FastBenchUtil.handleShiftCraft(this.ths().owner, this.ths(), this.ths().slots.get(0), (CraftingInventoryExt) this.ths().craftSlots, this.ths().resultSlots, 9, 45));
        }
    }

    private InventoryMenu ths() {
        return (InventoryMenu) (Object) this;
    }

}
