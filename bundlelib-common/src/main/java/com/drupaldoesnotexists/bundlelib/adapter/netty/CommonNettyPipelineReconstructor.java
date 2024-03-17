package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Common channel injector that, in fact, almost entirely
 * reconstructs the default pipeline.
 * @param <PACKET> Packet type.
 */
public class CommonNettyPipelineReconstructor<PACKET> implements ChannelInjector {

    private final BundleLibAdapter<PACKET> adapter;

    public CommonNettyPipelineReconstructor(BundleLibAdapter<PACKET> adapter) {
        this.adapter = adapter;
    }

    private static class Constants {

        /*
        Pipeline handler names
         */

        static final String MINECRAFT_LENGTH_PREPENDER = "prepender";
        static final String MINECRAFT_ENCODER = "encoder";
        static final String MINECRAFT_COMPRESSOR = "compress";
        static final String BUNDLE_TO_COLLECTION_ENCODER = "bundle2collection";
        static final String COLLECTION_COMPRESSOR = "compress_collection";
        static final String BYTEBUF_COLLECTION_SPLITTER = "bytebuf_splitter";

        /*
        Attribute keys
         */

        static final AttributeKey<Boolean> ATTRIBUTE_INJECTED = AttributeKey.valueOf("bundlelib_injected");
        static final AttributeKey<Object> ATTRIBUTE_PROTOCOL = AttributeKey.valueOf("protocol");

    }

    /**
     * Splits bundles to the collection of packets.
     * <br />
     * DO NOT MOVE! Due to netty type param matching mechanism, this class should
     * be defined here, so it can inherit PACKET type parameter without defining
     * its own.
     */
    private class Bundle2CollectionEncoder extends MessageToMessageEncoder<PACKET> {

        @Override
        @SuppressWarnings("unchecked")
        protected void encode(ChannelHandlerContext ctx, PACKET packet, List<Object> list) {
            List<PACKET> packets = new ArrayList<>();
            if (packet instanceof Bundle) {
                Bundle<PACKET> bundle = (Bundle<PACKET>) packet;
                bundle.getBundledPackets().forEach(packets::add);
            } else {
                packets.add(packet);
            }
            list.add(packets);
        }

    }

    /**
     * Encodes packets within the collection as a collection of
     * individual byte buffers.
     * <br />
     * DO NOT MOVE! Due to netty type param matching mechanism, this class should
     * be defined here, so it can inherit PACKET type parameter without defining
     * its own.
     */
    private class PacketCollectionEncoder extends ListMessage2MessageEncoderDelegating2Message2BytesEncoder<PACKET> {

        public PacketCollectionEncoder(MessageToByteEncoder<PACKET> original) throws NoSuchMethodException {
            super(original);
        }

        @Override
        protected void beforeDelegate(ChannelHandlerContext ctx, PACKET msg, List<PACKET> all, List<ByteBuf> list) {
            if (all.size() > 1) {
                // Fix: custom packets such as our Bundle do not have any protocol set.
                Channel channel = ctx.channel();
                channel.attr(Constants.ATTRIBUTE_PROTOCOL).set(adapter.getProtocolFor(msg));
            }
        }

        @Override
        protected void msgEncoded(ChannelHandlerContext ctx, PACKET msg, List<PACKET> all, List<ByteBuf> list) {}

    }

    @SuppressWarnings("unchecked")
    private <T> T genericHelper(ChannelPipeline pipeline, String name) {
        return (T) pipeline.get(name);
    }

    @Override
    public void inject(@NotNull Channel channel) throws Exception {
        if (channel.attr(Constants.ATTRIBUTE_INJECTED).compareAndSet(false, true)) {
            throw new UnsupportedOperationException("Channel is already injected");
        }

        // Reconstruct the pipeline
        ChannelPipeline pipeline = channel.pipeline();
        MessageToByteEncoder<PACKET> encoder = genericHelper(pipeline, Constants.MINECRAFT_ENCODER);
        MessageToByteEncoder<ByteBuf> prepender = genericHelper(pipeline, Constants.MINECRAFT_LENGTH_PREPENDER);
        MessageToByteEncoder<ByteBuf> compress = genericHelper(pipeline, Constants.MINECRAFT_COMPRESSOR);
        pipeline.replace(Constants.MINECRAFT_ENCODER, Constants.MINECRAFT_ENCODER, new PacketCollectionEncoder(encoder));
        pipeline.replace(Constants.MINECRAFT_LENGTH_PREPENDER, Constants.MINECRAFT_LENGTH_PREPENDER, new ReleasingDelegatingM2MEncoder(prepender));

        // NOTE: Paper uses a custom compressor, and We can't remove it from the pipeline as then it closes
        //       the native resources and can't really work anymore. We leave it behind as it doesn't receive
        //       packets anyway because of the type parameter matching.
        pipeline.addAfter(Constants.MINECRAFT_COMPRESSOR, Constants.COLLECTION_COMPRESSOR, new ReleasingDelegatingM2MEncoder(compress));

        pipeline.addAfter(Constants.MINECRAFT_ENCODER, Constants.BUNDLE_TO_COLLECTION_ENCODER, new Bundle2CollectionEncoder());
        pipeline.addBefore(Constants.MINECRAFT_LENGTH_PREPENDER, Constants.BYTEBUF_COLLECTION_SPLITTER, new ByteBufCollectionSplitter());
    }

}
