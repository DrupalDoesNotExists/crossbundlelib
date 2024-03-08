package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BundleLibAdapter<PACKET> {

    @NotNull BundleFactory<PACKET> getBundleFactory();

    @NotNull ChannelInjector getChannelInjector();

    @NotNull Channel getChannel(Player player);

    boolean isCompatible();

}
