package cn.stars.addons.creativetab;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.util.player.ItemUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

public final class ReversalTab extends CreativeTabs implements GameInstance {

    public ReversalTab() {
        super(12, "reversal");
    }

    @Override
    public void displayAllReleventItems(final List<ItemStack> items) {
        // Hologram
        final ItemStack hologram = new ItemStack(Items.armor_stand);
        final NBTTagCompound baseCompound = new NBTTagCompound();
        final NBTTagList posList = new NBTTagList();
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posX));
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posY));
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
        baseCompound.setBoolean("Invisible", true);
        baseCompound.setBoolean("NoGravity", true);
        baseCompound.setBoolean("CustomNameVisible", true);
        baseCompound.setString("CustomName", "Reversal Client");
        baseCompound.setTag("Pos", posList);
        baseCompound.setTag("Pose", posList);
        hologram.setTagInfo("EntityTag", baseCompound);
        hologram.setStackDisplayName("\247r浮空字");
        items.add(hologram);

        // Hologram (Via Version)
        final ItemStack hologramVia = new ItemStack(Items.armor_stand);
        final NBTTagCompound baseCompound1 = new NBTTagCompound();
        final NBTTagList posList1 = new NBTTagList();
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posX));
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posY));
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
        baseCompound1.setBoolean("Invisible", true);
        baseCompound1.setBoolean("NoGravity", true);
        baseCompound1.setBoolean("CustomNameVisible", true);
        baseCompound1.setString("CustomName", "\"" + "Reversal Client" + "\"");
        baseCompound1.setTag("Pos", posList1);
        hologramVia.setTagInfo("EntityTag", baseCompound1);
        hologramVia.setStackDisplayName("\247r浮空字 (Via Version)");
        items.add(hologramVia);

        // Stars' Head
        items.add(ItemUtil.getCustomSkull("Stars", "http://textures.minecraft.net/texture/7e6e1c235a9fdd053691e133ef385525aacc3acf510805c6e747ec0359cb59ff"));

        // Dream's Head
        items.add(ItemUtil.getCustomSkull("Dream", "http://textures.minecraft.net/texture/ca93f6fc40488f1877cda94a830b54e9f6f54ab58a5453bad5c947726dd1f473"));

        // Spawn Imposter
        final ItemStack crashAnvil = ItemUtil.getItemStack("anvil 1 100");
        crashAnvil.setStackDisplayName("\247r崩溃铁砧");
        items.add(crashAnvil);

        // Splash Potion of Instant Death
        final ItemStack deathPotion = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:6,Amplifier:125,Duration:1000000}]}");
        deathPotion.setStackDisplayName("\247r喷溅型 秒杀药水");
        items.add(deathPotion);

        // Dragon Egg
        items.add(ItemUtil.getItemStack("dragon_egg"));

        // Barrier
        items.add(ItemUtil.getItemStack("barrier"));

        // Command Block
        items.add(ItemUtil.getItemStack("command_block"));

        // Command Block Minecart
        items.add(ItemUtil.getItemStack("command_block_minecart"));

        // Alpha Slab
        final ItemStack alphaSlab = ItemUtil.getItemStack("stone_slab 1 2");
        alphaSlab.setStackDisplayName("\247rAlpha版台阶");
        items.add(alphaSlab);

        // Alpha Leaves
        final ItemStack alphaLeaves = ItemUtil.getItemStack("leaves 1 4");
        alphaLeaves.setStackDisplayName("\247rAlpha版树叶");
        items.add(alphaLeaves);

        // Shrub
        final ItemStack tallGrass = ItemUtil.getItemStack("tallgrass 1 0");
        tallGrass.setStackDisplayName("\247r艹");
        items.add(tallGrass);

        // Splash Potion of Annoyance
        final ItemStack annoyancePotion = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:15,Amplifier:2,Duration:1000000},{Id:2,Amplifier:2,Duration:1000000},{Id:9,Amplifier:2,Duration:1000000},{Id:19,Amplifier:2,Duration:1000000},{Id:20,Amplifier:2,Duration:1000000},{Id:18,Amplifier:2,Duration:1000000},{Id:17,Amplifier:2,Duration:1000000},{Id:14,Amplifier:2,Duration:1000000},{Id:4,Amplifier:2,Duration:1000000}]}");
        annoyancePotion.setStackDisplayName("\247r喷溅型 恼人药水");
        items.add(annoyancePotion);

        // Splash Potion of Infinite Invisibility
        final ItemStack infiniteInvisibility = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:14,Duration:1000000,ShowParticles:0b}]}");
        infiniteInvisibility.setStackDisplayName("\247r喷溅型 无限隐身药水");
        items.add(infiniteInvisibility);

        // God Sword
        final ItemStack godSword = ItemUtil.getItemStack("diamond_sword 1 0 {ench:[{id:19,lvl:32767},{id:20,lvl:32767},{id:18,lvl:32767},{id:16,lvl:32767},{id:17,lvl:32767}],Unbreakable:1}");
        godSword.setStackDisplayName("\247r\247b\247l神之剑");
        items.add(godSword);

        // God Bow
        final ItemStack godBow = ItemUtil.getItemStack("bow 1 0 {ench:[{id:48,lvl:32767},{id:49,lvl:32767},{id:50,lvl:32767},{id:51,lvl:32767},{id:19,lvl:32767}],Unbreakable:1}");
        godBow.setStackDisplayName("\247r\247b\247l神之弓");
        items.add(godBow);

        // God Helmet
        final ItemStack godHelmet = ItemUtil.getItemStack("diamond_helmet 1 0 {ench:[{id:0,lvl:32767},{id:6,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767},{id:4,lvl:32767}],Unbreakable:1}");
        godHelmet.setStackDisplayName("\247r\247b\247l神之头盔");
        items.add(godHelmet);

        // God Chestplate
        final ItemStack godChestplate = ItemUtil.getItemStack("diamond_chestplate 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godChestplate.setStackDisplayName("\247r\247b\247l神之胸甲");
        items.add(godChestplate);

        // God Leggings
        final ItemStack godLeggings = ItemUtil.getItemStack("diamond_leggings 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godLeggings.setStackDisplayName("\247r\247b\247l神之裤腿");
        items.add(godLeggings);

        // God Boots
        final ItemStack godBoots = ItemUtil.getItemStack("diamond_boots 1 0  {ench:[{id:0,lvl:32767},{id:8,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godBoots.setStackDisplayName("\247r\247b\247l神之靴子");
        items.add(godBoots);

        // OP Sign
        final ItemStack opSign = ItemUtil.getItemStack("sign 1 0 {BlockEntityTag:{Text1:\"{\\\"text\\\":\\\"右键我查看彩蛋!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op " + mc.getSession().getUsername() + "\\\"}}\"}}");
        opSign.setStackDisplayName("\247rOP 牌子");
        items.add(opSign);

        // OP Book
        final ItemStack opBook = ItemUtil.getItemStack("written_book 1 0 {pages:[\"{\\\"text\\\":\\\"点击我查看彩蛋!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op " + mc.getSession().getUsername() + "\\\"}}\"],title:\"彩蛋\",author:" + mc.getSession().getUsername() + "}");
        items.add(opBook);
    }

    @Override
    public String getTranslatedTabLabel() {
        return Reversal.NAME;
    }

    @Override
    public Item getTabIconItem() {
        return Items.diamond;
    }
}