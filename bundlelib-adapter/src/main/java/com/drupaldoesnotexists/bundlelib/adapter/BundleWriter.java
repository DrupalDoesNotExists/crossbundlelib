package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface BundleWriter<PACKET, BUNDLE extends Bundle<PACKET>> {

    void write(@NotNull BUNDLE bundle, @NotNull ByteBuf buf, @NotNull BiConsumer<@NotNull PACKET, @NotNull ByteBuf> minecraftEncoder);

}
