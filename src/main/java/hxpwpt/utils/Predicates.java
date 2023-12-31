package hxpwpt.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class Predicates {
    public static Predicate <BlockState> from_block (final Predicate <Block> block) {
        return (x) -> block.test (x.getBlock ());
    }
    public static Predicate <BlockState> from_block (final Supplier <Block> block) {
        return (x) -> x.getBlock () == block.get ();
    }
    public static Predicate <ItemStack> from_item (final Predicate <Item> item) {
        return (x) -> item.test (x.getItem ());
    }
    public static Predicate <ItemStack> from_item (final Supplier <Item> item) {
        return (x) -> x.getItem () == item.get ();
    }
}
