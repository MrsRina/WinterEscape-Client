package me.rina.zyra.api.feature;

import net.minecraft.client.Minecraft;

/**
 * @author SrRina
 * @since 06/09/2021 at 18:43
 **/
public class Feature {
    public final Minecraft mc = Minecraft.getMinecraft();

    private String tag;
    private String description;

    public Feature(final String tag, final String description) {
        this.tag = tag;
        this.description = description;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
