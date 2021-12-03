package me.rina.winterescape.api.module;

import event.bus.EventBus;
import me.rina.winterescape.Client;
import me.rina.winterescape.api.feature.Feature;
import me.rina.winterescape.api.value.Value;
import me.rina.winterescape.api.value.type.BindBox;
import me.rina.winterescape.api.value.type.Combobox;

import java.util.*;

/**
 * @author SrRina
 * @since 06/09/2021 at 18:43
 **/
public class Module extends Feature {
    private final List<Value> valueList = new ArrayList<>();
    private int type;

    private Combobox toggleMessage;
    private BindBox keyBind;

    public Module(final String tag, final String description, int type) {
        super(tag, description);

        this.setType(type);

        this.registry(this.keyBind = new BindBox("BIND", "Set module key bind.", false));
        this.registry(this.toggleMessage = new Combobox("Alert", "Message when toggled the module.", "Silent", "Silent", "Static", "Disabled"));
    }

    public Value registry(Value value) {
        this.valueList.add(value);

        return value;
    }

    public void remove(Value value) {
        this.valueList.remove(value);
    }

    public List<Value> getValueList() {
        return valueList;
    }

    public Value getValue(String tag) {
        Value value = null;

        for (Value values : this.getValueList()) {
            if (values.getTag().equalsIgnoreCase(tag)) {
                value = values;

                break;
            }
        }

        return value;
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

    public void reloadListener() {
        this.keyBind.setValue(!this.isEnabled());

        if (this.isEnabled()) {
            this.setListener();
        } else {
            this.unsetListener();
        }
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

    public void refresh() {
        if (keyBind.getValue()) {
            this.setListener();
        } else {
            this.unsetListener();
        }
    }

    public void setListener() {
        if (this.keyBind.getValue()) {
            return;
        }

        this.keyBind.setValue(true);
        this.onEnable();

        if (this.toggleMessage.is("Silent")) {
            Client.info(this.getTag() + " enabled");
        } else if (this.toggleMessage.is("Static")) {
            Client.log(this.getTag() + " " + " enabled");
        }

        EventBus.INSTANCE.register(this);
    }

    public void unsetListener() {
        if (!this.keyBind.getValue()) {
            return;
        }

        this.keyBind.setValue(false);
        this.onDisable();

        if (this.toggleMessage.is("Silent")) {
            Client.info(this.getTag() + " disabled");
        } else if (this.toggleMessage.is("Static")) {
            Client.log(this.getTag() + " " + " disabled");
        }

        EventBus.INSTANCE.unregister(this);
    }

    protected boolean nullable() {
        return mc.player == null || mc.world == null;
    }

    public void onSetting() {
        
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onOverlayRender(float partialTicks) {

    }

    public void onWorldRender(float partialTicks) {

    }

    public void onShutdown() {

    }
}
