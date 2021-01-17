# FastWorkbench
Caching recipes in the crafting table


## Developers Wishing to Add Support
Adding compatability for FastWorkbench to modded crafting tables is made to be as painless as possible.  There are only five changes that need to be made from the standard
vanilla table, and FastWorkbench will handle the rest.

### If you are extending WorkbenchContainer:  
You need to replace the existing `CraftingInventory` field (`WorkbenchContainer.craftMatrix`) with an instance of `CraftingInventoryExt` [as shown here.](https://github.com/Shadows-of-Fire/FastWorkbench/blob/1.16/src/main/java/shadows/fastbench/gui/ContainerFastBench.java#L36)

You then need to override `onCraftMatrixChanged` to invoke `ContainerFastBench.slotChangedCraftingGrid`.  It need not make any other calls.

Third, you need to override `transferStackInSlot` such that it returns `ContainerFastBench.handleShiftCraft` when called for the `CraftingResultSlot`.  In this case, that slot is index 0.  For all other slots, simply return super.

Fourth, you need to implement `ICraftingContainer` on your container, and `ICraftingScreen` on your screen.  These allow the packet to process properly.  These classes are located in `shadows.fastbench.api` and may be repacked with any mod.

Finally, you need to replace any instances of `CraftingResultSlot` with `CraftResultSlotExt`.  Failure to do so will cause all kinds of shenanigans.

### If you are not extending WorkbenchContainer:  
You need to make similar changes as described above, but you will need to find the right slot numbers and changes for your implementation of a crafting screen.  The FastWorkbench solutions are generally equipped to allow any container to implement their functions seamlessly.
