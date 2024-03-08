package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

public abstract class IterableBundle<PACKET> implements Bundle<PACKET> {

    private final Iterable<PACKET> packets;

    public IterableBundle(Iterable<PACKET> packets) {
        this.packets = packets;
    }

    @Override
    public @NotNull Iterable<PACKET> getBundledPackets() {
        return packets;
    }

}
