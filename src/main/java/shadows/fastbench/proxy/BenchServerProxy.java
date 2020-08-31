package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import shadows.fastbench.book.DedRecipeBook;

public class BenchServerProxy implements IBenchProxy {

	@Override
	public void deleteBook(Entity e) {
		if (e instanceof ServerPlayerEntity) ((ServerPlayerEntity) e).recipeBook = new DedRecipeBook();
	}

}
