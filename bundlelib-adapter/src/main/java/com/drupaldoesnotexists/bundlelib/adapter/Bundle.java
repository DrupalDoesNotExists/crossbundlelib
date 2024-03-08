package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

public interface Bundle<PACKET> {

    @NotNull Iterable<PACKET> getBundledPackets();

    @NotNull PACKET asPacket();

}
