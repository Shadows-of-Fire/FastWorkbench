package shadows.fastbench.net;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import shadows.fastbench.gui.GuiFastBench;
import shadows.placebo.util.NetworkUtils;
import shadows.placebo.util.NetworkUtils.MessageProvider;

public class LastRecipeMessage extends MessageProvider<LastRecipeMessage> {

	public static final ResourceLocation NULL = new ResourceLocation("null", "null");

	ResourceLocation rec;

	public LastRecipeMessage() {
	}

	public LastRecipeMessage(IRecipe<CraftingInventory> toSend) {
		rec = toSend == null ? NULL : toSend.getId();
	}

	public LastRecipeMessage(ResourceLocation toSend) {
		rec = toSend;
	}

	@Override
	public Class<LastRecipeMessage> getMsgClass() {
		return LastRecipeMessage.class;
	}

	@Override
	public LastRecipeMessage read(PacketBuffer buf) {
		return new LastRecipeMessage(new ResourceLocation(buf.readString()));
	}

	@Override
	public void write(LastRecipeMessage msg, PacketBuffer buf) {
		buf.writeString(msg.rec.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(LastRecipeMessage msg, Supplier<Context> ctx) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			NetworkUtils.enqueueClient(() -> {
				if (Minecraft.getInstance().currentScreen instanceof GuiFastBench) {
					IRecipe<?> r = Minecraft.getInstance().world.getRecipeManager().getRecipe(msg.rec).orElse(null);
					((GuiFastBench) Minecraft.getInstance().currentScreen).getContainer().updateLastRecipe((IRecipe<CraftingInventory>) r);
				}
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
