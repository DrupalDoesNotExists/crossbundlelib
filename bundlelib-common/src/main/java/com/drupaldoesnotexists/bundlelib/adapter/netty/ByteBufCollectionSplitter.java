package com.drupaldoesnotexists.bundlelib.adapter.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Byte
 */
public class ByteBufCollectionSplitter extends MessageToMessageEncoder<List<ByteBuf>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, List<ByteBuf> byteBuffers, List<Object> list) {
        list.addAll(byteBuffers);
    }

}
