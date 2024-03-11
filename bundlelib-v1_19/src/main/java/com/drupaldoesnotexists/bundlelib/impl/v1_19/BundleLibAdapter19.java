package com.drupaldoesnotexists.bundlelib.impl.v1_19;

import com.drupaldoesnotexists.bundlelib.impl.reobf.ReobfLibAdapter;
import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter for 1.19.x
 */
public class BundleLibAdapter19 extends ReobfLibAdapter {

    @Override
    public @NotNull Channel getChannel(Player player) {
        return ((CraftPlayer) player).getHandle().connection.connection.channel;
    }

}
