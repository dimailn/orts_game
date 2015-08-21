package de.vatterger.entitysystem.tools;

import com.artemis.Entity;
import com.artemis.World;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.esotericsoftware.kryonet.Connection;

import de.vatterger.entitysystem.components.DataBucket;
import de.vatterger.entitysystem.components.Name;
import de.vatterger.entitysystem.components.SlimeCollision;
import de.vatterger.entitysystem.components.ClientConnection;
import de.vatterger.entitysystem.components.Position;
import de.vatterger.entitysystem.components.RemoteMaster;
import de.vatterger.entitysystem.components.Velocity;
import de.vatterger.entitysystem.components.ViewFrustum;

import static de.vatterger.entitysystem.tools.GameConstants.*;

public class SlimeSlickFactory {

	private SlimeSlickFactory() {}
	
	public static Entity createSlime(World world, Vector2 position) {
		Entity e = world.createEntity();
		return e.edit()
			.add(new Position(position))
			.add(new SlimeCollision(SLIME_INITIAL_SIZE, e))
			.add(new Velocity(new Vector2(MathUtils.random(-10f, 10f), MathUtils.random(-10f, 10f))))
			.add(new RemoteMaster(Position.class,SlimeCollision.class, Velocity.class))
		.getEntity();
	}

	public static Entity createSmallEdible(World world, Vector2 position) {
		Entity e = world.createEntity();
		return e.edit()
			.add(new Position(position))
			.add(new SlimeCollision(SMALL_EDIBLE_SIZE, e))
			.add(new RemoteMaster(Position.class, SlimeCollision.class))
		.getEntity();
	}

	public static Entity createPlayer(World world, Connection c) {
		return world.createEntity().edit()
			.add(new ClientConnection(c))
			.add(new DataBucket())
			.add(new Name("#Player "+c))
			.add(new ViewFrustum(new Rectangle(0, 0, XY_BOUNDS, XY_BOUNDS)))
		.getEntity();
	}
}
