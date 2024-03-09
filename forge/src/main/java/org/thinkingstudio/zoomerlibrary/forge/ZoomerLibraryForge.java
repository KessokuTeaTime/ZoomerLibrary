package org.thinkingstudio.zoomerlibrary.forge;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.thinkingstudio.zoomerlibrary.ZoomerLibrary;

@Mod(ZoomerLibrary.MODID)
public class ZoomerLibraryForge {
    public ZoomerLibraryForge() {
        EventBuses.registerModEventBus(ZoomerLibrary.MODID, FMLJavaModLoadingContext.get().getModEventBus());
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		if (FMLLoader.getDist().isClient()) {
			modEventBus.addListener(this::onPreLaunch);
		}
    }

	public void onPreLaunch(InterModProcessEvent event) {
		event.enqueueWork(() -> {
			MixinExtrasBootstrap.init();
		});
	}
}
