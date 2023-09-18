package org.thinkingstudio.zoomerlibrary.mixin;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.zoomerlibrary.ZoomerLibrary;
import org.thinkingstudio.zoomerlibrary.impl.SpyglassHelper;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class ModelPredicateProviderRegistryMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void addScopingPredicateToModdedSpyglasses(CallbackInfo ci) {
		ItemPropertiesRegistry.registerGeneric(new Identifier(ZoomerLibrary.MODID, "scoping"), (stack, clientWorld, entity, i) ->
			entity != null
				&& entity.isUsingItem()
				&& entity.getActiveItem() == stack
				&& entity.getActiveItem().isIn(SpyglassHelper.SPYGLASSES)
				? 1.0F
				: 0.0F
		);
	}
}
