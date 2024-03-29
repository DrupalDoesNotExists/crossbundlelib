package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

/**
 * .internal
 */
public class IncapableChannelInjector20 implements ChannelInjector {

    @Override
    public void inject(@NotNull Channel channel) {
        // This netty injector really does nothing useful, but everything
        // to get every developer in hysterics!
    }

}
