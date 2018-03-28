package shadows.fastbench.proxy;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import shadows.fastbench.FastBench;
import shadows.fastbench.book.DedClientBook;
import shadows.fastbench.net.HijackedPlayerList;

public class BenchClientProxy implements IBenchProxy {

	public static final DedClientBook CLIENT_BOOK = new DedClientBook();

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof EntityPlayerMP) ((EntityPlayerMP) e).recipeBook = FastBench.SERVER_BOOK;
		if (e instanceof EntityPlayerSP) ((EntityPlayerSP) e).recipeBook = CLIENT_BOOK;
	}

	@Override
	public void replacePlayerList(MinecraftServer server) {
		if (!(server.getPlayerList() instanceof HijackedPlayerList)) server.setPlayerList(new HijackedPlayerList((IntegratedServer) server));
	}

}
