package shadows.fastbench.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class Handler {

	public static Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == 0) return new ContainerFastBench(player, world, x, y, z);
		return null;
	}

	public static GuiScreen getClientGuiElement(FMLPlayMessages.OpenContainer msg) {
		if (msg.getId().equals(Blocks.CRAFTING_TABLE.getRegistryName())) {
			PacketBuffer buf = msg.getAdditionalData();
			return new GuiFastBench(Minecraft.getInstance().player.inventory, Minecraft.getInstance().world, buf.readBlockPos());
		}
		return null;
	}

}
