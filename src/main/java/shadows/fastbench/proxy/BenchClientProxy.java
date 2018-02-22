package shadows.fastbench.proxy;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import shadows.fastbench.book.DedClientBook;

public class BenchClientProxy extends BenchProxy {

	static DedClientBook book = new DedClientBook();

	@Override
	public void deleteBook(Entity e) {
		super.deleteBook(e);
		if (e instanceof EntityPlayerSP) ((EntityPlayerSP) e).recipeBook = book;
	}
}
