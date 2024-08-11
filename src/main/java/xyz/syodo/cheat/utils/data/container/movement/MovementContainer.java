package xyz.syodo.cheat.utils.data.container.movement;

import lombok.Getter;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.container.movement.elements.PlayerAuthInputData;
import xyz.syodo.cheat.utils.data.container.movement.elements.PlayerTimedLocationData;

@Getter
public class MovementContainer extends Container {

    private final PlayerAuthInputData playerAuthInputData = new PlayerAuthInputData(this);
    private final PlayerTimedLocationData playerTimedLocationData = new PlayerTimedLocationData(this);

    public MovementContainer(CheatPlayer player) {
        super(player);
    }
}
