package hxpwpt;

import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SimpleFeature {
    public static DeferredRegister <Feature <?>> regdr (String name) {
        return DeferredRegister.create (ForgeRegistries.FEATURES, name);
    }
    public final Predicate <FeaturePlaceContext <OreConfiguration>> poss;
    public static class InDim implements Predicate <FeaturePlaceContext <OreConfiguration>> {
        public final Set <ResourceKey <Level>> dims;
        public InDim (Set <ResourceKey <Level>> dims) {
            this.dims = Set.copyOf (dims);
        }
        @SafeVarargs
        public InDim (ResourceKey <Level>... lvls) {
            this (Set.of (lvls));
        }
        public boolean test (FeaturePlaceContext <OreConfiguration> foc) {
            return dims.contains (foc.level () .getLevel () .dimension ());
        }
    }
    public SimpleFeature (Predicate <FeaturePlaceContext <OreConfiguration>> poss) {
        this.poss = poss;
    }
    public class HxPwPtFeature extends OreFeature {
        public HxPwPtFeature () {
            super (OreConfiguration.CODEC);
        }
        public boolean place (FeaturePlaceContext <OreConfiguration> foc) {
            if (poss.test (foc)) {
                return super.place (foc);
            } else return false;
        }
    }
    public RegistryObject <Feature <?>> reg (DeferredRegister <Feature <?>> dr, String name) {
        return dr.register (name, () -> new HxPwPtFeature ());
    }
}
