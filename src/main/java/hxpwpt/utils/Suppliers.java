package hxpwpt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class Suppliers {
    public static Supplier <ItemStack> from_item (Supplier <Item> sup) {
        return () -> new ItemStack (sup.get ());
    }
    public static Supplier <ItemStack> from_item (Item item) {
        return () -> new ItemStack (item);
    }
    @SafeVarargs
    public static List <Supplier <ItemStack>> from_item (Supplier <Item>... sups) {
        List <Supplier <ItemStack>> res = new ArrayList <> ();
        int i;
        for (i=0; i<sups.length; i++) {
            if (sups [i] == null) continue;
            res.add (from_item (sups [i]));
        }
        return List.copyOf (res);
    }
}
