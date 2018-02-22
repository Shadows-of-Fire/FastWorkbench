package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import shadows.fastbench.book.DedRecipeBook;

public class BenchProxy {

	static DedRecipeBook book = new DedRecipeBook();

	public void deleteBook(Entity e) {
		if (e instanceof EntityPlayerMP) ((EntityPlayerMP) e).recipeBook = book;
	}

}
