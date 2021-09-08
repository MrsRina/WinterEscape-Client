package club.cpacket.solaros.impl.command.impl;

import club.cpacket.solaros.Client;
import club.cpacket.solaros.api.command.Command;
import club.cpacket.solaros.api.social.Social;
import club.cpacket.solaros.api.social.management.SocialManager;
import club.cpacket.solaros.api.social.type.SocialType;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * @author SrRina
 * @since 08/09/2021 at 15:22
 **/
public class CommandSocial extends Command {
    public CommandSocial() {
        super(new String[] {"friends", "friend"}, "Add player on list.");
    }

    @Override
    public String syntax() {
        return "friend <add/new/set>/<remove/delete/del/rem/unset> <name> | friends";
    }

    @Override
    public void onCommand(String[] args) {
        if (mc.getConnection() == null) {
            Client.log(ChatFormatting.RED + "Connection is offline!");

            return;
        }

        String task = null;
        String tag = null;

        if (args.length > 1) {
            task = args[1];
        }

        if (args.length > 2) {
            tag = args[2];
        }

        if (args.length > 3) {
            splash();

            return;
        }

        if (args[0].equalsIgnoreCase("friends")) {
            if (tag == null && task == null) {
                final StringBuilder builder = new StringBuilder();

                for (Social socials : Client.INSTANCE.socialManager.getSocialList()) {
                    final String name = socials.getTag() + "; ";

                    builder.append(name);
                }

                Client.log("Friends: ");
                Client.log(builder.toString());
            } else {
                splash();
            }

            return;
        }

        if (task == null) {
            splash();

            return;
        }

        if (tag == null) {
            splash();

            return;
        }

        if (task.equalsIgnoreCase("add") || tag.equalsIgnoreCase("new") || tag.equalsIgnoreCase("set")) {
            final NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(tag);

            if (info == null) {
                Client.log("Player '" + tag + "' is not online.");
            } else {
                SocialManager.add(tag, SocialType.FRIEND);

                Client.log("Added " + tag + " in social list.");
            }

            return;
        }

        if (task.equalsIgnoreCase("remove") || task.equalsIgnoreCase("delete") || task.equalsIgnoreCase("del") || task.equalsIgnoreCase("rem") || task.equalsIgnoreCase("unset")) {
            final Social social = SocialManager.get(tag);

            if (social == null) {
                Client.log("There is no '" + tag + "' in friend list.");
            } else {
                SocialManager.remove(tag);

                Client.log("Removed " + social.getTag() + " from your friend list.");
            }

            return;
        }

        splash();
    }
}
