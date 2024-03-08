package com.drupaldoesnotexists.bundlelib.adapter;

import org.jetbrains.annotations.NotNull;

public interface BundleFactory<PACKET> {

    @NotNull Bundle<PACKET> createBundle(@NotNull Iterable<PACKET> packets) throws Exception;

}
