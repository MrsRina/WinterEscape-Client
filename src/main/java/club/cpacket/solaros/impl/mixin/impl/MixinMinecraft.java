package club.cpacket.solaros.impl.mixin.impl;

import club.cpacket.solaros.Client;
import club.cpacket.solaros.api.event.Event;
import club.cpacket.solaros.impl.event.RunTickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 07/09/2021 at 15:49
 **/
@Mixin(value = Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "runTick", at = @At("HEAD"))
    public void onRunTick(CallbackInfo ci) {
        final Event event = new RunTickEvent();

        event.setPre();

        Client.EVENT_BUS.post(event);
    }
}
