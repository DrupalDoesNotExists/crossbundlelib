package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.IterableBundle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.jetbrains.annotations.NotNull;

/**
 * A common partial implementation for all paperweight-based adapters.
 * Because of the remapping, it fixes a lot of boilerplate issues as all
 * classes and packages are named the same.
 * <br />
 * INTERNAL USE ONLY!
 * CAN'T BE USED AS A STANDALONE ADAPTER!
 */
public class ReobfBundle extends IterableBundle<Packet<ClientGamePacketListener>>
        implements Packet<ClientGamePacketListener> {

    /**
     * .ctor
     * @param packets Packets to bundle.
     */
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
