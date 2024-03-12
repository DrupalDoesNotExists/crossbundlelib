package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * .internal
 */
public class NativeBundleFactory implements BundleFactory<Packet<?>> {

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Bundle<Packet<?>> createBundle(@NotNull Iterable<Packet<?>> packets) {
        Iterator<Packet<?>> packetIterator = packets.iterator();
        if (!packetIterator.hasNext()) {
            throw new IllegalArgumentException("Passed packets iterable is empty");
        }
        List<Packet<ClientGamePacketListener>> casted = new ArrayList<>();
        for (Packet<?> packet : packets) {
            casted.add((Packet<ClientGamePacketListener>) packet);
        }

        return new NativeBundle20(new ClientboundBundlePacket(casted));
    }

}
