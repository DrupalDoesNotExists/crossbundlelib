package com.drupaldoesnotexists.bundlelib;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CrossBundleLib {

    private final BundleLibAdapter<?> adapter;

    public CrossBundleLib(@NotNull BundleLibAdapter<?> adapter) {
        this.adapter = adapter;
    }

    public CrossBundleLib(@NotNull BundleLibAdapter<?> ...adapters) {
        for (BundleLibAdapter<?> adapter : adapters) {
            if (adapter.isCompatible()) {
                this.adapter = adapter;
                break;
            }
        }
        throw new IllegalStateException("None of the passed adapters were compatible with the current platform!");
    }

    public void inject(Player player) throws Exception {
        this.adapter.getChannelInjector()
                .inject(this.adapter.getChannel(player));
    }

    @SuppressWarnings("unchecked")
    public <P> @NotNull Bundle<P> createBundle(Iterable<P> packets) throws Exception {
        return ((BundleFactory<P>) this.adapter.getBundleFactory()).createBundle(packets);
    }

    @SafeVarargs
    public final <P> @NotNull Bundle<P> createBundle(P ...packets) throws Exception {
        return createBundle(Arrays.asList(packets));
    }

}
