package club.cpacket.solaros.impl.module.impl.combat;

import club.cpacket.solaros.Client;
import club.cpacket.solaros.api.module.Module;
import club.cpacket.solaros.api.module.type.ModuleType;
import club.cpacket.solaros.impl.event.RunTickEvent;
import event.bus.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 07/09/2021 at 15:45
 **/
public class ModuleKillAura extends Module {
    public ModuleKillAura() {
        super("KillAura", "Hit any entity around you!", ModuleType.COMBAT);

        // Dev thing don't worry.
        this.setKey(Keyboard.KEY_G);
    }

    private Entity entityIn;

    @EventListener
    public void onRunTick(RunTickEvent event) {
        if (this.nullable()) {
            return;
        }

        if (mc.player.getCooledAttackStrength(0) >= 1) {
            this.findForEntities();

            if (this.entityIn != null) {
                if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }

                mc.playerController.attackEntity(mc.player, this.entityIn);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    public void findForEntities() {
        Entity entity = null;

        for (Entity entities : mc.world.loadedEntityList) {
            if (mc.player.getDistance(entities) > 4.5f) {
                continue;
            }

            if (entities instanceof EntityMob) {
                entity = entities;

                break;
            }
        }

        if (entity != null) {
            this.entityIn = entity;
        }
    }
}
