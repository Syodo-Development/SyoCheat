package xyz.syodo.cheat.utils.data.container.combat.elements;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import lombok.Setter;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.HashMap;

public class CPSData extends Data {

    @Setter
    private static int REMEMBER_CPS = 10;
    @Setter
    private static double MINIMUM_AVERAGE_CPS = 6.0D;
    @Setter
    private static int MINIMUM_SAMPLES = 8;
    @Setter
    private static double MINIMUM_LOWEST_CPS = 4.0D;
    @Setter
    private static double CONSISTENCY_DIVISOR = 5.0D;

    private int clicks = 0;
    public IntArrayList CPS = new IntArrayList();

    public CPSData(Container container) {
        super(container);
    }

    public void addClick() {
        clicks++;
    }

    public CheatResponse saveCPS() {
        if(!SyoCheat.isENABLED()) return new CheatResponse(CheatCheck.OTHER);
        CPS.addLast(clicks);
        clicks = 0;
        while (CPS.size() > REMEMBER_CPS) {
            CPS.removeFirst();
        }
        return doCheck();
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.AUTOCLICKER);
        int count = CPS.size();
        int highest = 0;
        int lowest = Integer.MAX_VALUE;
        double average = 0;
        for(Integer i : CPS){
            if(i > highest) highest = i;
            if(i < lowest) lowest = i;
            average += i;
        }
        average /= count;

        if (average >= MINIMUM_AVERAGE_CPS && count >= MINIMUM_SAMPLES && lowest >= MINIMUM_LOWEST_CPS
                && average - lowest < average / CONSISTENCY_DIVISOR) {
            response.setCheating(true);
            response.getMetaData().put("lowest", lowest);
            response.getMetaData().put("highest", highest);
            response.getMetaData().put("average", average);
            //removing the latest 7 elements to prevent the same data to trigger the autoclicker check again and again
            CPS.removeElements((count-1) - 3, count-1);
        }
        return response;
    }
}
