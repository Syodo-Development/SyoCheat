package xyz.syodo.cheat.utils.data.container.combat;

import lombok.Getter;
import xyz.syodo.cheat.utils.data.container.combat.elements.CPSData;
import xyz.syodo.cheat.utils.data.container.combat.elements.HitCooldownData;

@Getter
public class CombatContainer {

    private final CPSData cpsData = new CPSData();
    private final HitCooldownData hitCooldownData = new HitCooldownData();

}
