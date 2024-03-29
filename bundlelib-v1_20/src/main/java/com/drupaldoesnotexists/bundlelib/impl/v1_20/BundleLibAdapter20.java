package com.drupaldoesnotexists.bundlelib.impl.v1_20;

import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter for 1.20.x
 */
public class BundleLibAdapter20 implements BundleLibAdapter<Packet<?>> {

    @Override
    public @NotNull BundleFactory<Packet<?>> getBundleFactory() {
        return new NativeBundleFactory();
    }

    @Override
    public @NotNull ChannelInjector getChannelInjector() {
        return new IncapableChannelInjector20();
    }

    @Override
    public @NotNull Channel getChannel(Player player) {
        return ((CraftPlayer) player).getHandle().connection.connection.channel;
    }

    @Override
    public @NotNull Object getProtocolFor(Packet<?> packet) {
        throw new UnsupportedOperationException("Something very bad happened somewhere! BundleLibAdapter20#getProtocolFor is not intended for calls!");
    }

    @Override
    public boolean isCompatible() {
        return Bukkit.getMinecraftVersion().contains("1.20");
    }

}
