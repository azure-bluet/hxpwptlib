package hxpwpt;

import hxpwpt.recipes.SerializerRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod (HxPwPtLibInit.MOD_ID)
public class HxPwPtLibInit {
    public static final String MOD_ID = "hxpwptlib";
    public HxPwPtLibInit () {
        IEventBus bus = FMLJavaModLoadingContext.get () .getModEventBus ();
        SerializerRegistry.registry.register (bus);
        CreativeTab.registry.register (bus);
    }
}
