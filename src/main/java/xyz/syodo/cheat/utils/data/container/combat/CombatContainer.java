package xyz.syodo.cheat.utils.data.container.combat;

import lombok.Getter;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.container.combat.elements.CPSData;
import xyz.syodo.cheat.utils.data.container.combat.elements.HitCooldownData;

@Getter
public class CombatContainer extends Container {

    private final CPSData cpsData = new CPSData(this);
    private final HitCooldownData hitCooldownData = new HitCooldownData(this);

    public CombatContainer(CheatPlayer player) {
        super(player);
    }
}
