package com.somemone.dynamiceeconomy.command.de;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import com.somemone.dynamiceeconomy.event.TransactionEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;

public class BuySubcommand extends SubCommand {


    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (args.length > 4 || args.length < 2) {
            player.sendMessage(ChatColor.RED + "/de buy <item> (amount)");
            return;
        }

        Material material = Material.getMaterial(args[1].toUpperCase());
        if (material == null) {
            player.sendMessage(ChatColor.RED + "Cannot resolve item name. Use item namespace (\"oak_log\")");
            return;
        }

        int amount = 1;
        if (args.length == 3) {
            amount = Integer.parseInt(args[2]);
        }

        StoresConfig instance = new StoresConfig();
        if (!instance.storeExists(material.name())) {
            player.sendMessage(ChatColor.RED + "There isn't a market for this item.");
            return;
        }
        ItemStore store = instance.getStore(material.name());

        float totalCost = store.getPrice() * amount;
        if (!DynamicEeconomy.getEcon().has((OfflinePlayer) player, totalCost)) {
            player.sendMessage(ChatColor.RED + "Insufficient funds in player account.");
            return;
        }

        //Complete transaction
        ItemStack toGive = new ItemStack(material, amount);
        DynamicEeconomy.getEcon().withdrawPlayer((OfflinePlayer) player, totalCost);
        player.getInventory().addItem(toGive);

        Transaction transaction = new Transaction(material.name(), amount, totalCost, ItemStore.APSType.BUY, Seller.getServerSeller(), LocalDateTime.now());
        TransactionHandler.writeTransaction(transaction);

        TransactionEvent event = new TransactionEvent(transaction, player.getUniqueId());
        Bukkit.getPluginManager().callEvent(event);

        player.sendMessage(ChatColor.GREEN + "Transaction completed. Total cost: $" + totalCost );

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.buy";
    }
}
