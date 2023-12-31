package hxpwpt.ingredients;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;

public abstract class MoreAbstractIngredient extends AbstractIngredient {
    @Override
    public abstract boolean isEmpty ();
    @Override
    public abstract ItemStack [] getItems ();
}
