package club.cpacket.solaros.impl.command.impl;

import club.cpacket.solaros.api.command.Command;

/**
 * @author SrRina
 * @since 08/09/2021 at 15:22
 **/
public class CommandSocial extends Command {
    public CommandSocial() {
        super(new String[] {"friend", "enemy"}, "Add player on list.");
    }

    @Override
    public String syntax() {
        return "friend/enemy <name>";
    }

    @Override
    public void onCommand(String[] args) {
        String type = null;
        String tag = null;
    }
}
