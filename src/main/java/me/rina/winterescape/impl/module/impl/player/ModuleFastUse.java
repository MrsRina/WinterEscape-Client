package me.rina.winterescape.impl.module.impl.player;

import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.api.value.type.CheckBox;
import net.minecraft.block.Block;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

public class ModuleFastUse extends Module
{
    /* Check box settings. */
    public static CheckBox xpSetting = new CheckBox("Experience", "Experience Bottles", true);
    public static CheckBox crystalSetting = new CheckBox("Crystals", "End Crystals", true);
    public static CheckBox blocksSetting = new CheckBox("Blocks", "Blocks", true);

    public ModuleFastUse() {
        super("ModuleFastUse", "Fast Use", ModuleType.PLAYER);
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

        if (blocksSetting.getValue())
        {
            if (Block.getBlockFromItem(mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock()) {
                mc.rightClickDelayTimer = 0;
            }
        }
    }


}
