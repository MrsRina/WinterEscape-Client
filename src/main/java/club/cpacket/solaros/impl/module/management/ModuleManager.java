package club.cpacket.solaros.impl.module.management;

import club.cpacket.solaros.api.feature.Feature;
import club.cpacket.solaros.api.module.Module;

import java.util.HashMap;

/**
 * @author SrRina
 * @since 06/09/2021 at 19:01
 **/
public class ModuleManager extends Feature {
    public static ModuleManager INSTANCE;

    private final HashMap<String, Module> register = new HashMap<String, Module>();

    public ModuleManager() {
        super("Module", "Process all modules from Solaros client.");

        INSTANCE = this;
    }

    public void registry(Module module) {
        this.register.put(module.getTag(), module);
    }

    public void remove(Module module) {
        this.register.remove(module.getTag());
    }

    public static Module get(String tag) {
        return INSTANCE.getRegister().get(tag);
    }

    // lol
    public static Module get(Module module) {
        return INSTANCE.getRegister().get(module.getTag());
    }

    public HashMap<String, Module> getRegister() {
        return register;
    }
}
