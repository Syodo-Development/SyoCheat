package xyz.syodo.cheat.utils.data.container.movement.elements;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.*;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.Arrays;
import java.util.Collections;

public class FlyData extends Data {

    @Setter
    private static int REQUIRED_MISSMATCH = 5;
    @Setter
    private static int MINIMUM_AIRTIME = 10;
    @Setter
    private static boolean INTERUPT_VANILLA_FLY = true;


    @Getter
    private long lastToggleFlight = System.currentTimeMillis();

    private Pair<Long, Vector3> lastMotion;

    private FlightEntry lastEntry;
    private int flyMismatch = 0;

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
                    response.getMetaData().put("airtime", entry.getAirTime());
                    response.getMetaData().put("difference", entry.Y - lastEntry.Y);
                    if(INTERUPT_VANILLA_FLY) {
                        player.getAdventureSettings().sendAbilities(Collections.singleton(player));
                    }
                    if(flyMismatch++ >= REQUIRED_MISSMATCH && entry.airTime >= MINIMUM_AIRTIME) {
                        response.setCheating(true);
                        lastEntry = null;
                        flyMismatch = 0;
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

    public void teleported() {
        lastEntry = null;
        flyMismatch = 0;
    }
}
