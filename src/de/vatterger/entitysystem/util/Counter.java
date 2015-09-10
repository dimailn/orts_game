package de.vatterger.entitysystem.util;

public class Counter {

	private int tick;
	private int tickMax;
	
	public Counter(int tickMax) {
		reset();
		this.tickMax = Functions.max(tickMax, 0);
	}
	
	public boolean tick() {
		tick++;
		return isActive();
	}
	
	public boolean isActive() {
		return !(tick<tickMax);
	}
	
	public void reset() {
		tick = 0;
	}
}
