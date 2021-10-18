package me.rina.hyperpop.impl.module.management;

import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.impl.module.impl.client.ModuleUserInterface;
import me.rina.hyperpop.impl.module.impl.combat.ModuleKillAura;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 06/09/2021 at 19:01
 **/
public class ModuleManager extends Feature {
    public static ModuleManager INSTANCE;

    private final List<Module> moduleList = new ArrayList<>();

    public ModuleManager() {
        super("Module", "Process all modules from Solaros client.");

        INSTANCE = this;
    }

    public void preInitAll() {
        this.add(new ModuleKillAura());
        this.add(new ModuleUserInterface());

        // Dev thing.
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
    }

    public void onKeyboard(int key) {
        for (Module modules : this.getModuleList()) {
            if (modules.equalsKeyBind(key)) {
                modules.reload(!modules.isEnabled());
            }
        }
    }

    public void onWorldRender(float partialTicks) {
        for (Module modules : this.getModuleList()) {
            if (modules.isEnabled()) {
                modules.onWorldRender(partialTicks);
            }
        }
    }

    public void onOverlayRender(float partialTicks) {
        for (Module modules: this.getModuleList()) {
            if (modules.isEnabled()) {
                modules.onOverlayRender(partialTicks);
            }
        }
    }

    public void add(Module module) {
        this.moduleList.add(module);
    }

    public void remove(Module module) {
        this.moduleList.remove(module);
    }

    public static Module get(String tag) {
        Module module = null;

        for (Module modules : INSTANCE.getModuleList()) {
            if (modules.getTag().equalsIgnoreCase(tag)) {
                module = modules;

                break;
            }
        }

        return module;
    }

    // lol
    public static Module get(Module module) {
        return get(module.getTag());
    }

    public List<Module> getModuleList() {
        return moduleList;
    }
}
