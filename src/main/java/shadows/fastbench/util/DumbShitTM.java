package shadows.fastbench.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class DumbShitTM {

	private static MethodHandle moveItemStackTo;

	static {
		Method _moveItemStackTo = ObfuscationReflectionHelper.findMethod(AbstractContainerMenu.class, "m_38903_", ItemStack.class, int.class, int.class, boolean.class);
		_moveItemStackTo.setAccessible(true);
		try {
			moveItemStackTo = MethodHandles.lookup().unreflect(_moveItemStackTo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean mergeItemStack(AbstractContainerMenu container, ItemStack stack, int start, int end) {
		try {
			return (boolean) moveItemStackTo.invoke(container, stack, start, end, true);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
