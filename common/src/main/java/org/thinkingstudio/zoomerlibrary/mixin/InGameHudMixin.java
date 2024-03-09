package org.thinkingstudio.zoomerlibrary.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import org.thinkingstudio.zoomerlibrary.api.ZoomInstance;
import org.thinkingstudio.zoomerlibrary.api.ZoomOverlay;
import org.thinkingstudio.zoomerlibrary.api.ZoomRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(
		method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/client/MinecraftClient.getLastFrameDuration()F"
		)
	)
	private void injectZoomOverlay(GuiGraphics graphics, float tickDelta, CallbackInfo ci, @Share("cancelOverlay") LocalBooleanRef cancelOverlay) {
		cancelOverlay.set(false);
		for (ZoomInstance instance : ZoomRegistry.getZoomInstances()) {
			ZoomOverlay overlay = instance.getZoomOverlay();
			if (overlay != null) {
				overlay.tickBeforeRender();
				if (overlay.getActive()) {
					cancelOverlay.set(overlay.cancelOverlayRendering());
					overlay.renderOverlay(graphics);
				}
			}
		}
	}

	// Cancel the cancellable overlays
	@ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
	private boolean cancelOverlay(boolean original, @Share("cancelOverlay") LocalBooleanRef cancelOverlay) {
		return original && !cancelOverlay.get();
	}
}
