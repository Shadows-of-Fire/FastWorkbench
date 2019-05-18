package shadows.fastbench.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import shadows.fastbench.book.DedRecipeBook;

public class HijackedDedicatedPlayerList extends DedicatedPlayerList {

	public HijackedDedicatedPlayerList(DedicatedServer server) {
		super(server);
	}

	@Override
	public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
		playerIn.recipeBook = new DedRecipeBook(playerIn.world.getRecipeManager());
		super.initializeConnectionToPlayer(netManager, playerIn);
	}
}
