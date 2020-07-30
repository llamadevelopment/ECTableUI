package net.lldv.ectableui.components.forms;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import net.lldv.ectableui.ECTableUI;
import net.lldv.ectableui.components.forms.simple.SimpleForm;
import net.lldv.ectableui.components.language.Language;

import java.util.List;
import java.util.Random;

public class FormWindows {

    public static void sendSelectForm(Player player, Block table) {
        Random r = new Random();

        Item item = player.getInventory().getItemInHand();

        SimpleForm.Builder form = new SimpleForm.Builder(Language.getNP("title"), Language.getNP("content"));

        List<Enchantment> ecs = ECTableUI.getInstance().getEnchantmentsByItem(item);

        if (ecs.size() == 0) {
            player.sendMessage(Language.get("no.enchantments"));
            return;
        }

        int bs = ECTableUI.getInstance().getBookshelfs(table);
        if (bs == 0) bs = 1;
        if (bs > 15) bs = 15;

        int base = (r.nextInt(7) + 1) + (bs / 2) + (r.nextInt(bs)); // (1..8 + floor(b / 2) + 0..b),

        Enchantment ec = ecs.get(r.nextInt(ecs.size() - 1));
        Enchantment ec2 = ecs.get(r.nextInt(ecs.size() - 1));
        Enchantment ec3 = ecs.get(r.nextInt(ecs.size() - 1));

        int firstLevels = Math.max(base / 3, 1);
        int secondLevels = (base * 2) / 3 + 1;
        int thirdLevels = Math.max(base, bs * 2);

        int firstLevel = generateEnchantmentLevel(ec, 1, bs);
        int secondLevel = generateEnchantmentLevel(ec2, 1, bs);
        int thirdLevel = generateEnchantmentLevel(ec3, 2, bs);

        form.addButton(new ElementButton(Language.getNP("button", ECTableUI.enchantmentNames.get(ec.getId()), formatLevel(firstLevel), firstLevels)), (p) -> {
            tryEnchant(player, item, ec, firstLevel, firstLevels);
        });

        form.addButton(new ElementButton(Language.getNP("button", ECTableUI.enchantmentNames.get(ec2.getId()), formatLevel(secondLevel), secondLevels)), (p) -> {
            tryEnchant(player, item, ec2, secondLevel, secondLevels);
        });

        form.addButton(new ElementButton(Language.getNP("button", ECTableUI.enchantmentNames.get(ec3.getId()), formatLevel(thirdLevel), thirdLevels)), (p) -> {
            tryEnchant(player, item, ec3, thirdLevel, thirdLevels);
        });


        form.build().send(player);
    }

    private static void tryEnchant(Player player, Item originItem, Enchantment ec, int level, int cost) {
        Item item = player.getInventory().getItemInHand();

        ec.setLevel(level);

        if (originItem.getId() == item.getId()) {

            if (player.getExperienceLevel() > cost) {
                for (Enchantment check : item.getEnchantments()) {
                    if (check.getId() == ec.getId()) {
                        player.sendMessage(Language.get("already.enchanted.this"));
                        return;
                    }
                }

                item.addEnchantment(ec);
                player.getInventory().setItemInHand(item);
                player.setExperience(player.getExperience(), player.getExperienceLevel() - cost);
                player.sendMessage(Language.get("enchanted", ECTableUI.enchantmentNames.get(ec.getId()), formatLevel(ec.getLevel())));
            } else player.sendMessage(Language.get("not.enough.levels"));
        }
    }

    private static int generateEnchantmentLevel(Enchantment ec, int slotBonus, int books) {

        /*
        * Well, i'm pretty sure there is a better way to do this
        * I've translated that from my old plugin EC-TableUI.
        * If you have any idea how to improve this, feel free
        * to fork this project and make a pull request :)
        *
        */

        Random r = new Random();

        double levelSub = 0.20;

        if (books < 3) slotBonus = 1;

        if(books > 5) levelSub = 0.40;
        if(books > 10) levelSub = 0.70;
        if(books > 15) levelSub = 1;

        int maxLevel = (int) Math.round(ec.getMaxEnchantableLevel() * levelSub);
        if (maxLevel == 0) maxLevel = 1;

        if (ec.getMaxLevel() < slotBonus) slotBonus = ec.getMaxLevel();

        return r.nextInt(maxLevel) + slotBonus;
    }

    public static String formatLevel(int level) {
        switch (level) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return "" + level;
        }
    }

}
