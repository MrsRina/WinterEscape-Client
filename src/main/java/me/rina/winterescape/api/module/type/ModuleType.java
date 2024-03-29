package me.rina.winterescape.api.module.type;

/**
 * @author SrRina
 * @since 06/09/2021 at 18:51
 **/
public class ModuleType {
    public static final int COMBAT = 0;
    public static final int RENDER = 1;
    public static final int MISC = 2;
    public static final int MOVEMENT = 3;
    public static final int CLIENT = 4;
    public static final int HUD = 5;
    public static final int PLAYER = 6;
    public static final int SIZE = 7;

    public static String toString(int type) {
        if (type == COMBAT) {
            return "Combat";
        } else if (type == RENDER) {
            return "Visuals";
        } else if (type == MISC) {
            return "Misc";
        } else if (type == MOVEMENT) {
            return "Movement";
        } else if (type == CLIENT) {
            return "Client";
        } else if (type == HUD) {
            return "HUD";
        } else if (type == PLAYER) {
            return "Player";
        }

        return "null";
    }
}
