package xyz.syodo.cheat.utils.data.container.combat.elements;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import lombok.Setter;

public class CPSData {

    @Setter
    private static int REMEMBER_CPS = 20;

    private int clicks = 0;
    public IntArrayList CPS = new IntArrayList();

    public void addClick() {
        clicks++;
    }

    public void saveCPS() {
        CPS.add(clicks);
        clicks = 0;
        if(CPS.size() > REMEMBER_CPS) {
            CPS.removeFirst();
        }
    }
}
