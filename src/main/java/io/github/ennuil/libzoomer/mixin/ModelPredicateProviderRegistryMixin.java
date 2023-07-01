package io.github.ennuil.libzoomer.mixin;

import io.github.ennuil.libzoomer.LibZoomerForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.ennuil.libzoomer.impl.SpyglassHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class ModelPredicateProviderRegistryMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void addScopingPredicateToModdedSpyglasses(CallbackInfo ci) {
		ModelPredicateProviderRegistry.registerGeneric(new Identifier(LibZoomerForge.MODID, "scoping"), (stack, clientWorld, entity, i) ->
			entity != null
				&& entity.isUsingItem()
				&& entity.getActiveItem() == stack
				&& entity.getActiveItem().isIn(SpyglassHelper.SPYGLASSES)
				? 1.0F
				: 0.0F
		);
	}
}
