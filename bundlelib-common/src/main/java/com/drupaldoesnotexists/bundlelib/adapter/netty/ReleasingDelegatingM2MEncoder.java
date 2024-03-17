package com.drupaldoesnotexists.bundlelib.adapter.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class ReleasingDelegatingM2MEncoder extends ListMessage2MessageEncoderDelegating2Message2BytesEncoder<ByteBuf> {

    public ReleasingDelegatingM2MEncoder(MessageToByteEncoder<ByteBuf> original) throws NoSuchMethodException {
        super(original);
    }

    @Override
    protected void beforeDelegate(ChannelHandlerContext ctx, ByteBuf msg, List<ByteBuf> all, List<ByteBuf> list) {}

    @Override
    protected void msgEncoded(ChannelHandlerContext ctx, ByteBuf msg, List<ByteBuf> all, List<ByteBuf> list) {
        // Release an old buffer
        msg.release();
    }

}
