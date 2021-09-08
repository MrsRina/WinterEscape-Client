package club.cpacket.solaros.api.module;

import club.cpacket.solaros.Client;
import club.cpacket.solaros.api.feature.Feature;
import club.cpacket.solaros.api.value.Value;
import club.cpacket.solaros.api.value.type.BindBox;
import club.cpacket.solaros.api.value.type.CheckBox;
import club.cpacket.solaros.api.value.type.Combobox;

import java.util.HashMap;

/**
 * @author SrRina
 * @since 06/09/2021 at 18:43
 **/
public class Module extends Feature {
    private final HashMap<String, Value> register = new HashMap<String, Value>();
    private int type;

    private Combobox toggleMessage;
    private BindBox keyBind;

    public Module(final String tag, final String description, int type) {
        super(tag, description);

        this.setType(type);

        this.registry(this.keyBind = new BindBox("KeyBind", "Set module key bind.", false));
        this.registry(this.toggleMessage = new Combobox("Toggle Message", "ToggleMessage", "Silent", "Silent", "Static", "Disabled"));
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

    public void setKey(int key) {
        this.keyBind.setKey(key);
    }

    public boolean equalsKeyBind(int key) {
        return keyBind.getKey() == key;
    }

    public boolean isEnabled() {
        return keyBind.getValue();
    }

    public void reload(boolean state) {
        if (keyBind.getValue() != state) {
            if (state) {
                this.setListener();
            } else {
                this.unsetListener();
            }
        }
    }

    public void setListener() {
        this.keyBind.setValue(true);

        this.onEnable();

        if (this.toggleMessage.is("Silent")) {
            Client.info(this.getTag() + " enabled");
        } else if (this.toggleMessage.is("Static")) {
            Client.log(this.getTag() + " " + " enabled");
        }

        Client.EVENT_BUS.register(this);
    }

    public void unsetListener() {
        this.keyBind.setValue(false);

        this.onDisable();

        if (this.toggleMessage.is("Silent")) {
            Client.info(this.getTag() + " disabled");
        } else if (this.toggleMessage.is("Static")) {
            Client.log(this.getTag() + " " + " disabled");
        }

        Client.EVENT_BUS.unregister(this);
    }

    protected boolean nullable() {
        return mc.player == null || mc.world == null;
    }

    public void onEnable() {}
    public void onDisable() {}

    public void onOverlayRender(float partialTicks) {

    }

    public void onWorldRender(float partialTicks) {

    }
}
