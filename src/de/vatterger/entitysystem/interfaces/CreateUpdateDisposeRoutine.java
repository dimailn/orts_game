package de.vatterger.entitysystem.interfaces;

public interface CreateUpdateDisposeRoutine {
	public abstract void create();
	public abstract void update(float delta);
	public abstract void dispose();
}
