package xyz.syodo.cheat.utils.data.container.combat.elements;

import org.powernukkitx.Player;
import org.powernukkitx.math.Vector3;
import org.powernukkitx.math.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.data.ItemUseOnActorInventoryTransaction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.Iterator;

public class AimlockData extends Data {

    @Setter
    private static int REMEMBER_HITS = 10;
    @Setter
    private static int MIN_HITS_TO_TRIGGER = 5;
    @Setter
    private static double ANGLE_TRIGGER = 1;

    public ObjectArrayList<AngleElement> headPlayerAngles = new ObjectArrayList<>();

    public AimlockData(Container container) {
        super(container);
    }

    public void addAngle(ItemUseOnActorInventoryTransaction entityData) {
        if(getContainer().getPlayer().getPlayer().getLevel().getEntity(entityData.getRuntimeId()) == null) return;
        headPlayerAngles.addLast(new AngleElement(entityData));
        while (headPlayerAngles.size() > REMEMBER_HITS) {
            headPlayerAngles.removeFirst();
        }
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.AIMLOCK);
        if(headPlayerAngles.size() >= MIN_HITS_TO_TRIGGER) {
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

        private final ItemUseOnActorInventoryTransaction data;
        private final Vector3f targetPos;
        private final double angle;
        private final long time;

        public AngleElement(ItemUseOnActorInventoryTransaction data) {
            this.time = System.currentTimeMillis();
            this.data = data;
            Player player = getContainer().getPlayer().getPlayer();
            var fromPosition = data.getFromPosition();
            Vector3 playerloc = new Vector3(fromPosition.getX(), fromPosition.getY(), fromPosition.getZ());
            targetPos = player.getLevel().getEntity(data.getRuntimeId()).asVector3f().clone();
            double anglePosition = Math.abs(Math.toDegrees(Math.atan2(targetPos.x - playerloc.x, targetPos.z - playerloc.z)));
            double angleVector = Math.abs(Math.toDegrees(Math.atan2(player.getDirectionVector().x, player.getDirectionVector().z)));
            angle = Math.abs(anglePosition - angleVector);
        }
    }
}
