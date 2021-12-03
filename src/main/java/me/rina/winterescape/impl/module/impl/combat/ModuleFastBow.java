package me.rina.winterescape.impl.module.impl.combat;

import event.bus.EventListener;
import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.impl.event.RunTickEvent;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

/**
 * @author SrRina
 * @since 20/11/2021 at 02:02
 **/
public class ModuleFastBow extends Module {
    public ModuleFastBow() {
        super("FastBow", "Fast bow module.", ModuleType.COMBAT);
    }

    @EventListener
    public void onRunTick(RunTickEvent event) {
        if (this.nullable()) {
            return;
        }

        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));

            mc.player.stopActiveHand();
        }
    }
}
