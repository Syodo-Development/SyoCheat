package xyz.syodo.cheat.utils.data.container.combat.elements;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import lombok.Getter;
import lombok.Setter;

public class HitCooldownData {

    private Long lastHit = System.currentTimeMillis();

    public boolean hit() {
        if(System.currentTimeMillis() - lastHit < 333L) {
            return false;
        }
        lastHit = System.currentTimeMillis();
        return true;
    }

}
