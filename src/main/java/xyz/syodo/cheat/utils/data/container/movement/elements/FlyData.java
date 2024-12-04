package xyz.syodo.cheat.utils.data.container.movement.elements;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

public class FlyData extends Data {

    @Setter
    private static int REQUIRED_MISSMATCH = 20;

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
        if(true) return response;
        Player player = getContainer().getPlayer().getPlayer();
        boolean isTrueOnGround = false;
        AxisAlignedBB box = player.getBoundingBox().grow(0,.7,0).offset(0, -.6, 0);
        for(int x = (int) box.getMinX(); x < box.getMaxX(); x++) {
            for(int z = (int) box.getMinZ(); z < box.getMaxZ(); z++) {
                for(int y = (int) box.getMinY(); y < box.getMaxY(); y++) {
                    if(!player.getLevel().getBlock(x, y, z).isAir()) {
                        isTrueOnGround = true;
                        break;
                    }
                }
            }
        }
        if(!isTrueOnGround) {
            FlightEntry entry = new FlightEntry();
            if(lastEntry != null) {
                if(lastMotion != null) {
                    if(System.currentTimeMillis() - lastMotion.first() < 1000 * lastMotion.second().y) {
                        return response;
                    }
                }
                if(lastEntry.Y >= entry.Y) {
                    if(flyMismatch++ >= REQUIRED_MISSMATCH) {
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
                lastEntry = null;
                flyMismatch = 0;
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

}
