package hxpwpt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class Stacker implements Supplier <ItemStack> {
    private final Supplier <ItemStack> original;
    private final List <Function <ItemStack, ItemStack>> ops;
    public static Supplier <ItemStack> simple (Supplier <Item> s) {
        return () -> new ItemStack (s.get ());
    }
    public Stacker (Supplier <ItemStack> orig) {
        this.ops = new ArrayList <> ();
        this.original = orig;
    }
    public ItemStack get () {
        ItemStack s = this.original.get ();
        for (Function <ItemStack, ItemStack> i : this.ops) {
            s = i.apply (s);
        }
        return s;
    }
    public void add (Function <ItemStack, ItemStack> func) {
        this.ops.add (func);
    }
    public Stacker enchant (Enchantment ench, int lvl) {
        this.add (
            (ItemStack is) -> {
                is.enchant (ench, lvl);
                return is;
            }
        );
        return this;
    }
    public Stacker enchant (Enchantment ench) {
        return this.enchant (ench, 1);
    }
    public static ItemStack increase (ItemStack in) {
        in.setCount (in.getCount () + 1);
        return in;
    }
    public static ItemStack decrease (ItemStack in) {
        in.setCount (in.getCount () - 1);
        return in;
    }
}
