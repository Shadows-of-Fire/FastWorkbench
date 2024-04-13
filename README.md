# FastWorkbench [![](http://cf.way2muchnoise.eu/fastworkbench.svg)](https://www.curseforge.com/minecraft/mc-mods/fastworkbench) [![](http://cf.way2muchnoise.eu/versions/fastworkbench.svg)](https://www.curseforge.com/minecraft/mc-mods/fastworkbench)
FastWorkbench causes all crafting operations to cache the last recipe used. This resolves the game freeze that triggers when shift-click (batch) crafting a full stack of items when many recipes are loaded, since the number of match ops is reduced to one.

## Technical Details
Minecraft retains a list of recipes, which are a mapping from 1-9 inputs to a single output. Vanilla has about 2,000 recipes, but large modpacks will typically reach 20,000 or higher.
When using a crafting grid, any time an input item changes, a full iteration of this list is performed to compute the updated output. This match operation is O(r) on the number of loaded recipes `r`, and as such, is fairly expensive.

When a user crafts an item (picking it up from the output slot), all the inputs are decremented individually. Each decrement triggers another match operation, which means that crafting a recipe triggers O(i) match ops on the number of inputs `i`.
Compounding this further, Minecraft permits batch crafting via shift-click, which crafts all items until the input is extinguished. This single operation can then trigger O(i * s) match ops, on the number of inputs `i` times the stack size of the inputs `s`.

The result is several hundred match operations executed immediately as a result of a normal game operation (batch crafting), which stalls out the game when the recipe list is large.

FastWorkbench optimizes this logic such that only one match op is performed for any crafting op (batch and non-batch). Additionally, it prevents duplication of work by not computing the matched recipe on the client as Minecraft does.

## Adding FastWorkbench Compatibility
Adding compatibility for FastWorkbench to modded crafting tables can be divided into two paths:

#### 1. If you are extending CraftingMenu:  
Everything will be handled automatically, as long as you do not override the methods `quickMoveStack` or `slotsChanged`.  If you override these methods, you need to match the implementation found in FastWorkbench's overrides [here](https://github.com/Shadows-of-Fire/FastWorkbench/blob/1.20/src/main/java/dev/shadowsoffire/fastbench/mixin/MixinCraftingMenu.java).

#### 2. If you are not extending CraftingMenu:  
You will need to use the FastWorkbench classes `CraftingInventoryExt` and `CraftResultSlotExt` in place of `TransientCraftingContainer` and `ResultSlot` respectively.

Additionally, you will need to call `FastBenchUtil.queueSlotUpdate` from your `slotsChanged` method (or other mechanism you use to detect crafting grid updates), and call `FastBenchUtil.handleShiftCraft` from your `quickMoveStack` function when the output slot is clicked.
