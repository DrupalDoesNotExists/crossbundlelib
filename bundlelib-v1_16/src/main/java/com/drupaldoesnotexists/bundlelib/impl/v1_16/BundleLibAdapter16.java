package com.drupaldoesnotexists.bundlelib.impl.v1_16;

import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import com.drupaldoesnotexists.bundlelib.adapter.SequentialBundleWriter;
import com.drupaldoesnotexists.bundlelib.adapter.netty.CommonChannelInjector;
import io.netty.channel.Channel;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketListenerPlayOut;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter for 1.16.x
 */
public class BundleLibAdapter16 implements BundleLibAdapter<Packet<PacketListenerPlayOut>> {

    @Override
    public @NotNull BundleFactory<Packet<PacketListenerPlayOut>> getBundleFactory() {
        return new Bundle16Factory();
    }

    @Override
    public @NotNull ChannelInjector getChannelInjector() {
        return new CommonChannelInjector<>(new SequentialBundleWriter<>());
    }

    @Override
    public @NotNull Channel getChannel(Player player) {
        return ((CraftPlayer) player).getHandle().networkManager.channel;
    }

    @Override
    public boolean isCompatible() {
        return Bukkit.getMinecraftVersion().contains("1.16");
    }

}
