package xyz.syodo.cheat.utils.data.container.combat.elements;

import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

public class HitCooldownData extends Data {

    private Long lastHit = System.currentTimeMillis();

    public HitCooldownData(Container container) {
        super(container);
    }

    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.OTHER);
        if(System.currentTimeMillis() - lastHit < 333L) {
            response.setCheating(true);
        } else {
            lastHit = System.currentTimeMillis();
        }
        return response;
    }

}
