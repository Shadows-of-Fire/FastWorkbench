package shadows.fastbench.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import shadows.fastbench.gui.GuiFastBench;

public class LastRecipeMessage implements IMessage {

	public LastRecipeMessage() {
	}

	IRecipe rec;

	public LastRecipeMessage(IRecipe toSend) {
		rec = toSend;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		rec = CraftingManager.REGISTRY.getObjectById(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(CraftingManager.REGISTRY.getIDForObject(rec));
	}

	public static class Handler implements IMessageHandler<LastRecipeMessage, IMessage> {

		@Override
		public IMessage onMessage(LastRecipeMessage message, MessageContext ctx) {
			if (Minecraft.getMinecraft().currentScreen instanceof GuiFastBench) ((GuiFastBench) Minecraft.getMinecraft().currentScreen).getContainer().lastRecipe = message.rec;
			return null;
		}

	}

}
