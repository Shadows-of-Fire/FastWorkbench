package dev.shadowsoffire.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.shadowsoffire.fastbench.api.ICraftingContainer;
import dev.shadowsoffire.fastbench.util.CraftResultSlotExt;
import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.TransientCraftingContainer;

@Mixin(value = AbstractCraftingMenu.class, remap = false)
public class MixinAbstractCraftingMenu implements ICraftingContainer {

    @Shadow
    private ResultContainer resultSlots;
    
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/inventory/AbstractContainerMenu;II)Lnet/minecraft/world/inventory/TransientCraftingContainer;"), require = 1)
    private TransientCraftingContainer makeExtInv(AbstractContainerMenu container, int x, int y) {
        return new CraftingInventoryExt(container, x, y);
    }

    @Redirect(method = "addResultSlot", at = @At(value = "NEW", target = "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/Container;III)Lnet/minecraft/world/inventory/ResultSlot;"), require = 1)
    private ResultSlot makeExtSlot(Player player, CraftingContainer craftSlots, Container container, int slot, int x, int y) {
        return new CraftResultSlotExt(player, craftSlots, (ResultContainer) container, slot, x, y);
    }

    @Override
    public ResultContainer getResult() {
        return this.resultSlots;
    }
}
