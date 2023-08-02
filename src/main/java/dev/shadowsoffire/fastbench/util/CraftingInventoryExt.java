package dev.shadowsoffire.fastbench.util;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;

public class CraftingInventoryExt extends TransientCraftingContainer {

    /**
     * If this boolean is changed to false, then invocations to {@link AbstractContainerMenu#slotsChanged} will not incur a recipe re-lookup.<br>
     * It is very important that this state is properly reset, as otherwise the grid will fail to update.
     */
    public boolean checkChanges = true;

    public CraftingInventoryExt(AbstractContainerMenu container, int width, int height) {
        super(container, width, height);
    }

}
