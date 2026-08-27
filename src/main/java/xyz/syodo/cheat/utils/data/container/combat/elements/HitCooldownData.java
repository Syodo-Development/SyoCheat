package xyz.syodo.cheat.utils.data.container.combat.elements;

import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

public class HitCooldownData extends Data {

    @Setter
    private static long MINIMUM_HIT_INTERVAL_MS = 333L;

    private Long lastHit = System.currentTimeMillis();

    public HitCooldownData(Container container) {
        super(container);
    }

    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.OTHER);
        if(System.currentTimeMillis() - lastHit < MINIMUM_HIT_INTERVAL_MS) {
            response.setCheating(true);
        } else {
            lastHit = System.currentTimeMillis();
        }
        return response;
    }

}
