package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.jetbrains.annotations.NotNull;

public class ReobfBundleFactory implements BundleFactory<Packet<ClientGamePacketListener>> {

    @Override
    public @NotNull Bundle<Packet<ClientGamePacketListener>> createBundle(@NotNull Iterable<Packet<ClientGamePacketListener>> packets) {
        return new ReobfBundle(packets);
    }

}
