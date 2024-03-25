package com.drupaldoesnotexists.bundlelib.adapter.netty;

import com.drupaldoesnotexists.bundlelib.adapter.Bundle;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelAlreadyInjectedException;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import com.drupaldoesnotexists.bundlelib.adapter.netty.constable.PacketConstable;
import com.drupaldoesnotexists.bundlelib.adapter.netty.constable.PacketWindow;
import com.drupaldoesnotexists.bundlelib.adapter.netty.constable.PacketWindowKind;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        static final String PROTOCOL_FIXER = "protocol-attr-fixer";
        static final String BUNDLE_DISSECTOR = "bundle-dissector";
        static final String BUNDLE_COLLECTOR = "bundle-collector";

        /*
        Attribute keys
         */

        static final AttributeKey<Class<?>> ATTRIBUTE_INJECTED = AttributeKey.valueOf("bundlelib_injected");
        static final AttributeKey<PacketConstable> ATTRIBUTE_CONSTABLE = AttributeKey.valueOf("bundlelib_constable");
        static final AttributeKey<Object> ATTRIBUTE_PROTOCOL = AttributeKey.valueOf("protocol");

    }

    class ProtocolFixer extends MessageToMessageEncoder<PACKET> {

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, PACKET packet, List<Object> list) {
            Attribute<Object> protocol = channelHandlerContext.channel().attr(Constants.ATTRIBUTE_PROTOCOL);
            if (protocol.get() == null) {
                // Only resolve protocol if needed
                protocol.set(adapter.getProtocolFor(packet));
            }
            list.add(packet);
        }

    }

    class BundleDissector extends MessageToMessageEncoder<PACKET> {

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, PACKET packet, List<Object> list) {
            PacketConstable constable = channelHandlerContext.channel().attr(Constants.ATTRIBUTE_CONSTABLE).get();
            PacketWindow window;
            if (packet instanceof Bundle<?> bundle) {
                bundle.getBundledPackets().forEach(list::add);
                window = new PacketWindow(PacketWindowKind.BUNDLE, list.size());
            } else {
                list.add(packet);
                window = new PacketWindow(PacketWindowKind.SINGLE, 1);
            }
            constable.schedule(window);
        }

    }

    // TODO: Move to other file
    static class BundleCollector extends ChannelOutboundHandlerAdapter {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            PacketConstable constable = ctx.channel().attr(Constants.ATTRIBUTE_CONSTABLE).get();
            PacketWindow window = constable.getScheduledWindow();
            if (window == null) {
                promise.setFailure(new IllegalStateException("BUNDLELIB. Got unexpected packet (no window scheduled)"));
                return;
            }

            boolean bundle = window.kind() == PacketWindowKind.BUNDLE;
            ctx.write(msg, promise);
            if (!bundle) {
                // Only flush immediately if it is a single packet
                ctx.flush();
            }

            if (counter.incrementAndGet() == window.size().get()) {
                constable.closeWindow();
                counter.set(0);
                if (bundle) {
                    // Flush on the window end if that was a bundle end
                    ctx.flush();
                }
            }
        }

    }

    @Override
    public void inject(@NotNull Channel channel) {
        if (ensureNotInjected(channel)) return;

        channel.attr(Constants.ATTRIBUTE_CONSTABLE).set(new PacketConstable());

        // Reconstruct the pipeline
        ChannelPipeline pipeline = channel.pipeline();
        // Handler -> Dissector -> Encoder -> ...
        pipeline.addBefore(Constants.MINECRAFT_ENCODER, Constants.BUNDLE_DISSECTOR, new BundleDissector());
        // Handler -> Dissector -> Fixer -> Encoder -> ...
        pipeline.addBefore(Constants.MINECRAFT_ENCODER, Constants.PROTOCOL_FIXER, new ProtocolFixer());
        // ... -> Length Field Prepender -> Collector
        pipeline.addAfter(Constants.MINECRAFT_LENGTH_PREPENDER, Constants.BUNDLE_COLLECTOR, new BundleCollector());
    }

    private boolean ensureNotInjected(Channel channel) {
        Attribute<Class<?>> attribute = channel.attr(Constants.ATTRIBUTE_INJECTED);
        Class<?> attrVal = attribute.get();
        if (attrVal != null && attrVal != this.adapter.getClass()) {
            throw new ChannelAlreadyInjectedException(
                    "Channel " + channel.id()
                            + " is already injected by "
                            + attribute.get().getName());
        }
        attribute.set(this.adapter.getClass());
        return attrVal == this.adapter.getClass();  // true means should stop quietly
    }

}
