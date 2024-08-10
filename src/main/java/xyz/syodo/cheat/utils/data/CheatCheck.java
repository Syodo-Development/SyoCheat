package xyz.syodo.cheat.utils.data;

import lombok.Getter;
import lombok.Setter;

public enum CheatCheck {

    OTHER(0),
    SPEED(20),
    TIMER(10),
    AUTOCLICKER(20);

    @Getter
    @Setter
    private Integer cheatpoints;

    CheatCheck(int cheatpoints) {
        this.cheatpoints = cheatpoints;
    }
}
