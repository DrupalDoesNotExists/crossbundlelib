package com.drupaldoesnotexists.bundlelib.impl.v1_16;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import net.minecraft.server.v1_16_R3.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * .internal
 */
public class Bundle16Factory implements BundleFactory<Packet<?>> {

    @Override
    public @NotNull Bundle<Packet<?>> createBundle(@NotNull Iterable<Packet<?>> packets) {
        return new Bundle16(packets);
    }

}
