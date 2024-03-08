package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

/**
 * Factory that creates custom bundle objects.
 * @param <PACKET> Packet type.
 */
public interface BundleFactory<PACKET> {

    @NotNull Bundle<PACKET> createBundle(@NotNull Iterable<PACKET> packets) throws Exception;

}
