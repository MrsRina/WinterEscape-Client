package me.rina.winterescape.api.social;

import me.rina.winterescape.api.feature.Feature;
import me.rina.winterescape.api.social.type.SocialType;

/**
 * @author SrRina
 * @since 08/09/2021 at 15:02
 **/
public class Social extends Feature {
    private SocialType type;

    public Social(String name, SocialType type) {
        super(name, "nigger");

        this.setType(type);
    }

    public void setType(SocialType type) {
        this.type = type;
    }

    public SocialType getType() {
        return type;
    }
}
