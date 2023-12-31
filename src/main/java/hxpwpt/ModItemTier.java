package hxpwpt;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ModItemTier implements Tier {

    protected final int harvest_level;
    protected final int max_uses;
    protected final float efficiency;
    protected final float attack_damage;
    protected final int enchantability;
    protected final Supplier <Ingredient> repair_material;

    public ModItemTier (int harvest_level, int max_uses, float efficiency, float attack_damage, int enchantability, Supplier <Ingredient> repair_material) {
        this.harvest_level = harvest_level;
        this.max_uses = max_uses;
        this.efficiency = efficiency;
        this.attack_damage = attack_damage;
        this.enchantability = enchantability;
        this.repair_material = repair_material;
    }

    @Override
    public int getUses () {
        return this.max_uses;
    }

    @Override
    public float getSpeed () {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus () {
        return this.attack_damage;
    }

    @Override
    public int getLevel() {
        return this.harvest_level;
    }

    @Override
    public int getEnchantmentValue () {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient () {
        return this.repair_material.get ();
    }
}
