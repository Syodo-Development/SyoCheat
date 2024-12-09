package xyz.syodo.cheat.utils.data.container.combat;

import lombok.Getter;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.container.combat.elements.AimBotData;
import xyz.syodo.cheat.utils.data.container.combat.elements.CPSData;
import xyz.syodo.cheat.utils.data.container.combat.elements.HitCooldownData;
import xyz.syodo.cheat.utils.data.container.combat.elements.ReachData;

@Getter
public class CombatContainer extends Container {

    private final CPSData cpsData = new CPSData(this);
    private final HitCooldownData hitCooldownData = new HitCooldownData(this);
    private final ReachData reachData = new ReachData(this);
    private final AimBotData aimBotData = new AimBotData(this);

    public CombatContainer(CheatPlayer player) {
        super(player);
    }
}
