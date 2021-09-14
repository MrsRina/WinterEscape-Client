package club.cpacket.solaros;

import club.cpacket.solaros.api.social.management.SocialManager;
import club.cpacket.solaros.impl.command.management.CommandManager;
import club.cpacket.solaros.impl.forge.ForgeInteract;
import club.cpacket.solaros.impl.gui.GUI;
import club.cpacket.solaros.impl.gui.impl.module.ModuleGUI;
import club.cpacket.solaros.impl.module.management.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import event.bus.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author SrRina
 * @since 06/09/2021 at 19:01
 **/
@Mod(modid = Client.CLIENT_NAME, version = Client.CLIENT_VERSION)
public class Client {
    public static final String CLIENT_NAME = "solaros";
    public static final String CLIENT_VERSION = "0.1";

    public ModuleManager moduleManager;
    public CommandManager commandManager;

    public SocialManager socialManager;
    public ForgeInteract forgeInteract;

    public GUI guiModule;

    public static EventBus EVENT_BUS = EventBus.INSTANCE;

    @Mod.Instance
    public static Client INSTANCE;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.createClassesInClient();
        this.registryClassesInEventListener();
        this.initClient();
    }

    public void createClassesInClient() {
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.socialManager = new SocialManager();
        this.forgeInteract = new ForgeInteract();
        this.guiModule = new ModuleGUI();
    }

    public void registryClassesInEventListener() {
        MinecraftForge.EVENT_BUS.register(this.forgeInteract);
        EVENT_BUS.register(this.guiModule);
    }

    public void initClient() {
        this.moduleManager.preInitAll();
        this.commandManager.preInitAll();
    }

    public static void log(final String message) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.world == null) {
            return;
        }

        final String prefix = ChatFormatting.LIGHT_PURPLE + "@" + CLIENT_NAME + " " + ChatFormatting.GRAY;

        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + message));
    }

    public static void info(final String message) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.world == null) {
            return;
        }

        final String prefix = ChatFormatting.LIGHT_PURPLE + "@" + CLIENT_NAME + " " + ChatFormatting.GRAY;

        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + message), 69);
    }
}