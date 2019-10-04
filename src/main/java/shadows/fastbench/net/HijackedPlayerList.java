package shadows.fastbench.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServer;
import shadows.fastbench.book.DedRecipeBook;

public class HijackedPlayerList extends IntegratedPlayerList {

	public HijackedPlayerList(IntegratedServer server) {
		super(server);
	}

	@Override
	public void initializeConnectionToPlayer(NetworkManager netManager, ServerPlayerEntity playerIn) {
		playerIn.recipeBook = new DedRecipeBook();
		super.initializeConnectionToPlayer(netManager, playerIn);
	}
}
