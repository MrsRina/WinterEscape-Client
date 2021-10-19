package me.rina.hyperpop.impl.module.impl.combat;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.event.RunTickEvent;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Slider;
import me.rina.hyperpop.social.management.SocialManager;
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
    // Misc settings.
    public static CheckBox settingPlayer = registry(new CheckBox("Player", "Attack players.", true));
    public static CheckBox settingMobs = registry(new CheckBox("Mobs", "Attack mobs.", false));
    public static CheckBox settingAnimals = registry(new CheckBox("Animals", "Attack animals.", false));
    public static CheckBox settingOnlySwordAxe = registry(new CheckBox("OnlySword&Axe", "Only sword and axe.", true));
    public static CheckBox settingOffhandUse = registry(new CheckBox("OffhandUse", "Use offhand while attacks.", true));
    public static CheckBox settingCancelFriendHit = registry(new CheckBox("CancelFriendHit", "Cancel all friend hits.", true));
    public static Slider settingRange = registry(new Slider("Range", "Range for attack.", 4.5f, 2f, 6f)); 

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
                if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && settingOffhandUse.getValue()) {
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
            if (mc.player.getDistance(entities) > settingRange.getValue().floatValue()) {
                continue;
            }

            if (entities instanceof EntityPlayer && entities != mc.player && (SocialManager.isFriend(entities.getName()) || !settingCancelFriendHit.getValue()) && settingPlayer.getValue()) {
                entity = entities;

                break;
            }

            if (entities instanceof EntityMob && settingMobs.getValue()) {
                entity = entities;

                break;
            }

            if (entities instanceof EntityAnimal && settingAnimals.getValue()) {
                entity = entities;

                break;
            }
        }

        if (entity != null) {
            this.entityIn = entity;
        }
    }
}
