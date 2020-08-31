package shadows.fastbench.net;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.GuiFastBench;
import shadows.placebo.util.NetworkUtils;
import shadows.placebo.util.NetworkUtils.MessageProvider;

public class RecipeMessage extends MessageProvider<RecipeMessage> {

	public static final ResourceLocation NULL = new ResourceLocation("null", "null");

	ResourceLocation rec;

	public RecipeMessage() {
	}

	public RecipeMessage(IRecipe<CraftingInventory> toSend) {
		rec = toSend == null ? NULL : toSend.getId();
	}

	public RecipeMessage(ResourceLocation toSend) {
		rec = toSend;
	}

	@Override
	public Class<RecipeMessage> getMsgClass() {
		return RecipeMessage.class;
	}

	@Override
	public RecipeMessage read(PacketBuffer buf) {
		return new RecipeMessage(new ResourceLocation(buf.readString()));
	}

	@Override
	public void write(RecipeMessage msg, PacketBuffer buf) {
		buf.writeString(msg.rec.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(RecipeMessage msg, Supplier<Context> ctx) {
		NetworkUtils.handlePacket(() -> () -> {
			IRecipe<CraftingInventory> r = (IRecipe<CraftingInventory>) Minecraft.getInstance().world.getRecipeManager().getRecipe(msg.rec).orElse(null);
			if (Minecraft.getInstance().currentScreen instanceof GuiFastBench) {
				ContainerFastBench c = ((GuiFastBench) Minecraft.getInstance().currentScreen).getContainer();
				updateLastRecipe(c.craftMatrix, c.craftResult, r);
			} else if (Minecraft.getInstance().currentScreen instanceof InventoryScreen) {
				PlayerContainer c = ((InventoryScreen) Minecraft.getInstance().currentScreen).getContainer();
				updateLastRecipe(c.craftMatrix, c.craftResult, r);
			}
		}, ctx.get());
	}

	public static void updateLastRecipe(CraftingInventory craftMatrix, CraftResultInventory craftResult, IRecipe<CraftingInventory> rec) {
		craftResult.setRecipeUsed(rec);
		if (rec != null) craftResult.setInventorySlotContents(0, rec.getCraftingResult(craftMatrix));
		else craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
	}

}
