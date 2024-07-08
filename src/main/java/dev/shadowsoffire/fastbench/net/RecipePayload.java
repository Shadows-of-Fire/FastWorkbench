package dev.shadowsoffire.fastbench.net;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.shadowsoffire.fastbench.FastBench;
import dev.shadowsoffire.fastbench.api.ICraftingContainer;
import dev.shadowsoffire.fastbench.api.ICraftingScreen;
import dev.shadowsoffire.placebo.network.PayloadProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RecipePayload(ResourceLocation recipeId, ItemStack output) implements CustomPacketPayload {

    public static final Type<RecipePayload> TYPE = new Type<>(FastBench.loc("recipe"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipePayload> CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, RecipePayload::recipeId,
        ItemStack.OPTIONAL_STREAM_CODEC, RecipePayload::output,
        RecipePayload::new);

    public static final ResourceLocation NULL = ResourceLocation.fromNamespaceAndPath("null", "null");

    public RecipePayload(@Nullable RecipeHolder<CraftingRecipe> recipe, ItemStack output) {
        this(recipe == null ? NULL : recipe.id(), output);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Provider implements PayloadProvider<RecipePayload> {

        @Override
        public Type<RecipePayload> getType() {
            return TYPE;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, RecipePayload> getCodec() {
            return CODEC;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handle(RecipePayload msg, IPayloadContext ctx) {
            RecipeHolder<CraftingRecipe> recipe = (RecipeHolder<CraftingRecipe>) Minecraft.getInstance().level.getRecipeManager().byKey(msg.recipeId).orElse(null);
            if (Minecraft.getInstance().screen instanceof ICraftingScreen) {
                ICraftingContainer c = ((ICraftingScreen) Minecraft.getInstance().screen).getContainer();
                updateLastRecipe(c.getResult(), recipe, msg.output);
            }
            else if (Minecraft.getInstance().screen instanceof InventoryScreen) {
                InventoryMenu c = ((InventoryScreen) Minecraft.getInstance().screen).getMenu();
                updateLastRecipe(c.resultSlots, recipe, msg.output);
            }
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

        @Override
        public String getVersion() {
            return "1";
        }
    }
}
