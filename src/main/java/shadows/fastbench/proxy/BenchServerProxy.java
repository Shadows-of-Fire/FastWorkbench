package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import shadows.fastbench.book.DedRecipeBook;
import shadows.fastbench.net.HijackedDedicatedPlayerList;

public class BenchServerProxy implements IBenchProxy {

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof EntityPlayerMP) ((EntityPlayerMP) e).recipeBook = new DedRecipeBook(e.world.getRecipeManager());
	}

	@Override
	public void replacePlayerList(MinecraftServer server) {
		if (!(server.getPlayerList() instanceof HijackedDedicatedPlayerList)) server.setPlayerList(new HijackedDedicatedPlayerList((DedicatedServer) server));
	}

}
