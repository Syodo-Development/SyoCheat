package xyz.syodo.cheat.utils.data.container.movement.elements;

import org.powernukkitx.Player;
import org.powernukkitx.block.*;
import org.powernukkitx.event.entity.EntityMotionEvent;
import org.powernukkitx.math.AxisAlignedBB;
import org.powernukkitx.math.SimpleAxisAlignedBB;
import org.powernukkitx.math.Vector3;
import it.unimi.dsi.fastutil.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlyData extends Data {

    @Setter
    private static int REQUIRED_MISSMATCH = 5;
    @Setter
    private static int REQUIRED_MISSMATCH_0 = 10;
    @Setter
    private static int MINIMUM_AIRTIME = 10;
    @Setter
    private static boolean INTERUPT_VANILLA_FLY = true;


    @Getter
    private long lastToggleFlight = System.currentTimeMillis();

    @Getter
    private Pair<Long, Vector3> lastMotion;

    private FlightEntry lastEntry;
    private int flyMismatch = 0;

    private int zeroHeightMismatch = 0;

    public FlyData(Container container) {
        super(container);
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.FLY);
        Player player = getContainer().getPlayer().getPlayer();

        AxisAlignedBB box = new SimpleAxisAlignedBB(new Vector3(player.getX()-.5, player.getY()-2, player.getZ()-.5).floor(), new Vector3(player.getX()+.5, player.getY()+2, player.getZ()+.5).ceil());
        if(!player.getAllowFlight() && player.level.getCollisionBlocks(box, false, true).length == 0) {

            FlightEntry entry = new FlightEntry();
            if(lastEntry != null) {
                if(lastMotion != null) {
                    if(System.currentTimeMillis() - lastMotion.first() < 3000) {
                        return response;
                    }
                }
                if(lastEntry.Y <= entry.Y) {
                    if(lastEntry.Y == entry.Y)  {
                        if(Arrays.stream(player.level.getCollisionBlocks(player.getBoundingBox(), false, true)).anyMatch(b -> b instanceof BlockFlowable)) return response;
                    }
                    double heightDiff = entry.Y - lastEntry.Y;
                    response.getMetaData().put("airtime", entry.getAirTime());
                    response.getMetaData().put("difference", heightDiff);
                    if(INTERUPT_VANILLA_FLY) {
                        player.getAdventureSettings().sendAbilities(Collections.singleton(player));
                    }
                    if(flyMismatch++ >= REQUIRED_MISSMATCH && entry.airTime >= MINIMUM_AIRTIME) {

                        response.setCheating(true);
                        lastEntry = null;
                        flyMismatch = 0;
                        if(heightDiff == 0 && ++zeroHeightMismatch < REQUIRED_MISSMATCH_0) {
                            response.setCheating(false);
                        } else zeroHeightMismatch = 0;
                        return response;
                    }
                }
            }
            lastEntry = entry;
        } else {
            if(flyMismatch != 0) {
                teleported();
            }
        }
        return response;
    }

    public void toggledFlight() {
        lastToggleFlight = System.currentTimeMillis();
    }

    public void setLastMotion(EntityMotionEvent event) {
        this.lastMotion = Pair.of(System.currentTimeMillis(), event.getMotion());
    }

    @Getter
    public class FlightEntry {

        private final double Y;
        private final float airTime;

        public FlightEntry() {
            Player player = getContainer().getPlayer().getPlayer();
            this.Y = player.getY();
            this.airTime = player.getInAirTicks();
        }
    }

    @Getter
    @AllArgsConstructor
    public class MismatchData {
        private final double heightDif;
    }

    public void teleported() {
        lastEntry = null;
        flyMismatch = 0;
        zeroHeightMismatch = 0;
    }
}
