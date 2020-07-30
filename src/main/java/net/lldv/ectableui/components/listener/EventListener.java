package net.lldv.ectableui.components.listener;

import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import net.lldv.ectableui.components.forms.FormWindows;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.getBlock().getId() == BlockID.ENCHANTMENT_TABLE) {

                FormWindows.sendSelectForm(event.getPlayer(), event.getBlock());
                event.setCancelled(true);
            }
        }
    }

}
