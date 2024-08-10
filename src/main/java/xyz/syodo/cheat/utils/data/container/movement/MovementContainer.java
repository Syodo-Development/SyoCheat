package xyz.syodo.cheat.utils.data.container.movement;

import lombok.Getter;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.data.Container;
import xyz.syodo.cheat.utils.data.container.movement.elements.PlayerAuthInputData;

@Getter
public class MovementContainer extends Container {

    private final PlayerAuthInputData playerAuthInputData = new PlayerAuthInputData(this);

    public MovementContainer(CheatPlayer player) {
        super(player);
    }
}
