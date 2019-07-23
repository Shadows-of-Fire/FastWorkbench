package shadows.fastbench.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import shadows.fastbench.FastBench;

public class HijackedDedicatedPlayerList extends DedicatedPlayerList {

	public HijackedDedicatedPlayerList(DedicatedServer server) {
		super(server);
	}

	@Override
	public void initializeConnectionToPlayer(NetworkManager netManager, ServerPlayerEntity playerIn) {
		playerIn.recipeBook = FastBench.SERVER_BOOK;
		super.initializeConnectionToPlayer(netManager, playerIn);
	}
}
