package shadows.fastbench.net;

import net.minecraft.entity.player.EntityPlayerMP;
import shadows.fastbench.FastBench;

public class PlayerListHook {

    public static void initializeConnectionToPlayerHook(EntityPlayerMP player) {
        if (FastBench.removeRecipeBook) {
            player.recipeBook = FastBench.SERVER_BOOK;
        }
    }
}
