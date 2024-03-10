package com.drupaldoesnotexists.bundlelib.impl.v1_16;

import com.drupaldoesnotexists.bundlelib.adapter.IterableBundle;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketListenerPlayOut;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Bundle16 extends IterableBundle<Packet<PacketListenerPlayOut>> implements Packet<PacketListenerPlayOut> {

    public Bundle16(Iterable<Packet<PacketListenerPlayOut>> packets) {
        super(packets);
    }

    @Override
    public @NotNull Packet<PacketListenerPlayOut> asPacket() {
        return this;
    }

    @Override
    public void a(PacketDataSerializer packetDataSerializer) throws IOException {}

    @Override
    public void b(PacketDataSerializer packetDataSerializer) {}

    @Override
    public void a(PacketListenerPlayOut packetListenerPlayOut) {}

}
