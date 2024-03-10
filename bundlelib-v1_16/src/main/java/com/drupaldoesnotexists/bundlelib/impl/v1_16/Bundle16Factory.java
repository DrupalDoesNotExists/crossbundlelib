package com.drupaldoesnotexists.bundlelib.impl.v1_16;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketListenerPlayOut;
import org.jetbrains.annotations.NotNull;

public class Bundle16Factory implements BundleFactory<Packet<PacketListenerPlayOut>> {

    @Override
    public @NotNull Bundle<Packet<PacketListenerPlayOut>> createBundle(@NotNull Iterable<Packet<PacketListenerPlayOut>> packets) {
        return new Bundle16(packets);
    }

}
