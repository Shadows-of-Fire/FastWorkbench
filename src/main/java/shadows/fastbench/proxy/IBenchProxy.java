package shadows.fastbench.proxy;

import net.minecraft.entity.Entity;

public interface IBenchProxy {

	public void deleteBook(Entity e);

	public default void registerButtonRemover() {
	};

}
