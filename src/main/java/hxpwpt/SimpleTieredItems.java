package hxpwpt;

import javax.annotation.Nullable;

import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SimpleTieredItems {
    public final RegistryObject <Item> sword;
    public final RegistryObject <Item> pickaxe;
    public final RegistryObject <Item> axe;
    public final RegistryObject <Item> shovel;
    public final RegistryObject <Item> hoe;
    public static class Properties {
        public Properties () {}
        public Properties (Item.Properties properties) {
            this.sword = this.pickaxe = this.axe = this.shovel = this.hoe = properties;
        }
        @Nullable public Item.Properties sword;
        public int sword_atk_damage = 3,
                   pickaxe_atk_damage = 1,
                   hoe_atk_damage = -4;
        public float shovel_atk_damage = 1.5f,
                     axe_atk_damage = 5.0f;
        public float sword_atk_speed = -2.4f,
                     shovel_atk_speed = 3.0f,
                     pickaxe_atk_speed = -2.8f,
                     axe_atk_speed = -3.0f,
                     hoe_atk_speed = 0.0f;
        @Nullable public Item.Properties pickaxe = null;
        @Nullable public Item.Properties axe = null;
        @Nullable public Item.Properties shovel = null;
        @Nullable public Item.Properties hoe = null;
    }
    public static Item.Properties n (@Nullable Item.Properties p) {
        return p == null ? new Item.Properties () : p;
    }
    public SimpleTieredItems (DeferredRegister <Item> reg, String prefix, ModItemTier tier, Properties pro) {
        this.sword = reg.register (prefix + "_sword", () -> new SwordItem (tier, pro.sword_atk_damage, pro.sword_atk_speed, n (pro.sword)));
        this.pickaxe = reg.register (prefix + "_pickaxe", () -> new PickaxeItem (tier, pro.pickaxe_atk_damage, pro.pickaxe_atk_speed, n (pro.pickaxe)));
        this.axe = reg.register (prefix + "_axe", () -> new AxeItem (tier, pro.axe_atk_damage, pro.axe_atk_speed, n (pro.axe)));
        this.shovel = reg.register (prefix + "_shovel", () -> new ShovelItem (tier, pro.shovel_atk_damage, pro.axe_atk_speed, n (pro.shovel)));
        this.hoe = reg.register (prefix + "_hoe", () -> new HoeItem (tier, pro.hoe_atk_damage, pro.hoe_atk_speed, n (pro.hoe)));
    }
    public SimpleTieredItems (DeferredRegister <Item> reg, String prefix, ModItemTier tier) {
        this (reg, prefix, tier, new Properties ());
    }
}
