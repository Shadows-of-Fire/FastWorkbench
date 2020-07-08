package shadows.fastbench.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import shadows.fastbench.book.DedRecipeBook;

public class HijackedDedicatedPlayerList extends DedicatedPlayerList {

	public HijackedDedicatedPlayerList(DedicatedServer server) {
		super(server, server.field_240767_f_, server.field_240766_e_);
	}

	@Override
	public void initializeConnectionToPlayer(NetworkManager netManager, ServerPlayerEntity playerIn) {
		playerIn.recipeBook = new DedRecipeBook();
		super.initializeConnectionToPlayer(netManager, playerIn);
	}
}
