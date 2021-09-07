package club.cpacket.solaros.impl.forge;

import club.cpacket.solaros.Client;
import club.cpacket.solaros.api.command.Command;
import club.cpacket.solaros.api.feature.Feature;
import com.mojang.realmsclient.gui.ChatFormatting;
import event.bus.EventListener;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 07/09/2021 at 12:21
 **/
public class ForgeInteract extends Feature {
    public ForgeInteract() {
        super("Interact", "Interact with all events in client.");
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            Client.INSTANCE.moduleManager.onKeyboard(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        final String message = event.getMessage();
        final String prefix = Client.INSTANCE.commandManager.getPrefix();

        if (message.startsWith(prefix)) {
            final String replace = message.replaceFirst(prefix, "");
            final String[] args = replace.split(" ");

            mc.ingameGUI.getChatGUI().addToSentMessages(message);

            boolean flag = args.length > 0;
            Command command = null;

            if (flag) {
                command = Client.INSTANCE.commandManager.getCommand(args[0]);
            }

            if (command == null) {
                Client.log(ChatFormatting.RED + "Unknown command.");
            } else {
                command.onCommand(args);
            }

            event.setCanceled(true);
        }
    }
}
