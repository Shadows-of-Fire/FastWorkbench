## 6.1.0
* Allowed the API to handle cases where you need to move crafted stacks to multiple locations.
* Fixed shift-click-crafting issues with Quark Oddities' Backpack.
* RoyalReject: Added a config for the workbench tooltip.

## 6.0.2
* Rebuilt for 1.18.2
* dracnis: Added French Translation.

## 6.0.1
* Updated to Placebo 6.1.0

## 6.0.0
* Updated to 1.18.1

## 5.0.1
* Ported changes from 4.6.1 to 1.17.1

## 5.0.0
* Ported to 1.17.1

## 4.6.1
* Fixed a dupe bug that could occur if the player inventory was full when using a workbench.
* FB will now crash the game when an invalid recipe (a recipe that matches but produces no output) is detected, instead of infinitely looping.

## 4.6.0
* The mod has been rewritten to use mixins instead of JS coremods and registry replacement.
* Some unnecessary calls during the shift-crafting process have been trimmed.
* Non-vanilla non-special recipes that have their output contents change anyway will be properly updated.