package xyz.syodo.cheat.utils.data.container.combat.elements;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.HashMap;

public class CPSData extends Data {

    @Setter
    private static int REMEMBER_CPS = 20;

    private int clicks = 0;
    public IntArrayList CPS = new IntArrayList();

    public CPSData(Container container) {
        super(container);
    }

    public void addClick() {
        clicks++;
    }

    public CheatResponse saveCPS() {
        CPS.add(clicks);
        clicks = 0;
        if(CPS.size() > REMEMBER_CPS) {
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
        average /= (double) count;
        if (average >= 6.0D && count > 7 && lowest >= 4.0D && average - lowest < average / 5.0D) {
            response.setCheating(true);
            response.getMetaData().put("lowest", lowest);
            response.getMetaData().put("highest", highest);
            response.getMetaData().put("average", average);
            //removing the latest 7 elements to prevent the same data to trigger the autoclicker check again and again
            CPS.removeElements((count-1) - 7, count-1);
        }
        return response;
    }
}
