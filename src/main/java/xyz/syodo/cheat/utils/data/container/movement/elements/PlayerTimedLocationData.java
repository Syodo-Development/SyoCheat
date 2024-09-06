package xyz.syodo.cheat.utils.data.container.movement.elements;

import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;
import lombok.Setter;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerTimedLocationData extends Data {

    @Setter
    private static int REMEMBER_LOCATIONS = 20;

    @Setter
    private static double ALLOWED_SINGLE_DISTANCE = 4.2d;
    @Setter
    private static double ALLOWED_AVERAGE_DISTANCE = 3.8d;
    @Setter
    private static boolean TELEPORT_IF_EXCEED = true;

    private Long teleported = System.currentTimeMillis();

    final List<Location> locations = new ArrayList<>();

    public PlayerTimedLocationData(Container container) {
        super(container);
    }

    public CheatResponse addLocation() {
        if(System.currentTimeMillis() - teleported < 500) {
            locations.clear();
            return new CheatResponse(CheatCheck.OTHER);
        }
        locations.addLast(getContainer().getPlayer().getPlayer().getLocation().clone());
        if(locations.size() > REMEMBER_LOCATIONS) {
            locations.removeFirst();
        }
        return doCheck();
    }

    @Override
    public CheatResponse doCheck() {
        CheatResponse response = new CheatResponse(CheatCheck.SPEED);
        if(getContainer().getPlayer().getPlayer().getAllowFlight()) return response;
        if(locations.size() > 10) {
            double highest = 0;
            double lowest = Integer.MAX_VALUE;
            double average = 0;
            Iterator<Location> iterator = new ArrayList<Location>(locations).iterator();
            Location latest = iterator.next();
            while(iterator.hasNext()) {
                Location location = iterator.next();
                double distance = location.clone().setY(0).distance(latest.clone().setY(0));
                if(distance > highest) highest = distance;
                if(distance < lowest) lowest = distance;
                average += distance;
                latest = location;
                if(!iterator.hasNext()) {
                    if(distance > ALLOWED_SINGLE_DISTANCE) {
                        response.setCheating(true);
                        response.getMetaData().put("trigger", "TIMED SINGLE (" + ALLOWED_AVERAGE_DISTANCE + ")");
                        response.getCheck().setCheatpoints((int) (response.getCheck().getCheatpoints() * (distance/ALLOWED_SINGLE_DISTANCE)));
                        if(TELEPORT_IF_EXCEED) {
                            getContainer().getPlayer().getPlayer().teleport(latest, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        }
                    }
                }
            }
            average /= locations.size();
            response.getMetaData().put("lowest", lowest);
            response.getMetaData().put("highest", highest);
            response.getMetaData().put("average", average);
            if(average > ALLOWED_AVERAGE_DISTANCE) {
                response.setCheating(true);
                response.getMetaData().put("trigger", "TIMED AVERAGE (" + ALLOWED_AVERAGE_DISTANCE + ")");
                for(int i = 0; i < (REMEMBER_LOCATIONS / 3) * 2; i++) locations.removeFirst();
            }
        }
        return  response;
    }

    public void teleported() {
        this.teleported = System.currentTimeMillis();
        locations.clear();
    }

}
