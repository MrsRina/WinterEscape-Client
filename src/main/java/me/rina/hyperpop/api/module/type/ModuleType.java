package me.rina.hyperpop.api.module.type;

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
    public static final int SIZE = 5;

    public static String toString(int type) {
        if (type == COMBAT) {
            return "Combat";
        } else if (type == RENDER) {
            return "Combat";
        } else if (type == MISC) {
            return "Misc";
        } else if (type == MOVEMENT) {
            return "Movement";
        } else if (type == CLIENT) {
            return "Client;";
        }

        return "null";
    }
}
