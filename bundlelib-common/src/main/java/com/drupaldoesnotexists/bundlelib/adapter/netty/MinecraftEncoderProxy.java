package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleWriter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MinecraftEncoderProxy<PACKET, BUNDLE extends Bundle<PACKET>,
        WRITER extends BundleWriter<PACKET, BUNDLE>> extends MessageToByteEncoder<PACKET> {

    private final MessageToByteEncoder<PACKET> minecraftEncoder;
    private final Method encoderMethod;
    private final WRITER writer;

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
    protected void encode(ChannelHandlerContext channelHandlerContext, PACKET packet, ByteBuf byteBuf) throws Exception {
        if (packet instanceof Bundle) {
            BUNDLE bundle = (BUNDLE) packet;
            this.writer.write(bundle, byteBuf, (pack, buf) -> delegate(channelHandlerContext, pack, buf));
        } else {
            delegate(channelHandlerContext, packet, byteBuf);
        }
    }

}
