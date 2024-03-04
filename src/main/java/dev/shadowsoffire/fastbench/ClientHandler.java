package dev.shadowsoffire.fastbench;

import static dev.shadowsoffire.fastbench.FastBench.disableToolTip;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = FastBench.MODID, value = Dist.CLIENT)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClientHandler {

    private static final int tooltipIdx = ThreadLocalRandom.current().nextInt(7);

    @SubscribeEvent
    public static void removeButton(ScreenEvent.Init.Post e) {
        if (!FastBench.removeBookButton) return;
        for (Renderable b : e.getScreen().renderables)
            if (b instanceof ImageButton ib && isBookButton(ib)) ib.visible = false;
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent e) {
        if (!disableToolTip) {
            if (e.getItemStack().getItem() == Items.CRAFTING_TABLE) {
                e.getToolTip().add(Component.translatable("info.fb.very_fast" + tooltipIdx).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    private static boolean isBookButton(ImageButton b) {
        return RecipeBookComponent.RECIPE_BUTTON_SPRITES == b.sprites;
    }

}
