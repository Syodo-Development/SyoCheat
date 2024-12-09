package xyz.syodo.cheat.utils.data.container.combat.elements;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemOnEntityData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.Iterator;

public class AimBotData extends Data {

    @Setter
    private static int REMEMBER_HITS = 10;
    @Setter
    private static double ANGLE_TRIGGER = 1;

    public ObjectArrayList<AngleElement> headPlayerAngles = new ObjectArrayList<>();

    public AimBotData(Container container) {
        super(container);
    }

    public CheatResponse addAngle(UseItemOnEntityData entityData) {
        headPlayerAngles.addLast(new AngleElement(entityData));
        while (headPlayerAngles.size() > REMEMBER_HITS) {
            headPlayerAngles.removeFirst();
        }
        return doCheck();
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.AIMLOCK);
        if(!headPlayerAngles.isEmpty()) {
            double highest = 0;
            double lowest = Double.MAX_VALUE;
            double average = 0;
            Iterator<AngleElement> iterator = headPlayerAngles.iterator();
            int count = 0;
            AngleElement latest = iterator.next();
            while(iterator.hasNext()) {
                AngleElement current = iterator.next();
                if(!latest.targetPos.equals(current.targetPos)) {
                    double i = current.angle;
                    if (i > highest) highest = i;
                    if (i < lowest) lowest = i;
                    average += i;
                    count++;
                }
                latest = current;
            }

            if(count != 0) {
                average /= count;
                if (average < ANGLE_TRIGGER) {
                    response.setCheating(true);
                    response.getMetaData().put("highest", highest);
                    response.getMetaData().put("lowest", lowest);
                    response.getMetaData().put("average", average);
                }
            }
        }
        return response;
    }

    @Getter
    @lombok.Data
    public class AngleElement {

        private final UseItemOnEntityData data;
        private final Vector3f targetPos;
        private final double angle;
        private final long time;

        public AngleElement(UseItemOnEntityData data) {
            this.time = System.currentTimeMillis();
            this.data = data;
            Player player = getContainer().getPlayer().getPlayer();
            Vector3 playerloc = data.playerPos;
            targetPos = player.getLevel().getEntity(data.entityRuntimeId).asVector3f().clone();
            double anglePosition = Math.abs(Math.toDegrees(Math.atan2(targetPos.x - playerloc.x, targetPos.z - playerloc.z)));
            double angleVector = Math.abs(Math.toDegrees(Math.atan2(player.getDirectionVector().x, player.getDirectionVector().z)));
            angle = Math.abs(anglePosition - angleVector);
        }
    }
}
