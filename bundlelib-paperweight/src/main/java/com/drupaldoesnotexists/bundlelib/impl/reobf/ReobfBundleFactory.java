package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
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
public class ReobfBundleFactory implements BundleFactory<Packet<ClientGamePacketListener>> {

    @Override
    public @NotNull Bundle<Packet<ClientGamePacketListener>> createBundle(@NotNull Iterable<Packet<ClientGamePacketListener>> packets) {
        return new ReobfBundle(packets);
    }

}
