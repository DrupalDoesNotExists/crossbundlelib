package com.drupaldoesnotexists.bundlelib.adapter.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ListMessage2MessageEncoderDelegating2Message2BytesEncoder<I> extends MessageToMessageEncoder<List<I>> {

    private final MessageToByteEncoder<I> original;
    private final Method encode;

    public ListMessage2MessageEncoderDelegating2Message2BytesEncoder(MessageToByteEncoder<I> original) throws NoSuchMethodException {
        this.original = original;
        this.encode = original.getClass().getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
        this.encode.setAccessible(true);
    }

    private void delegate(ChannelHandlerContext ctx, I msg, ByteBuf buf) {
        try {
            encode.invoke(original, ctx, msg, buf);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, List<I> messages, List<Object> list) throws Exception {
        List<ByteBuf> output = new ArrayList<>();
        for (I msg : messages) {
            ByteBuf buf = channelHandlerContext.alloc().ioBuffer();
            beforeDelegate(channelHandlerContext, msg, messages, output);
            delegate(channelHandlerContext, msg, buf);
            output.add(buf);
            msgEncoded(channelHandlerContext, msg, messages, output);
        }
        list.add(output);
    }

    /**
     * Handle the message before delegation.
     * @param ctx  Context
     * @param msg  Message
     * @param all  All messages passed
     * @param list Output list
     */
    protected abstract void beforeDelegate(ChannelHandlerContext ctx, I msg, List<I> all, List<ByteBuf> list);

    /**
     * Handle the message after delegation.
     * @param ctx  Context
     * @param msg  Message
     * @param all  All messages passed
     * @param list Output list
     */
    protected abstract void msgEncoded(ChannelHandlerContext ctx, I msg, List<I> all, List<ByteBuf> list);

}
