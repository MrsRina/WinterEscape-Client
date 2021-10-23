package me.rina.hyperpop.impl.forge;

import event.bus.EventBus;
import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.command.Command;
import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

/**
 * @author SrRina
 * @since 07/09/2021 at 12:21
 **/
public class ForgeInteract extends Feature {
    private Color cycleColorRGB = Color.WHITE;

    public ForgeInteract() {
        super("Interact", "Interact with all events in client.");
    }

    public Color getCycleColorRGB() {
        return cycleColorRGB;
    }

    public void setCycleColorRGB(int hex) {
        this.cycleColorRGB = new Color((hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF, 255);
    }

    @SubscribeEvent
    public void onWorldRenderEvent(RenderWorldLastEvent event) {
        Client.INSTANCE.moduleManager.onWorldRender(event.getPartialTicks());

        this.setCycleColorRGB(Color.HSBtoRGB((System.currentTimeMillis() % (360 * 32)) / (360f * 32f), 1, 1));
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        EventBus.post(new ClientTickEvent());

        Client.INSTANCE.moduleManager.onSetting();
    }

    @SubscribeEvent
    public void onOverlayRenderEvent(RenderGameOverlayEvent event) {
        if (mc.player == null) {
            return;
        }

        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.ALL;

        if (!mc.player.isCreative() && mc.player.getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }

        if (event.getType() != target) {
            return;
        }

        Client.INSTANCE.moduleManager.onOverlayRender(event.getPartialTicks());
    }

    @SubscribeEvent(receiveCanceled = true)
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
