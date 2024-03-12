package com.drupaldoesnotexists.bundlelib.impl.v1_16;

import com.drupaldoesnotexists.bundlelib.adapter.IterableBundle;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketListenerPlayOut;
import org.jetbrains.annotations.NotNull;

/**
 * .internal
 */
public class Bundle16 extends IterableBundle<Packet<?>> implements Packet<PacketListenerPlayOut> {

    /**
     * .ctor
     * @param packets .ignored
     */
    public Bundle16(Iterable<Packet<?>> packets) {
        super(packets);
    }

    @Override
    public @NotNull Packet<?> asPacket() {
        return this;
    }

    @Override
    public void a(PacketDataSerializer packetDataSerializer) {}

    @Override
    public void b(PacketDataSerializer packetDataSerializer) {}

    @Override
    public void a(PacketListenerPlayOut packetListenerPlayOut) {}

}
