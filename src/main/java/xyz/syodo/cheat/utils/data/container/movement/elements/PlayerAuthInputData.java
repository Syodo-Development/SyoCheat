package xyz.syodo.cheat.utils.data.container.movement.elements;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import lombok.Setter;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class PlayerAuthInputData extends Data {

    @Setter
    private static int REMEMBER_PACKETS = 20;
    @Setter
    private static int REMEMBER_AVERAGE = 5;
    @Setter
    private static double ALLOWED_DISTANCE = 0.63;
    @Setter
    private static double ALLOWED_AVERAGE = 0.42;

    private Long teleported = System.currentTimeMillis();

    final LinkedHashMap<Long, PlayerAuthInputPacket> playerAuthInputPackets = new LinkedHashMap<>();
    final List<Double> averages = new ArrayList<>();

    public PlayerAuthInputData(Container container) {
        super(container);
    }

    public CheatResponse addPacket(PlayerAuthInputPacket packet) {
        if(System.currentTimeMillis() - teleported < 500) {
            playerAuthInputPackets.clear();
            return new CheatResponse(CheatCheck.OTHER);
        }
        playerAuthInputPackets.putLast(System.currentTimeMillis(), packet);
        if(playerAuthInputPackets.size() > REMEMBER_PACKETS) {
            playerAuthInputPackets.remove(playerAuthInputPackets.firstEntry().getKey());
        }
        return doCheck();
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.SPEED);
        if(getContainer().getPlayer().getPlayer().getAllowFlight()) return response;
        int count = playerAuthInputPackets.size();
        if(count > 10) {
            long highestTimeDiff = 0;
            long lowestTimeDiff = Integer.MAX_VALUE;
            long averageTimeDiff = 0;

            double highestDistanceDiff = 0;
            double lowestDistanceDiff = Integer.MAX_VALUE;
            double averageDistanceDiff = 0;

            Iterator<Long> iterator = playerAuthInputPackets.keySet().iterator();
            long latest = iterator.next();
            while(iterator.hasNext()) {

                Long time = iterator.next();

                long difference = time - latest;

                PlayerAuthInputPacket priorPacket = playerAuthInputPackets.get(latest);
                PlayerAuthInputPacket currentPacket = playerAuthInputPackets.get(time);
                int tickDiff = (int) (currentPacket.tick - priorPacket.tick);

                Vector3f priorLocation = priorPacket.position.clone().setY(0);
                Vector3f currentLocation = currentPacket.position.clone().setY(0);
                double distance = priorLocation.distance(currentLocation) * (50d/difference);
                if(distance > 2) response.getCheck().setCheatpoints(100);
                if(distance > highestDistanceDiff) highestDistanceDiff = distance;
                if(distance < lowestDistanceDiff) lowestDistanceDiff = distance;
                averageDistanceDiff += distance;
                if(!iterator.hasNext()) {
                    response.getMetaData().put("tickDiff", tickDiff);
                    response.getMetaData().put("timeDiff", difference);
                    if(tickDiff > 1 && (difference < 100 || difference > 510)) {
                        response.getMetaData().put("tickInvalidation", true);
                        return response;
                    }
                    response.getMetaData().put("lastDistance", distance);
                    if(distance > ALLOWED_DISTANCE*1.3f && distance < 2) {
                        getContainer().getPlayer().getPlayer().teleport(priorPacket.position.asVector3());
                    }
                }

                if(difference > highestTimeDiff) highestTimeDiff = difference;
                if(difference < lowestTimeDiff) lowestTimeDiff = difference;
                averageTimeDiff += difference;

                latest = time;
            }

            averageTimeDiff /= count-1;
            averageDistanceDiff /= count-1;

            response.getMetaData().put("lowest", lowestDistanceDiff);
            response.getMetaData().put("highest", highestDistanceDiff);
            response.getMetaData().put("average", averageDistanceDiff);

            if(averageDistanceDiff > ALLOWED_AVERAGE) {
                response.setCheating(true);
            }

            if(playerAuthInputPackets.get(latest).tick%10 == 0) {
                averages.addLast(averageDistanceDiff);
                int averageCount = averages.size();
                if(averageCount > REMEMBER_AVERAGE) {
                    averages.removeFirst();
                    averageCount--;
                }
                if(averageCount > 3) {
                    double averageAverageDistance = 0;
                    for(double averageDistanceEntry : averages) {
                        averageAverageDistance += averageDistanceEntry;
                    }
                    averageAverageDistance /= averageCount;

                    if(averageAverageDistance > ALLOWED_DISTANCE) {
                        response.setCheating(true);
                        response.getCheck().setCheatpoints(34);
                        response.getMetaData().put("AV2", averageAverageDistance);
                        averages.clear();
                    }
                }
            }

            if(averageTimeDiff < 45) {
                response = new CheatResponse(CheatCheck.TIMER);
                response.setCheating(true);
                response.getMetaData().put("lowest", lowestTimeDiff);
                response.getMetaData().put("highest", highestTimeDiff);
                response.getMetaData().put("average", averageTimeDiff);
            }
        }
        if(response.isCheating()) {
            playerAuthInputPackets.clear();
        }
        return response;
    }

    public void teleported() {
        this.teleported = System.currentTimeMillis();
        playerAuthInputPackets.clear();
    }

}
