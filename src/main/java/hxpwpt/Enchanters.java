package hxpwpt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class Enchanters {
    public static Map <Enchantment, Integer> by_str (@Nullable ItemStack stack, String from) {
        if (stack == null) return Map.of ();
        CompoundTag tag = stack.getTag ();
        if (tag == null) return Map.of ();
        ListTag ltag = tag.getList (from, 10);
        return EnchantmentHelper.deserializeEnchantments (ltag);
    }
    public static Map <Enchantment, Integer> book (@Nullable ItemStack stack) {
        return by_str (stack, "StoredEnchantments");
    }
    public static boolean test_ench (Map <Enchantment, Integer> required, Map <Enchantment, Integer> given) {
        for (Enchantment e : required.keySet ()) {
            if (!given.containsKey (e)) return false;
            if (given.get (e) .intValue () < required.get (e) .intValue ()) return false;
        }
        return true;
    }
    public static void toNetwork (FriendlyByteBuf buf, Map <Enchantment, Integer> map) {
        buf.writeInt (map.size ());
        for (Enchantment e : map.keySet ()) {
            buf.writeResourceLocation (ForgeRegistries.ENCHANTMENTS.getKey (e));
            buf.writeInt (map.get (e) .intValue ());
        }
    }
    public static Map <Enchantment, Integer> fromNetwork (FriendlyByteBuf buf) {
        Map <Enchantment, Integer> res = new HashMap <> ();
        int i, size = buf.readInt ();
        for (i=0; i<size; i++) {
            Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue (buf.readResourceLocation ());
            Integer lvl = Integer.valueOf (buf.readInt ());
            res.put (e, lvl);
        }
        return Map.copyOf (res);
    }
    public static Map <Enchantment, Integer> mix (Map <Enchantment, Integer> a, Map <Enchantment, Integer> b) {
        a = new HashMap <> (a);
        for (Enchantment e : b.keySet ()) {
            int m = b.get (e) .intValue ();
            if (a.containsKey (e)) m = Math.max (m, a.get (e) .intValue ());
            a.put (e, Integer.valueOf (m));
        }
        return Map.copyOf (a);
    }
    public static Supplier <ItemStack> enchant (Supplier <ItemStack> original, Map <Enchantment, Integer> map, String tag_on) {
        return () -> {
            ItemStack stack = original.get ();
            ListTag tag = new ListTag ();
            Map <Enchantment, Integer> mixed = mix (map, by_str (stack, tag_on));
            for (Enchantment e : mixed.keySet ()) {
                if (e == null) continue;
                ResourceLocation location = EnchantmentHelper.getEnchantmentId (e);
                tag.add (EnchantmentHelper.storeEnchantment (location, mixed.get (e) .intValue ()));
            }
            if (tag.isEmpty ()) stack.removeTagKey (tag_on);
            else stack.addTagElement (tag_on, tag);
            return stack;
        };
    }
    public static Supplier <ItemStack> enchant (Supplier <ItemStack> original, Map <Enchantment, Integer> map) {
        return enchant (original, map, "Enchantments");
    }
    public static Supplier <ItemStack> enchant (Supplier <ItemStack> original, Enchantment e, int lvl) {
        return enchant (original, Map.of (e, lvl));
    }
    public static Supplier <ItemStack> icon_enchanted (Supplier <Item> sup) {
        return () -> {
            ItemStack stack = new ItemStack (sup.get ());
            EnchantmentHelper.setEnchantments (Map.of (Enchantments.MENDING, Integer.valueOf (1)), stack);
            return stack;
        };
    }
    public static final Codec <Map <Enchantment, Integer>> map_codec = Codec.unboundedMap (ForgeRegistries.ENCHANTMENTS.getCodec (), Codec.INT.stable ());
}
