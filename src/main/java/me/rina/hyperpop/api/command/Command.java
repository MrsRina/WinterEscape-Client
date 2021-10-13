package me.rina.hyperpop.api.command;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.feature.Feature;

/**
 * @author SrRina
 * @since 06/09/2021 at 21:31
 **/
public class Command extends Feature {
    private String[] alias;

    public Command(String[] alias, String description) {
        super("command", description);

        this.alias = alias;
    }

    public String syntax() {
        return null;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public void splash() {
        Client.log("Unknown syntax, try '" + this.syntax() + "'");
    }

    public boolean contains(String string) {
        boolean contains = false;

        for (String strings : this.getAlias()) {
            if (strings.equalsIgnoreCase(string)) {
                contains = true;

                break;
            }
        }

        return contains;
    }

    public void onCommand(String[] args) {}
}
