package me.rina.hyperpop.api.social.management;

import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.api.social.Social;
import me.rina.hyperpop.api.social.type.SocialType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 08/09/2021 at 15:05
 **/
public class SocialManager extends Feature {
    public static SocialManager INSTANCE;

    private final List<Social> socialList = new ArrayList<>();

    public SocialManager() {
        super("Social", "Social things in client.");

        INSTANCE = this;
    }

    public List<Social> getSocialList() {
        return socialList;
    }

    public static void add(String name, SocialType type) {
        Social social = get(name);

        if (social != null) {
            social.setType(type);
        } else {
            social = new Social(name, type);

            INSTANCE.socialList.add(social);
        }
    }

    public static void remove(String name) {
        Social social = get(name);

        if (social != null) {
            INSTANCE.socialList.remove(social);
        }
    }

    public static Social get(String name) {
        Social social = null;

        for (Social socials : INSTANCE.getSocialList()) {
            if (socials.getTag().equalsIgnoreCase(name)) {
                social = socials;

                break;
            }
        }

        return social;
    }
}
