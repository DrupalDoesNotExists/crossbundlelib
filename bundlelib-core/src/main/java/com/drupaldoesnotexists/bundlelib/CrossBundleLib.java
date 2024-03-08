package com.drupaldoesnotexists.bundlelib;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Core facade for the CrossBundleLib.
 */
public class CrossBundleLib {

    private final BundleLibAdapter<?> adapter;

    /**
     * Initialize a library with the given adapter.
     * @param adapter Adapter.
     */
    public CrossBundleLib(@NotNull BundleLibAdapter<?> adapter) {
        this.adapter = adapter;
    }

    /**
     * Initialize a library with first compatible
     * adapter among all given.
     * @param adapters Adapters to check.
     */
    public CrossBundleLib(@NotNull BundleLibAdapter<?> ...adapters) {
        for (BundleLibAdapter<?> adapter : adapters) {
            if (adapter.isCompatible()) {
                this.adapter = adapter;
                break;
            }
        }
        throw new IllegalStateException("None of the passed adapters were compatible with the current platform!");
    }

    /**
     * Inject a custom netty handlers to the Player's connection.
     * @param player Player
     * @throws Exception If something bad happens during the injection.
     */
    public void inject(Player player) throws Exception {
        this.adapter.getChannelInjector()
                .inject(this.adapter.getChannel(player));
    }

    /**
     * Create the bundle containing all the given packets.
     * @param packets Packets
     * @return Bundle with all given packets bundled
     * @param <P> Packet type
     * @throws Exception When factory refuses to create such a bundle.
     */
    @SuppressWarnings("unchecked")
    public <P> @NotNull Bundle<P> createBundle(Iterable<P> packets) throws Exception {
        return ((BundleFactory<P>) this.adapter.getBundleFactory()).createBundle(packets);
    }

    /**
     * Create the bundle containing all the given packets.
     * @param packets Packets
     * @return Bundle with all given packets bundled
     * @param <P> Packet type
     * @throws Exception When factory refuses to create such a bundle.
     */
    @SafeVarargs
    public final <P> @NotNull Bundle<P> createBundle(P ...packets) throws Exception {
        return createBundle(Arrays.asList(packets));
    }

}
