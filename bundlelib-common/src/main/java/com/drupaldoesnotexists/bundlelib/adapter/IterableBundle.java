package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

/**
 * Common base bundle type for old adapters
 * that simply stores the passed iterable.
 * @param <PACKET> Packet type.
 */
public abstract class IterableBundle<PACKET> implements Bundle<PACKET> {

    private final Iterable<PACKET> packets;

    /**
     * .ctor
     * @param packets Packets to bundle
     */
    public IterableBundle(Iterable<PACKET> packets) {
        this.packets = packets;
    }

    @Override
    public @NotNull Iterable<PACKET> getBundledPackets() {
        return packets;
    }

}
