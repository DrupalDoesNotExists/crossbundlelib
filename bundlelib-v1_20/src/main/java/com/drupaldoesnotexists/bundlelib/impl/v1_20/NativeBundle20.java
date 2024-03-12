package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * .internal
 */
public class NativeBundle20 implements Bundle<Packet<?>> {

    private final ClientboundBundlePacket bundlePacket;

    /**
     * .ctor
     * @param packet Bundle packet handle
     */
    public NativeBundle20(ClientboundBundlePacket packet) {
        this.bundlePacket = packet;
    }

    @Override
    public @NotNull Iterable<Packet<?>> getBundledPackets() {
        List<Packet<?>> packets = new ArrayList<>();
        bundlePacket.subPackets().forEach(packets::add);
        return packets;
    }

    @Override
    public @NotNull Packet<?> asPacket() {
        return bundlePacket;
    }

}
