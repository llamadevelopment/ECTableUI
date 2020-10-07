package net.lldv.ectableui;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.plugin.PluginBase;
import net.lldv.ectableui.components.forms.FormListener;
import net.lldv.ectableui.components.language.Language;
import net.lldv.ectableui.components.listener.EventListener;

import java.util.*;

public class ECTableUI extends PluginBase {

    private static ECTableUI instance;
    public static HashMap<Integer, String> enchantmentNames = new HashMap<>();

    // TODO: Generate Enchantment, Error message when no Enchantments Available

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Language.init();
        getServer().getPluginManager().registerEvents(new FormListener(), this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        getConfig().getSection("enchantmentNames").getAllMap().forEach((id, obj) -> {
            if (obj instanceof String) {
                enchantmentNames.put(Integer.parseInt(id), (String) obj);
            }
        });

    }

    public List<Enchantment> getEnchantmentsByItem(Item item) {

        List<EnchantmentType> types = new ArrayList<>(Arrays.asList(EnchantmentType.ALL, EnchantmentType.BREAKABLE));

        if (item.isHelmet()) types.add(EnchantmentType.ARMOR_HEAD);
        if (item.isChestplate()) types.add(EnchantmentType.ARMOR_TORSO);
        if (item.isLeggings()) types.add(EnchantmentType.ARMOR_LEGS);
        if (item.isBoots()) types.add(EnchantmentType.ARMOR_FEET);
        if (item.isArmor()) types.add(EnchantmentType.ARMOR);
        if (item.isAxe() || item.isHoe() || item.isPickaxe() || item.isShovel()) types.add(EnchantmentType.DIGGER);
        if (item.isSword()) types.add(EnchantmentType.SWORD);
        if (item.getId() == ItemID.BOW) types.add(EnchantmentType.BOW);

        List<Enchantment> list = new ArrayList<>();

        if (types.size() > 2) {
            for (Enchantment ec : Enchantment.getEnchantments()) {
                if (types.contains(ec.type)) list.add(ec);
            }
        }

        return list;
    }

    public int getBookshelfs(Block table) {
        int count = 0;

        for (int y = 0; y <= 2; y++) {

            // top & bottom
            for (int z = 2; z >= -2; z--) {
                int top = table.getLevel().getBlockIdAt(table.getFloorX() - 2, table.getFloorY() + y, table.getFloorZ() + z);
                if (top == Block.BOOKSHELF) count++;
                int bottom = table.getLevel().getBlockIdAt(table.getFloorX() + 2, table.getFloorY() + y, table.getFloorZ() + z);
                if (bottom == Block.BOOKSHELF) count++;
            }

            // left & right
            for (int x = 1; x >= -1; x--) {
                int left = table.getLevel().getBlockIdAt(table.getFloorX() + x, table.getFloorY() + y, table.getFloorZ() -2);
                if (left == Block.BOOKSHELF) count++;
                int right = table.getLevel().getBlockIdAt(table.getFloorX() + x, table.getFloorY() + y, table.getFloorZ() + 2);
                if (right == Block.BOOKSHELF) count++;
            }

        }

        return count;
    }

    public static ECTableUI getInstance() {
        return instance;
    }
}
