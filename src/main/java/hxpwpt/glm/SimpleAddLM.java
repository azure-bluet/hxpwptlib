package hxpwpt.glm;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;

public class SimpleAddLM extends LootModifier {
    private final List <ItemStack> stack;
    private final ResourceLocation original;
    public List <ItemStack> stack () {
        return this.stack;
    }
    public ResourceLocation original () {
        return this.original;
    }
    public SimpleAddLM (LootItemCondition[] conditions, ResourceLocation original, List <ItemStack> stack) {
        super (conditions);
        this.original = original;
        this.stack = stack;
        for (ItemStack s : stack) {
            System.out.println (s.toString ());
        }
    }
    @Override
    protected ObjectArrayList <ItemStack> doApply (ObjectArrayList <ItemStack> in, LootContext context) {
        if (context.getQueriedLootTableId () .equals (this.original)) {
            for (ItemStack s : this.stack) in.add (s.copy ());
        }
        return in;
    }
    public static final RegistryObject <Codec <SimpleAddLM>> codec = GLMRegistry.registry.register (
        "simple_add",
        SimpleAddLM::get_codec
    );
    public static Codec <SimpleAddLM> get_codec () {
        return RecordCodecBuilder.create (
            (instance) -> LootModifier.codecStart (instance) .and (
                instance.group (
                    ResourceLocation.CODEC.fieldOf ("original") .forGetter (SimpleAddLM::original),
                    CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.listOf () .fieldOf ("stacks") .forGetter (SimpleAddLM::stack)
                )
            ) .apply (instance, SimpleAddLM::new)
        );
    }
    public Codec <SimpleAddLM> codec () {
        return codec.get ();
    }
    public static void load () {}
}
