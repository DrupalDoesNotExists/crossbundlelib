package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.jetbrains.annotations.NotNull;

/**
 * .internal
 */
public class NativeBundle20 implements Bundle<Packet<ClientGamePacketListener>> {

    private final BundlePacket<ClientGamePacketListener> bundlePacket;

    /**
     * .ctor
     * @param packet Bundle packet handle
     */
    public NativeBundle20(BundlePacket<ClientGamePacketListener> packet) {
        this.bundlePacket = packet;
    }

    @Override
    public @NotNull Iterable<Packet<ClientGamePacketListener>> getBundledPackets() {
        return bundlePacket.subPackets();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> asPacket() {
        return bundlePacket;
    }

}
