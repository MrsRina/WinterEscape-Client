package me.rina.hyperpop.impl.module.impl.combat;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.social.management.SocialManager;
import me.rina.hyperpop.api.social.type.SocialType;
import me.rina.hyperpop.impl.event.RunTickEvent;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Slider;
import event.bus.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 07/09/2021 at 15:45
 **/
public class ModuleKillAura extends Module {
    /* Check box settings. */
    public static CheckBox settingPlayer = new CheckBox("Player", "Attack players.", true);
    public static CheckBox settingMobs = new CheckBox("Mobs", "Attack mobs.", false);
    public static CheckBox settingAnimals = new CheckBox("Animals", "Attack animals.", false);
    public static CheckBox settingOnlySwordAxe = new CheckBox("OnlySword&Axe", "Only sword and axe.", true);
    public static CheckBox settingOffhandUse = new CheckBox("OffhandUse", "Use offhand while attacks.", true);
    public static CheckBox settingCancelFriendHit = new CheckBox("CancelFriendHit", "Cancel all friend hits.", true);
    public static CheckBox settingPacket = new CheckBox("Packet", "Send packet only.", false);
    public static CheckBox settingSwing = new CheckBox("Swing", "Render swing.", false);

    /* Slider setting. */
    public static Slider settingRange = new Slider("Range", "Range for attack.", 4.5f, 2f, 6f);

    public ModuleKillAura() {
        super("KillAura", "Hit any entity around you!", ModuleType.COMBAT);

        // Dev thing don't worry.
        this.setKey(Keyboard.KEY_G);
    }

    private Entity entityIn;
    private EnumHand handIn;

    @EventListener
    public void onRunTick(RunTickEvent event) {
        if (this.nullable()) {
            return;
        }

        if (mc.player.getCooledAttackStrength(0) >= 1) {
            this.findForEntities();
            this.verifyBestHandHoldingWeapon();

            if (this.entityIn != null && this.handIn != null) {
                if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && settingOffhandUse.getValue()) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }

                if (settingPacket.getValue()) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(this.entityIn));
                    mc.player.resetCooldown();
                } else {
                    mc.playerController.attackEntity(mc.player, this.entityIn);
                }

                if (settingSwing.getValue()) {
                    mc.player.swingArm(this.handIn);
                } else {
                    mc.player.connection.sendPacket(new CPacketAnimation(this.handIn));
                }
            }
        }
    }

    public void findForEntities() {
        Entity entity = null;

        for (Entity entities : mc.world.loadedEntityList) {
            if (mc.player.getDistance(entities) > settingRange.getValue().floatValue()) {
                continue;
            }

            if (entities instanceof EntityPlayer && entities != mc.player && ((SocialManager.get(entities.getName()) == null || SocialManager.get(entities.getName()).getType() != SocialType.FRIEND) || !settingCancelFriendHit.getValue()) && settingPlayer.getValue()) {
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

        this.entityIn = entity;
    }

    public void verifyBestHandHoldingWeapon() {
        EnumHand hand = EnumHand.MAIN_HAND;

        if (settingOnlySwordAxe.getValue()) {
            boolean flag = mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe;

            hand = flag ? EnumHand.MAIN_HAND : null;
        }

        this.handIn = hand;
    }
}