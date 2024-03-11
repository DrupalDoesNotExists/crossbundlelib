package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleWriter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

/**
 * Channel injector implementation that contains common
 * logic suitable for multiple adapters.
 * @param <PACKET> Packet type.
 * @param <BUNDLE> Bundle type.
 * @param <WRITER> Writer type.
 */
public class CommonChannelInjector<PACKET, BUNDLE extends Bundle<PACKET>,
        WRITER extends BundleWriter<PACKET, BUNDLE>> implements ChannelInjector {

    private static final String MINECRAFT_ENCODER_NAME = "encoder";
    private static final String PRE_ENCODER_NAME = "prepender";

    private final WRITER writer;

    /**
     * .ctor
     * @param bundleWriter Writer to delegate to
     */
    public CommonChannelInjector(@NotNull WRITER bundleWriter) {
        this.writer = bundleWriter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void inject(@NotNull Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        ChannelHandler channelHandler = pipeline.remove(MINECRAFT_ENCODER_NAME);
        MessageToByteEncoder<PACKET> encoder = (MessageToByteEncoder<PACKET>) channelHandler;

        if (encoder instanceof MinecraftEncoderProxy) {
            throw new IllegalStateException("Channel encoder proxy is already injected!");
        }
        MinecraftEncoderProxy<PACKET, BUNDLE, WRITER> proxy = new MinecraftEncoderProxy<>(encoder, writer);

        pipeline.addAfter(PRE_ENCODER_NAME, MINECRAFT_ENCODER_NAME, proxy);
    }

}
