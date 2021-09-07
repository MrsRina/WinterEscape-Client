package club.cpacket.solaros;

import club.cpacket.solaros.impl.command.management.CommandManager;
import club.cpacket.solaros.impl.module.management.ModuleManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "Solaros", version = "0.1")
public class Client {
    public ModuleManager moduleManager;
    public CommandManager commandManager;

    @Mod.Instance
    public static Client INSTANCE;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
    }
}