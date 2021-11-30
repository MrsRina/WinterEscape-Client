package me.rina.zyra.impl.mixin.impl;

import event.bus.Event;
import event.bus.EventBus;
import me.rina.zyra.api.preset.management.PresetManager;
import me.rina.zyra.impl.event.RunTickEvent;
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

        EventBus.post(event);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void onShutdown(CallbackInfo ci) {
        PresetManager.task(PresetManager.TASK_DATA);
        PresetManager.task(PresetManager.TASK_SAVE);
    }
}
