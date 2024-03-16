package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * General adapter for the core library.
 * @param <PACKET> Packet type.
 */
public interface BundleLibAdapter<PACKET> {

    /**
     * @return Adapted bundle factory
     */
    @NotNull BundleFactory<PACKET> getBundleFactory();

    /**
     * @return Adapted channel injector
     */
    @NotNull ChannelInjector getChannelInjector();

    /**
     * @param player Player.
     * @return Player connection Netty channel.
     */
    @NotNull Channel getChannel(Player player);

    /**
     * @param packet Packet.
     * @return Protocol object.
     */
    @NotNull Object getProtocolFor(PACKET packet);

    /**
     * @return Whether this adapter is compatible with the current platform or not.
     */
    boolean isCompatible();

}
