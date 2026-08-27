package xyz.syodo.cheat.utils.data.container.combat.elements;

import org.powernukkitx.event.entity.EntityDamageByEntityEvent;
import org.powernukkitx.event.entity.EntityDamageEvent;
import org.powernukkitx.math.Vector3f;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;

public class ReachData extends Data {

    @Setter
    private static int REMEMBER_HITS = 10;
    @Setter
    private static double FLAG_REACH_AT = 4.6;
    @Setter
    private static double FLAG_REACH_AT_AVERAGE = 4.5;

    final LinkedHashMap<Long, ReachElement> reachElements = new LinkedHashMap<>();

    public ReachData(Container container) {
        super(container);
    }

    public CheatResponse addReachElement(EntityDamageByEntityEvent event) {
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return new CheatResponse(CheatCheck.REACH);
        reachElements.putLast(System.currentTimeMillis(), new ReachElement(event));
        while (reachElements.size() > REMEMBER_HITS) {
            reachElements.remove(reachElements.firstEntry().getKey());
        }
        return doCheck();
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.REACH);
        int count = reachElements.size();
        double highest = 0;
        double lowest = Integer.MAX_VALUE;
        double average = 0;
        Iterator<Long> iterator = reachElements.keySet().iterator();
        while(iterator.hasNext()) {
            long time = iterator.next();
            ReachElement element = reachElements.get(time);
            double distance = element.getDamagerPos().distance(element.getDamagedPos());
            if(distance > highest) highest = distance;
            if(distance < lowest) lowest = distance;
            average += distance;
            if(!iterator.hasNext()) {
                if(distance > FLAG_REACH_AT) {
                    response.setCheating(true);
                    response.getMetaData().put("current", distance);
                }
            }
        }
        average /= count;

        if (average >= FLAG_REACH_AT_AVERAGE && count > 3) {
            response.setCheating(true);
            response.getCheck().setBroadcastRequirement(1);
            response.getMetaData().put("lowest", lowest);
            response.getMetaData().put("highest", highest);
            response.getMetaData().put("average", average);
        }
        return response;
    }

    @Getter
    public static class ReachElement {

        private final Vector3f damagerPos;
        private final Vector3f damagedPos;

        public ReachElement(EntityDamageByEntityEvent event) {
            this.damagerPos = event.getDamager().asVector3f().setY(0);
            this.damagedPos = event.getEntity().asVector3f().setY(0);
        }
    }
}
