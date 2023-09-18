package io.github.ennuil.libzoomer_test;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import io.github.ennuil.libzoomer.api.ZoomInstance;
import io.github.ennuil.libzoomer.api.ZoomRegistry;
import io.github.ennuil.libzoomer.api.modifiers.CinematicCameraMouseModifier;
import io.github.ennuil.libzoomer.api.modifiers.SpyglassMouseModifier;
import io.github.ennuil.libzoomer.api.overlays.SpyglassZoomOverlay;
import io.github.ennuil.libzoomer.api.transitions.InstantTransitionMode;
import io.github.ennuil.libzoomer.api.transitions.SmoothTransitionMode;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.item.Item;
import net.minecraft.item.SpyglassItem;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

@Mod(ZoomerLibraryTestMod.MODID)
public class ZoomerLibraryTestMod {
	public static final String MODID = "zoomerlibrary_test";

	public ZoomerLibraryTestMod() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::onInitialize);
		modEventBus.addListener(this::onInitializeClient);
	}

	// Michael's Zoom Instance
	private static final ZoomInstance MICHAEL_ZOOM = new ZoomInstance(
		new Identifier("libzoomer_test:zoom"),
		10.0F, new SmoothTransitionMode(),
		new SpyglassMouseModifier(),
		new SpyglassZoomOverlay(new Identifier("libzoomer_test:textures/misc/michael.png"))
	);

	// Michelle's Zoom Instance
	private static final ZoomInstance MICHELLE_ZOOM = new ZoomInstance(
		new Identifier("libzoomer_test:zoom_2"),
		3.0F, new InstantTransitionMode(),
		new CinematicCameraMouseModifier(),
		null
	);

	// Michael. He's a reimplementation of the spyglass. Tests if the spyglass can be replicated.
	private static final Item MICHAEL_ITEM = new SpyglassItem(new Item.Settings().maxCount(1));

	// Michelle. She's an implementation of a very simple zoom key. Tests if there are zoom instance conflicts and spyglass-unrelated things.
	private static final KeyBind MICHELLE_KEY = new KeyBind(
		"key.libzoomer_test.michelle",
		GLFW.GLFW_KEY_V,
		"key.libzoomer_test.category"
	);

	public void onInitialize(FMLCommonSetupEvent event) {
		// Register the Michael item
		Registry.register(Registry.ITEM, new Identifier("libzoomer_test:michael"), MICHAEL_ITEM);
	}

	public void onInitializeClient(FMLClientSetupEvent event) {
		// This prints out all zoom instances registered so far and some extra info
		for (ZoomInstance instance : ZoomRegistry.getZoomInstances()) {
			System.out.println("Id: " + instance.getInstanceId() + " | Zooming: " + instance.getZoom() + " | Divisor: " + instance.getZoomDivisor());
		}

		//KeyBindingHelper.registerKeyBinding(MICHELLE_KEY);
	}

	@SubscribeEvent
	public void registerKeys(RegisterKeyMappingsEvent event) {
		event.register(MICHELLE_KEY);
	}

	@SubscribeEvent
	public void endClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.ClientTickEvent.Phase.END && MinecraftClient.getInstance().world == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();

		// This is how you get a spyglass-like zoom working
		if (client.player == null) return;

		MICHAEL_ZOOM.setZoom(
			client.options.getPerspective().isFirstPerson()
			&& (
				client.player.isUsingItem()
				&& client.player.getActiveItem().isOf(MICHAEL_ITEM)
			)
		);

		// And this is how you get a simple zoom button working
		MICHELLE_ZOOM.setZoom(MICHELLE_KEY.isPressed());
	}
}
