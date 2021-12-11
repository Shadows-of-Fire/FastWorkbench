package shadows.fastbench.net;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import shadows.fastbench.api.ICraftingContainer;
import shadows.fastbench.api.ICraftingScreen;
import shadows.placebo.network.MessageHelper;
import shadows.placebo.network.MessageProvider;

public class RecipeMessage implements MessageProvider<RecipeMessage> {

	public static final ResourceLocation NULL = new ResourceLocation("null", "null");

	ResourceLocation recipeId;
	ItemStack output;

	public RecipeMessage() {
	}

	public RecipeMessage(Recipe<CraftingContainer> recipeId, ItemStack output) {
		this.recipeId = recipeId == null ? NULL : recipeId.getId();
		this.output = output;
	}

	public RecipeMessage(ResourceLocation recipeId, ItemStack output) {
		this.recipeId = recipeId;
		this.output = output;
	}

	@Override
	public Class<RecipeMessage> getMsgClass() {
		return RecipeMessage.class;
	}

	@Override
	public RecipeMessage read(FriendlyByteBuf buf) {
		ResourceLocation rec = new ResourceLocation(buf.readUtf());
		return new RecipeMessage(rec, rec.equals(NULL) ? ItemStack.EMPTY : buf.readItem());
	}

	@Override
	public void write(RecipeMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.recipeId.toString());
		if (!msg.recipeId.equals(NULL)) buf.writeItem(msg.output);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(RecipeMessage msg, Supplier<Context> ctx) {
		MessageHelper.handlePacket(() -> () -> {
			Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) Minecraft.getInstance().level.getRecipeManager().byKey(msg.recipeId).orElse(null);
			if (Minecraft.getInstance().screen instanceof ICraftingScreen) {
				ICraftingContainer c = ((ICraftingScreen) Minecraft.getInstance().screen).getContainer();
				updateLastRecipe(c.getResult(), recipe, msg.output);
			} else if (Minecraft.getInstance().screen instanceof InventoryScreen) {
				InventoryMenu c = ((InventoryScreen) Minecraft.getInstance().screen).getMenu();
				updateLastRecipe(c.resultSlots, recipe, msg.output);
			}
		}, ctx);
	}

	public static void updateLastRecipe(ResultContainer craftResult, Recipe<CraftingContainer> rec, ItemStack output) {
		craftResult.setRecipeUsed(rec);
		craftResult.setItem(0, output);
	}

}
