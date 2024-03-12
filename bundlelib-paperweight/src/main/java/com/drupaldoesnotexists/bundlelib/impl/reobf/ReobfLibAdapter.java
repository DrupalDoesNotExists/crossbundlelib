package com.drupaldoesnotexists.bundlelib.impl.reobf;

import com.drupaldoesnotexists.bundlelib.adapter.BundleFactory;
import com.drupaldoesnotexists.bundlelib.adapter.BundleLibAdapter;
import com.drupaldoesnotexists.bundlelib.adapter.ChannelInjector;
import com.drupaldoesnotexists.bundlelib.adapter.SequentialBundleWriter;
import com.drupaldoesnotexists.bundlelib.adapter.netty.CommonChannelInjector;
import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
        return new CommonChannelInjector<>(new SequentialBundleWriter<>());
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
