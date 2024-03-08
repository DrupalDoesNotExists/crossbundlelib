package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

/**
 * Wrapper, which describes bundles in a
 * version-compliant manner.
 * @param <PACKET> Packet type.
 */
public interface Bundle<PACKET> {

    /**
     * @return All bundled packets.
     */
    @NotNull Iterable<PACKET> getBundledPackets();

    /**
     * @return Native platform packet representation of this bundle.
     */
    @NotNull PACKET asPacket();

}
