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
    public static CheckBox settingXP = new CheckBox("Experience", "Experience Bottles", true);
    public static CheckBox settingCrystal = new CheckBox("Crystals", "End Crystals", true);
    public static CheckBox settingBlocks = new CheckBox("Blocks", "Blocks", true);

    public ModuleFastUse() {
        super("FastUse", "Allows you to use certain things with no delay.", ModuleType.PLAYER);
    }

    public void onUpdate()
    {
        if (settingXP.getValue())
        {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle)
            {
                mc.rightClickDelayTimer = 0;
            }
        }

        if (settingCrystal.getValue())
        {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal)
            {
                mc.rightClickDelayTimer = 0;
            }
        }

        if (settingBlocks.getValue())
        {
            if (Block.getBlockFromItem(mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock()) {
                mc.rightClickDelayTimer = 0;
            }
        }
    }
}
