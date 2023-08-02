package dev.shadowsoffire.fastbench.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import dev.shadowsoffire.fastbench.util.DumbShitTM;
import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

// https://github.com/Shadows-of-Fire/FastWorkbench/issues/87 - Quark's backpack overrides quickMoveStack, we need to ensure this variant of handleShiftCraft is called.
@Pseudo
@Mixin(targets = { "vazkii.quark.addons.oddities.inventory.BackpackMenu" })
public class MixinBackpackMenu extends InventoryMenu {

	public MixinBackpackMenu(Inventory p_39706_, boolean p_39707_, Player p_39708_) {
		super(p_39706_, p_39707_, p_39708_);
	}

	@Inject(at = @At("HEAD"), method = { "quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;" }, cancellable = true, require = 1)
	public void quickMoveStack(Player pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> ci) {
		if (pIndex == 0) {
			final int topSlots = 8;
			final int invStart = topSlots + 1;
			final int invEnd = invStart + 27;
			final int hotbarStart = invEnd;
			final int hotbarEnd = hotbarStart + 9;
			final int shieldSlot = hotbarEnd;
			final int backpackStart = shieldSlot + 1;
			final int backpackEnd = backpackStart + 27;
			ci.setReturnValue(FastBenchUtil.handleShiftCraft(this.ths().owner, this.ths(), this.ths().slots.get(0), (CraftingInventoryExt) this.ths().craftSlots, this.ths().resultSlots, (container, player) -> {
				if (DumbShitTM.mergeItemStack(container, player, backpackStart, backpackEnd)) return false;
				if (DumbShitTM.mergeItemStack(container, player, invStart, invEnd)) return false;
				if (DumbShitTM.mergeItemStack(container, player, hotbarStart, hotbarEnd)) return false;
				return true;
			}));
		}
	}

	private InventoryMenu ths() {
		return (InventoryMenu) (Object) this;
	}
}
