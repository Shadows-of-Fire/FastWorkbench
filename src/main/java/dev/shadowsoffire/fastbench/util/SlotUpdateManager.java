package dev.shadowsoffire.fastbench.util;

import java.util.IdentityHashMap;
import java.util.Map;

import dev.shadowsoffire.fastbench.FastBench;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.event.TickEvent.Phase;
import net.neoforged.neoforge.event.TickEvent.ServerTickEvent;

@EventBusSubscriber(modid = FastBench.MODID)
public class SlotUpdateManager {

	/**
	 * Map of scheduled updates. Updates are only executed on the server, as the client need not process recipe matches.
	 */
	private static final Map<CraftingInventoryExt, Runnable> UPDATES = new IdentityHashMap<>();

	/**
	 * Queues an update for the grid that will execute after the next 5-tick window.
	 * Multiple queue-ups will not stack, and only the latest one will be processed.
	 */
	public static void queueSlotUpdate(Level level, Player player, CraftingInventoryExt inv, ResultContainer result) {
		if (level.isClientSide) return;
		Runnable task = () -> FastBenchUtil.slotChangedCraftingGrid(level, player, inv, result);
		UPDATES.putIfAbsent(inv, task);
	}

	private static long serverTicks = 0;

	@SubscribeEvent
	public static void serverTick(ServerTickEvent e) {
		if (e.phase == Phase.END) {
			if (++serverTicks % FastBench.gridUpdateInterval == 0) {
				UPDATES.values().forEach(Runnable::run);
				UPDATES.clear();
			}
		}
	}
}
