package xyz.syodo.cheat.utils.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Getter
@Setter
public class CheatResponse {

    private final CheatCheck check;
    private boolean cheating = false;
    private final LinkedHashMap<String, Object> metaData = new LinkedHashMap<>();

    public String toString() {
        return this.getClass().getSimpleName() + "[" + check.name().toUpperCase() + ", cheating=" + cheating + ", metadata=" + new JSONObject(metaData).toString() + "]";
    }

}
