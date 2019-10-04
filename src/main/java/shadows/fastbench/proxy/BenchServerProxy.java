package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import shadows.fastbench.book.DedRecipeBook;
import shadows.fastbench.net.HijackedDedicatedPlayerList;

public class BenchServerProxy implements IBenchProxy {

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof ServerPlayerEntity) ((ServerPlayerEntity) e).recipeBook = new DedRecipeBook();
	}

	@Override
	public void replacePlayerList(MinecraftServer server) {
		if (!(server.getPlayerList() instanceof HijackedDedicatedPlayerList)) server.setPlayerList(new HijackedDedicatedPlayerList((DedicatedServer) server));
	}

}
