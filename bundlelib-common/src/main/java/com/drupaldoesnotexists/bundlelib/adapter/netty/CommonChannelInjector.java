package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleWriter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    private static final String PRE_ENCODER_NAME = "compress";

    private final WRITER writer;

    /**
     * .ctor
     * @param bundleWriter Writer to delegate to
     */
    public CommonChannelInjector(@NotNull WRITER bundleWriter) {
        this.writer = bundleWriter;
    }

    /**
     * Custom channel handler used by the common
     * channel injector implementation.
     * <br />
     * Delegates all encodings to the underlying
     * passed bundle writer and Minecraft original
     * encoder.
     */
    protected class MinecraftEncoderProxy extends MessageToByteEncoder<PACKET> {

        private final MessageToByteEncoder<PACKET> minecraftEncoder;
        private final Method encoderMethod;
        private final WRITER writer;

        /**
         * .ctor
         * @param minecraftEncoder Minecraft default encoder handler.
         * @param writer           Bundle writer to delegate to.
         * @throws NoSuchMethodException When previous handler does not contain the proper "encode" method.
         */
        public MinecraftEncoderProxy(@NotNull MessageToByteEncoder<PACKET> minecraftEncoder,
                                     @NotNull WRITER writer) throws NoSuchMethodException {
            this.minecraftEncoder = minecraftEncoder;
            this.encoderMethod = minecraftEncoder.getClass()
                    .getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
            this.encoderMethod.setAccessible(true);
            this.writer = writer;
        }

        private void delegate(ChannelHandlerContext ctx, PACKET packet, ByteBuf buf) {
            try {
                this.encoderMethod.invoke(this.minecraftEncoder, ctx, packet, buf);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void encode(ChannelHandlerContext channelHandlerContext, PACKET packet, ByteBuf byteBuf) {
            if (packet instanceof Bundle) {
                BUNDLE bundle = (BUNDLE) packet;
                this.writer.write(bundle, byteBuf, (pack, buf) -> delegate(channelHandlerContext, pack, buf));
            } else {
                delegate(channelHandlerContext, packet, byteBuf);
            }
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public void inject(@NotNull Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        ChannelHandler channelHandler = pipeline.remove(MINECRAFT_ENCODER_NAME);
        MessageToByteEncoder<PACKET> encoder = (MessageToByteEncoder<PACKET>) channelHandler;

        if (encoder instanceof CommonChannelInjector<?,?,?>.MinecraftEncoderProxy) {
            throw new IllegalStateException("Channel encoder proxy is already injected!");
        }
        MinecraftEncoderProxy proxy = new MinecraftEncoderProxy(encoder, writer);

        pipeline.addAfter(PRE_ENCODER_NAME, MINECRAFT_ENCODER_NAME, proxy);
    }

}
