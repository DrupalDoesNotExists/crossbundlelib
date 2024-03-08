package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Writer for the bundles that contains
 * custom bundle encoding logic.
 * <br />
 * E.g., for "nagling" We can't really only
 * use the packet class with custom "write"
 * method as default Minecraft encoder automatically
 * writes the ID for every passed packet.
 * @param <PACKET> Packet type.
 * @param <BUNDLE> Bundle type.
 */
public interface BundleWriter<PACKET, BUNDLE extends Bundle<PACKET>> {

    /**
     * Write the custom bundle.
     * @param bundle           Bundle.
     * @param buf              Buffer to fill.
     * @param minecraftEncoder Minecraft default encoder reference.
     */
    void write(@NotNull BUNDLE bundle, @NotNull ByteBuf buf, @NotNull BiConsumer<@NotNull PACKET, @NotNull ByteBuf> minecraftEncoder);

}
