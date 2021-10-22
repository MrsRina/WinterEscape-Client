package me.rina.hyperpop;

import me.rina.hyperpop.api.preset.management.PresetManager;
import me.rina.hyperpop.api.social.management.SocialManager;
import me.rina.hyperpop.impl.command.management.CommandManager;
import me.rina.hyperpop.impl.forge.ForgeInteract;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.module.management.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import event.bus.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author SrRina
 * @since 06/09/2021 at 19:01
 **/
@Mod(modid = Client.CLIENT_NAME, version = Client.CLIENT_VERSION)
public class Client {
    public static final String CLIENT_NAME = "hyperpop";
    public static final String CLIENT_VERSION = "0.1";

    public ModuleManager moduleManager;
    public CommandManager commandManager;

    public SocialManager socialManager;
    public ForgeInteract forgeInteract;

    public PresetManager presetManager;

    public GUI userInterfaceGUI;
    public Theme guiTheme;

    public static TurokFont OVERLAY_FONT = new TurokFont(new Font("Arial", 0, 16), true, true);

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
        this.presetManager = new PresetManager();
        this.userInterfaceGUI = new GUI();
        this.guiTheme = new Theme();
    }

    public void registryClassesInEventListener() {
        MinecraftForge.EVENT_BUS.register(this.forgeInteract);
        EventBus.INSTANCE.register(this.userInterfaceGUI);
    }

    public void initClient() {
        this.moduleManager.preInitAll();
        this.commandManager.preInitAll();

        this.presetManager.preInitAll();

        final GUI userInterface = (GUI) this.userInterfaceGUI;
        userInterface.init();
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

    public static void save() {}
    public static void load() {}

    public static Color getCycleColor() {
        return INSTANCE.forgeInteract.getCycleColorRGB();
    }
}