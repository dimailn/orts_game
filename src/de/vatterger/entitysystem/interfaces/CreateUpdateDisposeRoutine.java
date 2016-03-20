package de.vatterger.entitysystem.interfaces;

public interface CreateUpdateDisposeRoutine {
	public abstract void create() throws Exception;
	public abstract void update(float delta);
	public abstract void dispose();
}