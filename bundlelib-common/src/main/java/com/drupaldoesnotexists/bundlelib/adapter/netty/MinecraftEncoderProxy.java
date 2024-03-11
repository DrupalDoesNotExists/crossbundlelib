package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleWriter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Custom channel handler used by the common
 * channel injector implementation.
 * <br />
 * Delegates all encodings to the underlying
 * passed bundle writer and Minecraft original
 * encoder.
 * @param <PACKET> Packet type.
 * @param <BUNDLE> Bundle type.
 * @param <WRITER> Writer type.
 */
public class MinecraftEncoderProxy<PACKET, BUNDLE extends Bundle<PACKET>,
        WRITER extends BundleWriter<PACKET, BUNDLE>> extends MessageToByteEncoder<PACKET> {

    private final MessageToByteEncoder<PACKET> minecraftEncoder;
    private final Method encoderMethod;
    private final WRITER writer;

    public boolean acceptOutboundMessage(Object msg) {
        // TODO: This fix is a quick solution and should be reworked to understand
        //       why the issue #1 really happens.
        return true;
    }

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
