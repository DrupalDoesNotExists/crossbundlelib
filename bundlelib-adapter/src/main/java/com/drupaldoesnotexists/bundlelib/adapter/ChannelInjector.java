package com.drupaldoesnotexists.bundlelib.adapter;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

public interface ChannelInjector {

    void inject(@NotNull Channel channel) throws Exception;

}
