package xyz.syodo.cheat.utils.data;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CheatResponse {

    private final CheatCheck check;
    private boolean cheating = false;
    private final Object2ObjectLinkedOpenHashMap<String, Object> metaData = new Object2ObjectLinkedOpenHashMap<>();

    public String toString() {
        return this.getClass().getSimpleName() + "[" + check.name().toUpperCase() + ", cheating=" + cheating + ", metadata=" + metaData + "]";
    }

}
