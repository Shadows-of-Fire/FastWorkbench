package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import shadows.fastbench.FastBench;

public class BenchServerProxy implements IBenchProxy {

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof EntityPlayerMP) ((EntityPlayerMP) e).recipeBook = FastBench.SERVER_BOOK;
	}
}
