package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.jetbrains.annotations.NotNull;

/**
 * .internal
 */
public class NativeBundleFactory implements BundleFactory<Packet<ClientGamePacketListener>> {

    @Override
    public @NotNull Bundle<Packet<ClientGamePacketListener>> createBundle(@NotNull Iterable<Packet<ClientGamePacketListener>> packets) {
        return new NativeBundle20(new ClientboundBundlePacket(packets));
    }

}
