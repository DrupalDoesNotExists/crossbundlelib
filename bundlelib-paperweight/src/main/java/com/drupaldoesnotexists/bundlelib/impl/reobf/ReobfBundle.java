package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.IterableBundle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.jetbrains.annotations.NotNull;

public class ReobfBundle extends IterableBundle<Packet<ClientGamePacketListener>>
        implements Packet<ClientGamePacketListener> {

    public ReobfBundle(Iterable<Packet<ClientGamePacketListener>> packets) {
        super(packets);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> asPacket() {
        return this;
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {}

    @Override
    public void handle(@NotNull ClientGamePacketListener clientGamePacketListener) {}

}
