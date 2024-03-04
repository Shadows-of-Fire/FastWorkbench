package dev.shadowsoffire.fastbench.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import dev.shadowsoffire.fastbench.api.ICraftingContainer;
import dev.shadowsoffire.fastbench.api.ICraftingScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;

@Mixin(value = CraftingScreen.class, remap = false)
public class MixinCraftingScreen implements ICraftingScreen {

    @Override
    public ICraftingContainer getContainer() {
        return (ICraftingContainer) this.ths().getMenu();
    }

    private CraftingScreen ths() {
        return (CraftingScreen) (Object) this;
    }
}
