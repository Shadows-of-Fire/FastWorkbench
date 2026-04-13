package dev.shadowsoffire.fastbench.net;

import java.util.List;
import java.util.Optional;

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
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RecipePayload(ItemStack output) implements CustomPacketPayload {

    public static final Type<RecipePayload> TYPE = new Type<>(FastBench.loc("recipe"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipePayload> CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC, RecipePayload::output,
        RecipePayload::new);

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
        public void handleClient(RecipePayload msg, IPayloadContext ctx) {
            if (Minecraft.getInstance().screen instanceof ICraftingScreen screen) {
                ICraftingContainer c = screen.getContainer();
                updateLastRecipe(c.getResult(), msg.output);
            }
            else if (Minecraft.getInstance().screen instanceof InventoryScreen inv) {
                InventoryMenu c = inv.getMenu();
                updateLastRecipe(c.resultSlots, msg.output);
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

        public static void updateLastRecipe(ResultContainer craftResult, ItemStack output) {
            craftResult.setItem(0, output);
        }

        @Override
        public String getVersion() {
            return "2";
        }
    }
}
