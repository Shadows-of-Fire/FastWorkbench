package shadows.fastbench.net;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import shadows.fastbench.gui.GuiFastBench;

public class LastRecipeMessage {

	public LastRecipeMessage() {
	}

	IRecipe rec;

	public LastRecipeMessage(IRecipe toSend) {
		rec = toSend;
	}

	public static LastRecipeMessage read(PacketBuffer buf) {
		return new LastRecipeMessage(RecipeSerializers.read(buf));
	}

	public static void write(LastRecipeMessage msg, PacketBuffer buf) {
		RecipeSerializers.write(msg.rec, buf);
	}

	public static void handle(LastRecipeMessage message, Supplier<NetworkEvent.Context> context) {
		if (Minecraft.getInstance().currentScreen instanceof GuiFastBench) ((GuiFastBench) Minecraft.getInstance().currentScreen).getContainer().lastRecipe = message.rec;

	}

}
