package me.rina.zyra.api.preset;

import com.google.gson.JsonObject;
import me.rina.zyra.api.feature.Feature;
import me.rina.zyra.api.preset.util.PresetUtil;

/**
 * @author SrRina
 * @since 20/10/2021 at 13:05
 **/
public class Preset extends Feature {
    private String lastTimeHandled;
    private String path;

    private JsonObject data;

    public Preset(String name, String description) {
        super(name, description);

        this.lastTimeHandled = PresetUtil.getToday();
    }

    public Preset(String name, String description, String lastTimeHandled) {
        super(name, description);

        this.lastTimeHandled = lastTimeHandled;
    }

    public Preset(String name, String description, String lastTimeHandled, String path) {
        super(name, description);

        this.lastTimeHandled = lastTimeHandled;
        this.path = path;
    }

    public Preset(String name, String description, String lastTimeHandled, String path, JsonObject data) {
        super(name, description);

        this.lastTimeHandled = lastTimeHandled;

        this.path = path;
        this.data = data;
    }

    public void setLastTimeHandled(String lastTimeHandled) {
        this.lastTimeHandled = lastTimeHandled;
    }

    public String getLastTimeHandled() {
        return lastTimeHandled;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public JsonObject getData() {
        return data;
    }
}
