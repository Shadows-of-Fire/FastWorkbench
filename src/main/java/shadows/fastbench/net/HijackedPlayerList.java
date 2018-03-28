package shadows.fastbench.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServer;
import shadows.fastbench.FastBench;

public class HijackedPlayerList extends IntegratedPlayerList {

	public HijackedPlayerList(IntegratedServer server) {
		super(server);
	}

	@Override
	public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn, NetHandlerPlayServer nethandlerplayserver) {
		playerIn.recipeBook = FastBench.SERVER_BOOK;
		super.initializeConnectionToPlayer(netManager, playerIn, nethandlerplayserver);
	}
}
