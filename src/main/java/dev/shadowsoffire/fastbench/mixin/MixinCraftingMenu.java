package dev.shadowsoffire.fastbench.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@Mixin(value = CraftingMenu.class, remap = false)
public abstract class MixinCraftingMenu extends AbstractCraftingMenu {

    public MixinCraftingMenu(MenuType<?> type, int id, int w, int h) {
        super(type, id, w, h);
    }

    @Shadow
    private Player player;

    @Shadow
    private ContainerLevelAccess access;

    @Inject(at = @At("HEAD"), method = "quickMoveStack", cancellable = true, require = 1)
    public void quickMoveStack(Player pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
        if (pIndex == 0) {
            ci.setReturnValue(FastBenchUtil.handleShiftCraft(this.player, this.ths(), this.ths().slots.get(0), (CraftingInventoryExt) this.ths().craftSlots, this.ths().resultSlots, 10, 46));
        }
    }

    @Inject(at = @At("HEAD"), method = "slotChangedCraftingGrid", cancellable = true, require = 1)
    private static void fastbench_rewriteSlotChanges(AbstractContainerMenu menu, ServerLevel level, Player player, CraftingContainer container,
        ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipeHint, CallbackInfo ci) {
        if (menu instanceof AbstractCraftingMenu craftMenu) {
            FastBenchUtil.queueSlotUpdate(level, player, (CraftingInventoryExt) craftMenu.craftSlots, resultSlots);
            ci.cancel();
        }
    }

    private CraftingMenu ths() {
        return (CraftingMenu) (Object) this;
    }

}
