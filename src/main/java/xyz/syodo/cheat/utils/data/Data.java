package xyz.syodo.cheat.utils.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.syodo.cheat.utils.CheatPlayer;

@AllArgsConstructor
@Getter
public abstract class Data {

    private final Container container;

    public abstract CheatResponse doCheck();

}
