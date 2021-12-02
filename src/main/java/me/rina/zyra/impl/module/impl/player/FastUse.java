package me.rina.zyra.impl.module.impl.player;

import me.rina.zyra.api.module.Module;
import me.rina.zyra.api.module.type.ModuleType;
import me.rina.zyra.api.value.type.CheckBox;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

public class FastUse extends Module
{
    /* Check box settings. */
    public static CheckBox xpSetting = new CheckBox("Experience", "Experience Bottles", true);
    public static CheckBox crystalSetting = new CheckBox("Crystals", "End Crystals", true);

    public FastUse() {
        super("FastUse", "Fast Use", ModuleType.PLAYER);
    }

    public void onUpdate()
    {
        if (xpSetting.getValue())
        {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle)
            {
                mc.rightClickDelayTimer = 0;
            }
        }

        if (crystalSetting.getValue())
        {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal)
            {
                mc.rightClickDelayTimer = 0;
            }
        }
    }


}
