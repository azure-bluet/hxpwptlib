package hxpwpt;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;

public abstract class SimpleSerializerHelper extends CustomRecipe {
    public SimpleSerializerHelper (CraftingBookCategory cat) {
        super (cat);
    }
    public abstract List <Predicate <ItemStack>> predicates ();
    public static Predicate <ItemStack> simple (Supplier <Item> sup) {
        return (stack) -> stack.is (sup.get ());
    }
    public boolean canCraftInDimensions (int x, int y) {
        return x * y >= predicates () .size ();
    }
    public boolean matches (CraftingContainer container, Level level) {
        List <Predicate <ItemStack>> pre = predicates ();
        boolean [] flags = new boolean [pre.size ()];
        int i;
        for (i=0; i<predicates () .size (); i++) flags [i] = false;
        for (i=0; i<container.getContainerSize (); i++) {
            int j;
            boolean filled = false;
            for (j=0; j<pre.size (); j++) {
                if (pre.get (j) .test (container.getItem (i))) {
                    if (flags [j]) return false;
                    else flags [j] = true;
                    filled = true;
                    break;
                }
            }
            if (!filled && !container.getItem (i) .isEmpty ()) return false;
        }
        boolean flag = true;
        for (i=0; i<pre.size (); i++) flag = flags [i] && flag;
        return flag;
    }
}
