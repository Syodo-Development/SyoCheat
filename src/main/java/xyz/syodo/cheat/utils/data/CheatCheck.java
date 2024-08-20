package xyz.syodo.cheat.utils.data;

import lombok.Getter;
import lombok.Setter;

public enum CheatCheck {

    OTHER(0),
    SPEED(20),
    TIMER(10),
    AUTOCLICKER(20),
    REACH(20, 2),
    VELOCITY(20);

    @Getter
    @Setter
    private Integer cheatpoints;
    @Getter
    @Setter
    private Integer broadcastRequirement;

    CheatCheck(int cheatpoints) {
        this(cheatpoints, 1);
    }
    CheatCheck(int cheatpoints, int broadcastRequirement) {
        this.cheatpoints = cheatpoints;
        this.broadcastRequirement = broadcastRequirement;
    }
}
