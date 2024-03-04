package dev.shadowsoffire.fastbench.net;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.shadowsoffire.fastbench.FastBench;
import dev.shadowsoffire.fastbench.api.ICraftingContainer;
import dev.shadowsoffire.fastbench.api.ICraftingScreen;
import dev.shadowsoffire.placebo.network.PayloadHelper;
import dev.shadowsoffire.placebo.network.PayloadProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record RecipeMessage(ResourceLocation recipeId, ItemStack output) implements CustomPacketPayload {

    public static final ResourceLocation ID = FastBench.loc("recipe");
    public static final ResourceLocation NULL = new ResourceLocation("null", "null");

    public RecipeMessage(@Nullable RecipeHolder<CraftingRecipe> recipe, ItemStack output) {
        this(recipe == null ? NULL : recipe.id(), output);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.recipeId());
        if (!NULL.equals(this.recipeId())) buf.writeItem(this.output());
    }

    public static class Provider implements PayloadProvider<RecipeMessage, PlayPayloadContext> {

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public RecipeMessage read(FriendlyByteBuf buf) {
            ResourceLocation rec = buf.readResourceLocation();
            return new RecipeMessage(rec, rec.equals(NULL) ? ItemStack.EMPTY : buf.readItem());
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handle(RecipeMessage msg, PlayPayloadContext ctx) {
            PayloadHelper.handle(() -> {
                RecipeHolder<CraftingRecipe> recipe = (RecipeHolder<CraftingRecipe>) Minecraft.getInstance().level.getRecipeManager().byKey(msg.recipeId).orElse(null);
                if (Minecraft.getInstance().screen instanceof ICraftingScreen) {
                    ICraftingContainer c = ((ICraftingScreen) Minecraft.getInstance().screen).getContainer();
                    updateLastRecipe(c.getResult(), recipe, msg.output);
                }
                else if (Minecraft.getInstance().screen instanceof InventoryScreen) {
                    InventoryMenu c = ((InventoryScreen) Minecraft.getInstance().screen).getMenu();
                    updateLastRecipe(c.resultSlots, recipe, msg.output);
                }
            }, ctx);
        }

        @Override
        public List<ConnectionProtocol> getSupportedProtocols() {
            return List.of(ConnectionProtocol.PLAY);
        }

        @Override
        public Optional<PacketFlow> getFlow() {
            return Optional.of(PacketFlow.CLIENTBOUND);
        }

        @Override
        public boolean isOptional() {
            return false;
        }

        public static void updateLastRecipe(ResultContainer craftResult, RecipeHolder<CraftingRecipe> recipe, ItemStack output) {
            craftResult.setRecipeUsed(recipe);
            craftResult.setItem(0, output);
        }
    }
}
