package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

/**
 * Factory that creates custom bundle objects.
 * @param <PACKET> Packet type.
 */
public interface BundleFactory<PACKET> {

    /**
     * Create a bundle containing all the given packets.
     * @param packets Packets to include in the bundle.
     * @return Bundle
     * @throws Exception When given packets can't be bundled.
     */
    @NotNull Bundle<PACKET> createBundle(@NotNull Iterable<PACKET> packets) throws Exception;

}
