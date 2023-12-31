package hxpwpt.utils;

import net.minecraft.network.chat.Component;

public abstract class Texts {
    public static Component bitrans (String a, String middle, String b) {
        return Component.translatable (a) .append (middle)  .append (Component.translatable ((b)));
    }
}
