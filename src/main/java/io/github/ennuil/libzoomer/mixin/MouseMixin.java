package io.github.ennuil.libzoomer.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.ennuil.libzoomer.api.ZoomInstance;
import io.github.ennuil.libzoomer.api.ZoomRegistry;
import net.minecraft.client.Mouse;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Unique
	private boolean modifyMouse;

	@Unique
	private double finalCursorDeltaX;

	@Unique
	private double finalCursorDeltaY;

	@Inject(
		method = "updateLookDirection()V",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/client/option/GameOptions.invertYMouse:Z",
			opcode = Opcodes.GETFIELD
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void applyZoomChanges(CallbackInfo ci, double d, double e, double k, double l, double f, double g, double h, int m) {
		this.modifyMouse = false;
		if (ZoomRegistry.shouldIterateZoom() || ZoomRegistry.shouldIterateModifiers()) {
			for (ZoomInstance instance : ZoomRegistry.getZoomInstances()) {
				if (instance.getMouseModifier() != null) {
					boolean zoom = instance.getZoom();
					if (zoom || instance.isModifierActive()) {
						instance.getMouseModifier().tick(zoom);
						double zoomDivisor = zoom ? instance.getZoomDivisor() : 1.0;
						double transitionDivisor = instance.getTransitionMode().getInternalMultiplier();
						k = instance.getMouseModifier().applyXModifier(k, h, e, zoomDivisor, transitionDivisor);
						l = instance.getMouseModifier().applyYModifier(l, h, e, zoomDivisor, transitionDivisor);
						this.modifyMouse = true;
					}
				}
			}
		}
		this.finalCursorDeltaX = k;
		this.finalCursorDeltaY = l;
	}

	@ModifyVariable(
		method = "updateLookDirection()V",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/client/option/GameOptions.invertYMouse:Z",
			opcode = Opcodes.GETFIELD
		),
		ordinal = 2
	)
	private double modifyFinalCursorDeltaX(double k) {
		return this.modifyMouse ? finalCursorDeltaX : k;
	}

	@ModifyVariable(
		method = "updateLookDirection()V",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/client/option/GameOptions.invertYMouse:Z",
			opcode = Opcodes.GETFIELD
		),
		ordinal = 3
	)
	private double modifyFinalCursorDeltaY(double l) {
		return this.modifyMouse ? finalCursorDeltaY : l;
	}
}
