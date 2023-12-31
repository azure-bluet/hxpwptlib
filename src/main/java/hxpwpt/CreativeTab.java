package hxpwpt;

import static hxpwpt.HxPwPtLibInit.MOD_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import hxpwpt.utils.Suppliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTab {
    public final List <Supplier <ItemStack>> li;
    public final Supplier <ItemStack> icon;
    public final Supplier <Component> title;
    public final String regname;
    public CreativeTab (Supplier <ItemStack> icon, Supplier <Component> title, String regname) {
        this.li = new ArrayList <> ();
        this.icon = icon;
        this.title = title;
        this.regname = regname;
    }
    public CreativeTab (Supplier <ItemStack> icon, String title, String regname) {
        this (icon, () -> Component.translatable (title), regname);
    }
    public CreativeTab add (Supplier <ItemStack> sup) {
        li.add (sup);
        return this;
    }
    public CreativeTab add (RegistryObject <Item> item) {
        li.add (() -> new ItemStack (item.get ()));
        return this;
    }
    public CreativeTab add (Item item) {
        li.add (() -> new ItemStack (item));
        return this;
    }
    public CreativeTab add (SimpleTieredItems items) {
        this.add (items.sword);
        this.add (items.axe);
        this.add (items.pickaxe);
        this.add (items.shovel);
        this.add (items.hoe);
        return this;
    }
    public CreativeTab join (List <Supplier <ItemStack>> lo) {
        for (Supplier <ItemStack> si : lo) {
            li.add (si);
        }
        return this;
    }
    public static DeferredRegister <CreativeModeTab> regdr (String name) {
        return DeferredRegister.create (Registries.CREATIVE_MODE_TAB, name);
    }
    public RegistryObject <CreativeModeTab> register (DeferredRegister <CreativeModeTab> dr) {
        return dr.register (this.regname, 
            () -> CreativeModeTab.builder ()
            .icon (this.icon)
            .title (this.title.get ())
            .displayItems (
                (p, t) -> {
                    for (Supplier <ItemStack> s : this.li) {
                        t.accept (s.get ());
                    }
                }
            )
            .build ()
        );
    }

    // A Creative mode tab that adds some interesting things
    public static final DeferredRegister <CreativeModeTab> registry = regdr (MOD_ID);
    public static final CreativeTab tab = new CreativeTab (Enchanters.icon_enchanted (() -> Items.GOLDEN_SWORD), "tab.hxpwptlib.default", "default");
    static {
        tab.add (
            Enchanters.enchant (Suppliers.from_item (Items.GOLDEN_SWORD), Enchantments.SHARPNESS, 10)
        );
    }
    public static final RegistryObject <CreativeModeTab> default_tab = tab.register (registry);
}
