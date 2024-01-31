package org.thinkingstudio.zoomerlibrary.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.thinkingstudio.zoomerlibrary.ZoomerLibrary;

@Mod(ZoomerLibrary.MODID)
public class ZoomerLibraryForge {
    public ZoomerLibraryForge() {
        EventBuses.registerModEventBus(ZoomerLibrary.MODID, FMLJavaModLoadingContext.get().getModEventBus());
    }
}
