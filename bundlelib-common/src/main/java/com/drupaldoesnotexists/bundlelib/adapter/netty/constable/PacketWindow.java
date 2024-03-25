package com.drupaldoesnotexists.bundlelib.adapter.netty.constable;

import java.util.concurrent.atomic.AtomicInteger;

public record PacketWindow(PacketWindowKind kind, AtomicInteger size) {

    public PacketWindow(PacketWindowKind kind, int size) {
        this(kind, new AtomicInteger(size));
    }

}
