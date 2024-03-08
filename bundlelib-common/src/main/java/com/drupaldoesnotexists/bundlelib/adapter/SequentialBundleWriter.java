package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Common bundle writer used by old adapters
 * that writes all the bundled packets sequentially
 * to the single byte buffer by delegating encoding
 * to the Minecraft original encoder multiple times.
 * @param <PACKET> Packet type.
 * @param <BUNDLE> Bundle type.
 */
public class SequentialBundleWriter<PACKET, BUNDLE extends Bundle<PACKET>>
        implements BundleWriter<PACKET, BUNDLE> {

    @Override
    public void write(@NotNull BUNDLE bundle, @NotNull ByteBuf buf,
                      @NotNull BiConsumer<@NotNull PACKET, @NotNull ByteBuf> minecraftEncoder) {
        bundle.getBundledPackets().forEach(packet -> minecraftEncoder.accept(packet, buf));
    }

}
