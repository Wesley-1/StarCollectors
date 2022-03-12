package enchants.impl;

import enchants.api.enchant.AtlasEnchant;
import enchants.impl.enchants.speed.SpeedData;
import enchants.impl.enchants.speed.SpeedEnchant;
import enchants.impl.service.EnchantDataService;
import enchants.impl.user.EnchantUser;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Test implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        SpeedEnchant enchant = new SpeedEnchant();
        if (event.getBlock().getType().equals(Material.GOLD_BLOCK)) {
            if (!EnchantDataService.loadedUsers.containsKey(event.getPlayer().getUniqueId())) {
                EnchantUser user = new EnchantUser(event.getPlayer().getUniqueId());
                user.getStoredData().put(enchant, 1);
            } else if (EnchantDataService.loadedUsers.get(event.getPlayer().getUniqueId()).getStoredData().containsKey(enchant)) {
                event.getPlayer().sendMessage("Speed: " + EnchantDataService.loadedUsers.get(event.getPlayer().getUniqueId()).getStoredData().get(enchant));
                EnchantDataService.loadedUsers.get(event.getPlayer().getUniqueId()).getStoredData().replace(enchant, EnchantDataService.loadedUsers.get(event.getPlayer().getUniqueId()).getStoredData().get(enchant),
                        EnchantDataService.loadedUsers.get(event.getPlayer().getUniqueId()).getStoredData().get(enchant) + 1);
            }
        }
    }
}
