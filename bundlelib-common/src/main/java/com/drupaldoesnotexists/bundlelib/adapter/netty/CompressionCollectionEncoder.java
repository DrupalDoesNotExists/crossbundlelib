package com.drupaldoesnotexists.bundlelib.adapter.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Compresses all packets in the collection.
 */
public class CompressionCollectionEncoder extends MessageToMessageEncoder<List<ByteBuf>> {

    private final MessageToByteEncoder<ByteBuf> original;
    private final Method compress;

    public CompressionCollectionEncoder(MessageToByteEncoder<ByteBuf> original) throws NoSuchMethodException {
        this.original = original;
        this.compress = original.getClass().getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
        this.compress.setAccessible(true);
    }

    private void delegate(ChannelHandlerContext ctx, ByteBuf old, ByteBuf newBuf) {
        try {
            compress.invoke(original, ctx, old, newBuf);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, List<ByteBuf> buffers, List<Object> list) {
        List<ByteBuf> bufList = new ArrayList<>();
        for (ByteBuf buf : buffers) {
            ByteBuf newBuf = ctx.alloc().ioBuffer();
            delegate(ctx, buf, newBuf);
            bufList.add(newBuf);
            buf.release();
        }
        list.add(bufList);
    }

}
