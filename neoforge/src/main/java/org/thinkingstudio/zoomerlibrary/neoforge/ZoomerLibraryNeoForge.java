package org.thinkingstudio.zoomerlibrary.neoforge;

import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import org.thinkingstudio.zoomerlibrary.ZoomerLibrary;

@Mod(ZoomerLibrary.MODID)
public class ZoomerLibraryNeoForge {
    public ZoomerLibraryNeoForge() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
    }
}
