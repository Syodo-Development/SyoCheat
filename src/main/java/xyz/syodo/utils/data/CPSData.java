package xyz.syodo.utils.data;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CPSData {

    @Setter
    private static Integer REMEMBERCPS = 20;

    private Integer clicks = 0;
    public List<Integer> CPS = new ArrayList();

    public void addClick() {
        clicks++;
    }

    public void saveCPS() {
        
    }

}
