package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class SequentialBundleWriter<PACKET, BUNDLE extends Bundle<PACKET>>
        implements BundleWriter<PACKET, BUNDLE> {

    @Override
    public void write(@NotNull BUNDLE bundle, @NotNull ByteBuf buf,
                      @NotNull BiConsumer<@NotNull PACKET, @NotNull ByteBuf> minecraftEncoder) {
        bundle.getBundledPackets().forEach(packet -> minecraftEncoder.accept(packet, buf));
    }

}
