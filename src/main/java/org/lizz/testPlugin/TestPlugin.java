 package org.lizz.testPlugin;
 import net.kyori.adventure.text.Component;
 import net.kyori.adventure.text.format.NamedTextColor;
 import org.bukkit.Material;
import org.bukkit.Bukkit;
 import org.bukkit.attribute.Attribute;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.entity.PlayerDeathEvent;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
 import org.bukkit.plugin.java.JavaPlugin;

 public class TestPlugin extends JavaPlugin implements Listener {


     @Override
     public void onEnable() {
         Bukkit.getPluginManager().registerEvents(this, this);
     }
     @EventHandler
     public void onPlayerDeath(PlayerDeathEvent event) {
         Player player = event.getEntity();
         double currentMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
         // lose hp
             if (currentMaxHealth > 2) {
             player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth - 2);
             //sspawn on dead
             ItemStack heartItem = createHeartItem();
             event.getDrops().add(heartItem); // Adds the Heart Fragment to the death drops
             player.sendMessage(Component.text("You lost 1 heart permanently!").color(net.kyori.adventure.text.format.NamedTextColor.RED));
             } else {
             player.sendMessage(Component.text("You can't lose more hearts!").color(net.kyori.adventure.text.format.NamedTextColor.RED));
         }
     }

          @EventHandler
          public void onPlayerRightClick(PlayerInteractEvent event) {
         ItemStack heldItem = event.getItem();  // Rename to heldItem for clarity
         //check 1
         if (heldItem == null) return;
         Player player = event.getPlayer();
         //check 2
         if (isHeartItem(heldItem)) {
             double currentMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
             // incrrrase hp
             player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth + 2);
             player.sendMessage(Component.text("You gained 1 permanent heart!").color(net.kyori.adventure.text.format.NamedTextColor.GREEN));
             //use item
             heldItem.setAmount(heldItem.getAmount() - 1);
         }
         }
     //anti stack
         @EventHandler
         public void onInventoryClick(InventoryClickEvent event) {
         ItemStack currentItem = event.getCurrentItem();
         ItemStack cursorItem = event.getCursor();
         if (isHeartItem(currentItem) && isHeartItem(cursorItem)) {
             event.setCancelled(true);  //canecl event
         }
     }
     //stack
     private ItemStack createHeartItem() {
         ItemStack heart = new ItemStack(Material.NETHER_STAR, 1); //sstack ammount
         ItemMeta meta = heart.getItemMeta();
         if (meta != null) {
             meta.displayName(Component.text("Heart Fragment").color(net.kyori.adventure.text.format.NamedTextColor.RED)); // Paper way to set display name
             heart.setItemMeta(meta);
         }
         return heart;
     }
     //check special item
     private boolean isHeartItem(ItemStack item) {
         if (item == null || item.getType() != Material.NETHER_STAR) return false;
         ItemMeta meta = item.getItemMeta();
         return meta != null && Component.text("Heart Fragment").color(net.kyori.adventure.text.format.NamedTextColor.RED).equals(meta.displayName());
     }
 }