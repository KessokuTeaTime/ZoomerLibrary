package org.thinkingstudio.zoomerlibrary.api.transitions;

import net.minecraft.util.Identifier;
import org.thinkingstudio.zoomerlibrary.api.TransitionMode;

/**
 * An implementation of a simple zoom as a transition mode.
 */
public class InstantTransitionMode implements TransitionMode {
	private static final Identifier TRANSITION_ID = new Identifier("zoomerlibrary:no_transition");
	private boolean active;
	private double divisor;

	/**
	 * Initializes an instance of the instant transition mode.
	*/
	public InstantTransitionMode() {
		this.active = false;
		this.divisor = 1.0;
	}

	@Override
	public Identifier getIdentifier() {
		return TRANSITION_ID;
	}

	@Override
	public boolean getActive() {
		return this.active;
	}

	@Override
	public double applyZoom(double fov, float tickDelta) {
		return fov / this.divisor;
	}

	@Override
	public void tick(boolean active, double divisor) {
		this.active = active;
		this.divisor = divisor;
	}

	@Override
	public double getInternalMultiplier() {
		return 1.0 / this.divisor;
	}
}
