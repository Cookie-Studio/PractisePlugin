package me.liuli.pra.core.cache;

import cn.nukkit.Player;
import me.liuli.pra.core.Kit;

public class DuelInvite {
    public Player invite;
    public Player accept;
    public Kit kit;

    public DuelInvite(Player invite, Player accept, Kit kit) {
        this.invite = invite;
        this.accept = accept;
        this.kit = kit;
    }
}
