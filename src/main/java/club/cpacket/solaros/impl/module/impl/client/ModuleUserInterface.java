package club.cpacket.solaros.impl.module.impl.client;

import club.cpacket.solaros.api.module.Module;
import club.cpacket.solaros.api.module.type.ModuleType;

/**
 * @author SrRina
 * @since 08/09/2021 at 14:59
 **/
public class ModuleUserInterface extends Module {
    public ModuleUserInterface() {
        super("UserInterface", "User interface for client.", ModuleType.CLIENT);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
