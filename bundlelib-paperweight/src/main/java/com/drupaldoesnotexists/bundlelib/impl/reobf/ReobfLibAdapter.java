package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import com.drupaldoesnotexists.bundlelib.adapter.netty.CommonNettyPipelineReconstructor;
import io.netty.channel.Channel;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A common partial implementation for all paperweight-based adapters.
 * Because of the remapping, it fixes a lot of boilerplate issues as all
 * classes and packages are named the same.
 * <br />
 * INTERNAL USE ONLY!
 * CAN'T BE USED AS A STANDALONE ADAPTER!
 */
public abstract class ReobfLibAdapter implements BundleLibAdapter<Packet<?>> {

    @Override
    public @NotNull BundleFactory<Packet<?>> getBundleFactory() {
        return new ReobfBundleFactory();
    }

    @Override
    public @NotNull ChannelInjector getChannelInjector() {
        return new CommonNettyPipelineReconstructor<>(this);
    }

    @Override
    public @NotNull Object getProtocolFor(Packet<?> packet) {
        return ConnectionProtocol.getProtocolForPacket(packet);
    }

    @Override
    public boolean isCompatible() {
        return parseVersion() >= 17;
    }

    private int parseVersion() {
        String version = Bukkit.getMinecraftVersion();
        Integer[] components = Arrays.stream(version.split("\\."))
                .map(Integer::valueOf).toArray(Integer[]::new);
        return components[1];
    }

    public abstract @NotNull Channel getChannel(Player player);
}
