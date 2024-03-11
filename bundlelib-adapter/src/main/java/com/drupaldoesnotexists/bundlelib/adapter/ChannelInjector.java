package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

/**
 * Injects the custom channel handlers to the given
 * Netty channel.
 */
public interface ChannelInjector {

    /**
     * Inject custom handlers to the Netty channel.
     * @param channel Channel.
     * @throws Exception When something goes wrong.
     */
    void inject(@NotNull Channel channel) throws Exception;

}
