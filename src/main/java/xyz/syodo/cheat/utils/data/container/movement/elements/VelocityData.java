package xyz.syodo.cheat.utils.data.container.movement.elements;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.ArrayList;
import java.util.List;

public class VelocityData extends Data {

    @Setter
    private static int REMEMBER_VELO = 3;
    @Setter
    private static int ALLOWED_DIFFERENCE = 180;

    private int clicks = 0;

    private EntityMotionEvent event;
    private Vector3f location;
    private Long damaged = System.currentTimeMillis();
    private DoubleArrayList distances = new DoubleArrayList();

    public VelocityData(Container container) {
        super(container);
    }

    public boolean setMotion(EntityMotionEvent event) {
        if(this.event != null) return false;
        this.location = event.getEntity().asVector3f().setY(0);
        Player p = (Player) event.getEntity();
        Vector3 motion = event.getMotion();
        AxisAlignedBB axis = p.boundingBox.clone().getOffsetBoundingBox(motion.x/2, (motion.y/2) + 0.1f, motion.z/2).grow(0.2, 0, 0.2);
        for(double x = axis.getMinX(); x <= axis.getMaxX(); x+=0.5){
            for(double y = axis.getMinY(); y <= axis.getMaxY(); y+=0.5){
                for(double z = axis.getMinZ(); z <= axis.getMaxZ(); z+=0.5){
                    Block b = p.level.getBlock(new Vector3(x,y,z));
                    if(b.isSolid() || b.getId().equals(Block.WEB) || b.getId().contains("slab")) {
                        return false;
                    }
                }
            }
        }
        this.event = event;
        return true;
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.VELOCITY);
        Vector3f playerloc = getContainer().getPlayer().getPlayer().asVector3f().setY(0);
        double distance = location.distance(playerloc);
        distances.addLast(distance);
        while (distances.size() > REMEMBER_VELO) {
            distances.removeFirst();
        }
        double average = 0;
        for(double d : distances) average += d;
        average /= distances.size();
        if(event.getMotion().length() < 0.5) {
            return new CheatResponse(CheatCheck.OTHER);
        }
        if(average <= event.getMotion().length()) {
            response.getMetaData().put("average", average);
            response.getMetaData().put("length", event.getMotion().length());
            response.setCheating(true);
        }

        if(distance > 0.1 && System.currentTimeMillis() - damaged > 1000) {
            double anglePosition = Math.abs(Math.toDegrees(Math.atan2(playerloc.x - location.x, playerloc.z - location.z)));
            double angleVector = Math.abs(Math.toDegrees(Math.atan2(event.getMotion().x, event.getMotion().z)));
            double angleDiff = Math.abs(anglePosition - angleVector);
            if(angleDiff > ALLOWED_DIFFERENCE) {
                response.setCheating(true);
                response.getMetaData().put("angle", angleDiff);
            }
        }

        this.event = null;
        return response;
    }

    public void damaged() {
        this.damaged = System.currentTimeMillis();
    }
}
