package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public interface IBenchProxy {

	public void deleteBook(Entity e);

	public void replacePlayerList(MinecraftServer server);

}
