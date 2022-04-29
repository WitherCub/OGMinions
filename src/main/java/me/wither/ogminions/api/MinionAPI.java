package me.wither.ogminions.api;

import me.wither.ogminions.*;
import me.wither.ogminions.colls.MPlayerColl;
import me.wither.ogminions.colls.MinionColl;
import me.wither.ogminions.entities.MPlayer;
import me.wither.ogminions.entities.Minion;
import me.wither.ogminions.events.MinionEngine;
import me.wither.ogminions.extra.ExtraKt;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MinionAPI {
    private MinionAPI() {}

    public static MinionEntity getMinion(Location location) {
        return ExtraKt.getMinion(location);
    }

    public static MPlayer getAsMPLayer(OfflinePlayer player) {
        return MPlayerColl.get().get(player);
    }

    public static MPlayer getAsMPLayer(Player player) {
        return MPlayerColl.get().get(player);
    }

    public static List<MinionEntity> getAllOnlineMinions() {
        return ExtraKt.getAllOnlineMinions();
    }

    @NotNull
    public static List<MinionEntity> getAllMinions() {
        return ExtraKt.getAllMinions();
    }

    public static Minion getMinionConf(String type) {
        return MinionEngine.get().getConf(type);
    }

    public static void registerMinion(String id, Minion config, MinionHandler minionHandler) {
        Minion minionObj = MinionColl.get().getOrCreate(id, config);
        MinionEngine.get().register(minionObj, minionHandler);
    }
}
