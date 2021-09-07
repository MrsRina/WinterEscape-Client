package club.cpacket.solaros.api.module;

import club.cpacket.solaros.api.feature.Feature;
import club.cpacket.solaros.api.value.Value;

import java.util.HashMap;

/**
 * @author SrRina
 * @since 06/09/2021 at 18:43
 **/
public class Module extends Feature {
    private final HashMap<String, Value> register = new HashMap<String, Value>();
    private int type;

    public Module(final String tag, final String description, int type) {
        super(tag, description);

        this.setType(type);
    }

    public void registry(Value value) {
        this.register.put(value.getTag(), value);
    }

    public void remove(Value value) {
        this.register.remove(value.getTag());
    }

    public Value getValue(String tag) {
        return this.register.get(tag);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
