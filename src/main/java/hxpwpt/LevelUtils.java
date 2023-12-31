package hxpwpt;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LevelUtils {
    /* Refers to simple explosion */
    public static void se (Level level, BlockPos pos, float radius) {
        level.explode (null, ((double) pos.getX ()) + .5d, ((double) pos.getY ()) + .5d, ((double) pos.getZ ()) + .5d, radius, ExplosionInteraction.BLOCK);
    }
    /* Refers to summon item */
    public static void si (Level level, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity (level, ((double) pos.getX ()) + .5d, ((double) pos.getY ()) + .5d, ((double) pos.getZ ()) + .5d, stack);
        entity.setPickUpDelay (0);
        entity.setUnlimitedLifetime ();
        level.addFreshEntity (entity);
    }
    /* Refers to place tall flower */
    public static boolean ptf (@Nullable Level level, BlockPos base, DoublePlantBlock block) {
        if (level == null) return false;
        if (level.isOutsideBuildHeight (base) || level.isOutsideBuildHeight (base.above ())) return false;
        BlockState state = block.defaultBlockState ();
        if (!level.isEmptyBlock (base) || !level.isEmptyBlock (base.above ())) return false;
        DoublePlantBlock.placeAt (level, state, base, 3);
        return true;
    }
    @Nullable
    public static ClientLevel cli () {
        Minecraft minecraft = Minecraft.getInstance ();
        return minecraft.level;
    }
}
