package de.freemine.holocountdown.command;

import de.freemine.holocountdown.Main;
import de.freemine.holocountdown.timer.countdown;
import de.freemine.holocountdown.util.utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author martin
 */
public class command_holocountdown implements CommandExecutor {
    private Main main;

    public command_holocountdown(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, final Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("HoloCountdown")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("holocountdown.use") || player.isOp()) {
                    if (args.length == 0) {
                        player.sendMessage(utils.header("HoloCountdown"));
                        player.sendMessage(utils.getPRIMARY() + "TIPP: hover the command to get additional Information!");

                        TextComponent create = new TextComponent("/hc create");
                        create.setColor(ChatColor.GREEN);
                        create.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder("§bPossible Possibilities are: \n" +
                                        "§9/hc create <#>s      §2This will create a countdown with the given number as Seconds\n" +
                                        "§9/hc create <#>m     §2This will create a countdown with the given number as Minutes\n" +
                                        "§9/hc create <#>h     §2This will create a countdown with the given number as Hours\n" +
                                        "§9/hc create <#>d     §2This will create a countdown with the given number as Days\n" +
                                        "§9/hc create <#>w     §2This will create a countdown with the given number as Weeks\n").create()));
                        player.spigot().sendMessage(create);

                        TextComponent movehere = new TextComponent("/hc <#> movehere");
                        movehere.setColor(ChatColor.GREEN);
                        movehere.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder("§9/hc <#> movehere    §aThis command will move the Hologram with the specified ID to your current Location").create()));
                        player.spigot().sendMessage(movehere);

                        TextComponent list = new TextComponent("/hc list");
                        list.setColor(ChatColor.GREEN);
                        list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bGives you a list of all Holograms with their ID").create()));
                        player.spigot().sendMessage(list);
                        player.sendMessage(" ");

                        TextComponent credits = new TextComponent("Click Me");
                        credits.setColor(ChatColor.GOLD);
                        credits.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClick this to open the Developers Website!").create()));
                        credits.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://freemine.de/"));
                        player.spigot().sendMessage(credits);

                        player.sendMessage(utils.footer());
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(utils.header("Holograms"));
                            if (!main.getHolograms().isEmpty()) {
                                int i = 0;
                                while (i < main.getHolograms().size()) {
                                    Boolean status = main.getActive().get(main.getHolograms().get(i));
                                    player.sendMessage("ID: " + i + " located at " + main.getHolograms().get(i).getLocation().getBlockX() +
                                            "," + main.getHolograms().get(i).getLocation().getBlockY() +
                                            "," + main.getHolograms().get(i).getLocation().getBlockZ() + " Status: " + status);
                                    i++;
                                }
                            } else {
                                player.sendMessage(utils.getError() + "You haven't set any Holograms right now");
                            }
                        } else {
                            player.sendMessage(utils.getWarningColor() + "This subcommand does not exist. Try /hc to get an list of all possible commands");
                        }
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("create")) {
                            String s = args[1].substring(0, args[1].length() -1);
                            int time = Integer.parseInt(s);
                            char unit = args[1].charAt(args[1].length() -1);
                            try {
                                if (calculateUnits(time, unit) != null) {
                                    //TODO
                                    int counting = 10;
                                    main.getServer().getScheduler().runTask(main.plugin, new countdown(main, counting, player.getLocation()));

                                }
                            } catch (Exception e) {
                                player.sendMessage(utils.getError() + "cannot create a hologram. This is coursed by:\n" +
                                        " - using no Timeunit (eg. /hc create 10)\n" +
                                        " - using a not registered unit. possible units are s(econds), m(inutes), h(ours), d(ays), w(eeks)!");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(utils.getError() + "This is just a Player command!");
            }
        }
        return false;
    }

    /**
     * Calculates the Time in Seconds
     *
     * @param i an Integer
     * @param c an Unit
     * @return Seconds
     */
    private Integer calculateUnits(int i, char c) {
        if (String.valueOf(c).equalsIgnoreCase("s")) {
            return (i);
        } else if (String.valueOf(c).equalsIgnoreCase("m")) {
            return (i * 60);
        } else if (String.valueOf(c).equalsIgnoreCase("h")) {
            return (i * 60 * 60);
        } else if (String.valueOf(c).equalsIgnoreCase("d")) {
            return (i * 24 * 60 * 60);
        } else if (String.valueOf(c).equalsIgnoreCase("w")) {
            return (i * 7 * 24 * 60 * 60);
        }
        return null;
    }
}
