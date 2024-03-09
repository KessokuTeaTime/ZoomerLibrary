package org.thinkingstudio.zoomerlibrary.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import org.thinkingstudio.zoomerlibrary.api.ZoomInstance;
import org.thinkingstudio.zoomerlibrary.api.ZoomRegistry;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Inject(
		method = "updateLookDirection()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/Option;"
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void applyZoomChanges(CallbackInfo ci, @Local(ordinal = 1) double e, @Local(ordinal = 2) LocalDoubleRef k, @Local(ordinal = 3) LocalDoubleRef l, @Local(ordinal = 6) double h) {
		if (ZoomRegistry.shouldIterateModifiers()) {
			for (ZoomInstance instance : ZoomRegistry.getZoomInstances()) {
				if (instance.isModifierActive()) {
					double zoomDivisor = instance.getZoom() ? instance.getZoomDivisor() : 1.0;
					double transitionDivisor = instance.getTransitionMode().getInternalMultiplier();
					k.set(instance.getMouseModifier().applyXModifier(k.get(), h, e, zoomDivisor, transitionDivisor));
					l.set(instance.getMouseModifier().applyYModifier(l.get(), h, e, zoomDivisor, transitionDivisor));
				}
			}
		}
	}
}
