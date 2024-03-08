package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

/**
 * Injects the custom channel handlers to the given
 * Netty channel.
 */
public interface ChannelInjector {

    void inject(@NotNull Channel channel) throws Exception;

}
