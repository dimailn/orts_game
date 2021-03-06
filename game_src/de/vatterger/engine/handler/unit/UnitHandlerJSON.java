package de.vatterger.engine.handler.unit;

import com.artemis.EntityEdit;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import de.vatterger.engine.handler.asset.AtlasHandler;
import de.vatterger.engine.util.JSONPropertiesHandler;
import de.vatterger.engine.util.Math2D;
import de.vatterger.game.components.gameobject.*;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class UnitHandlerJSON {
	
	private UnitHandlerJSON() {}
	
	/**
	 * Adds a tank unit to the {@link World}
	 * @param name The type name of unit
	 * @param position The world position of this unit
	 * @param world The world to add this unit to
	 * @return The entity id or if failed -1
	 */
	public static int createTank(String name, Vector3 position, World world) {
		
		JSONPropertiesHandler properties = new JSONPropertiesHandler("assets/data/tank/"+name+".json");
		
		if(!properties.exists())
			return -1;
		
		JsonValue root = properties.get();
		
		JsonValue turrets = root.get("turrets");
		
		HashMap<String, Integer> nameToIdMap = new HashMap<String, Integer>(turrets.size*2);
		
		int hullId = AtlasHandler.getIdFromName(root.getString("sprite", name + "_h"));
		
		
		int e_hull = world.create();
		nameToIdMap.put("hull", e_hull);
		
		Turrets turretsComponent = new Turrets(turrets.size);
		
		float hullRotation = MathUtils.random(360f);
		
		EntityEdit ed = world.edit(e_hull);
		ed.add(new AbsolutePosition(position.x, position.y, position.z))
		.add(new AbsoluteRotation(hullRotation))
		.add(new SpriteID(hullId))
		.add(new SpriteLayer(SpriteLayer.OBJECTS0))
		.add(new CullDistance(
				root.getFloat("cullradius", 32f),
				root.getFloat("cullradius_offset_x", 0f),
				root.getFloat("cullradius_offset_y", 0f)))
		.add(turretsComponent);
		
		if(root.has("collisionradius")) {
			//ed.add(new CollisionRadius(root.getFloat("collisionradius", 0f)));
		}
		
		for (int i = 0; i < turrets.size; i++) {
			
			JsonValue turret = turrets.get(i);
			
			int turretId = AtlasHandler.getIdFromName(turret.getString("sprite", name + "_t" + i));
			//int flashId = AtlasHandler.getIdFromName(turret.getString("flash_sprite", "flash_big"));
			
			Vector3 offset = new Vector3(
					turret.getFloat("x", 0f),
					turret.getFloat("y", 0f),
					turret.getFloat("z", 0f)
			);
			
			
			int e_turret = world.create();
			
			nameToIdMap.put("turret_" + i, e_turret);
			String s_turret_parent = turret.getString("parent", "hull");
			int e_turret_parent = nameToIdMap.getOrDefault(s_turret_parent, e_hull);
			
			turretsComponent.turretIds[i] = e_turret;
			
			world.edit(e_turret)
			.add(new AbsolutePosition())
			.add(new AbsoluteRotation())
			.add(new Attached(e_turret_parent, offset))
			.add(new SpriteID(turretId))
			.add(new SpriteLayer(SpriteLayer.OBJECTS0))
			.add(new Turret())
			.add(new CullingParent(e_hull));
			
			
			/*int e_flash_turret = world.create();
			
			world.edit(e_flash_turret)
			.add(new AbsolutePosition())
			.add(new AbsoluteRotation())
			.add(new Attached(e_turret, new Vector3(
					turret.getFloat("flash_offset_x", 0f),
					turret.getFloat("flash_offset_y", 2f),
					turret.getFloat("flash_offset_z", 0f))
					))
			.add(new SpriteID(flashId))
			.add(new SpriteDrawMode(GL11.GL_ONE, GL11.GL_ONE))
			.add(new SpriteLayer(SpriteLayer.OBJECTS0))
			.add(new CullDistance(16f));*/
		}
		
		return e_hull;
	}
	
	/**
	 * Adds an infantry unit to the {@link World}
	 * @param name The type name of unit
	 * @param position The world position of this unit
	 * @param world The world to add this unit to
	 * @return The entity id or if failed -1
	 */
	public static int createInfatry(String name, Vector3 position, World world) {
		JSONPropertiesHandler properties = new JSONPropertiesHandler("assets/data/infantry/"+name+".json");
		
		if(!properties.exists())
			return -1;
		
		JsonValue root = properties.get();
		
		int spriteID = AtlasHandler.getIdFromName(root.getString("sprite"));
		
		int e = world.create();
		
		world.edit(e)
		.add(new AbsolutePosition(position.x, position.y, position.z))
		.add(new AbsoluteRotation())
		.add(new SpriteID(spriteID))
		.add(new SpriteLayer(SpriteLayer.OBJECTS0))
		.add(new CullDistance(
				root.getFloat("cullradius", 1f),
				root.getFloat("cullradius_offset_x", 0f),
				root.getFloat("cullradius_offset_y", 0f))
		);
		
		return e;
	}
	
	public static int createRandomTerrainTile(Vector3 v, World world) {
		float heightField[][] = new float[11][11];
		
		for (int i = 0; i < heightField.length; i++) {
			for (int j = 0; j < heightField[0].length; j++) {
				heightField[i][j] = MathUtils.random(0f,1f);
			}
		}
		
		return createTerrainTile(heightField, 25f, v, world);
	}
	
	public static int createTerrainTile(float heightField[][], float cellSize, Vector3 position, World world) {
		
		int e = world.create();
		
		float terrainSizeX = cellSize*0.5f*(heightField[0].length	- 1);
		float terrainSizeY = cellSize*0.5f*(heightField.length	- 1);
		
		world.edit(e)
		.add(new AbsolutePosition(position.x, position.y, position.z))
		.add(new TerrainHeightField(heightField,cellSize))
		.add(new CullDistance(Math.max(terrainSizeX,terrainSizeY),terrainSizeX, terrainSizeY));

		return e;
	}

	/**
	 * Adds a house to the {@link World}
	 * @param name The type name of object
	 * @param position The world position of this object
	 * @param layer The height layer of this object (->render-order)
	 * @param world The world to add this object to
	 * @return The entity id or if failed -1
	 */
	public static int createStaticObject(String name, Vector3 position, int layer, World world) {
		
		JSONPropertiesHandler properties = new JSONPropertiesHandler("assets/data/object/"+name+".json");
		
		if(!properties.exists())
			return -1;
		
		JsonValue root = properties.get();
		
		int spriteID = AtlasHandler.getIdFromName(root.getString("sprite"));
		
		int e = world.create();
		
		EntityEdit ed = world.edit(e);
		
		ed.add(new AbsolutePosition(position.x, position.y, position.z))
		.add(new SpriteID(spriteID))
		.add(new SpriteLayer(layer))
		.add(new CullDistance(
				root.getFloat("cullradius", 256f),
				root.getFloat("cullradius_offset_x", 0f),
				root.getFloat("cullradius_offset_y", 0f))
		);
		
		if(root.has("collisionradius")) {
			ed.add(new CollisionRadius(root.getFloat("collisionradius", 0f)));
		}

		return e;
	}

	/**
	 * Adds a static object to the {@link World}
	 * @param name The type name of object
	 * @param position The world position of this object
	 * @param world The world to add this object to
	 * @return The entity id or if failed -1
	 */
	public static int createStaticObject(String name, Vector3 position, World world) {
		
		int e = createStaticObject(name, position, SpriteLayer.OBJECTS0, world);
		
		return e;
	}

	/**
	 * Adds a tracer effect to the {@link World}
	 * @param name The type name of effect
	 * @param position The initial world position of this effect
	 * @param target The target position of this tracer
	 * @param world The world to add this object to
	 * @return The entity id or if failed -1
	 */
	public static int createTracer(String name, Vector3 position, Vector3 target, Vector3 velocity, World world) {

		JSONPropertiesHandler properties = new JSONPropertiesHandler("assets/data/fx/"+name+".json");
		
		if(!properties.exists())
			return -1;
		
		JsonValue root = properties.get();
		
		int spriteID = AtlasHandler.getIdFromName(root.getString("sprite"));
		
		float angle = Math2D.atan2d(target.y-position.y, target.x-position.x);
		
		int e = world.create();
		
		world.edit(e)
		.add(new AbsolutePosition(position.x, position.y, position.z))
		.add(new AbsoluteRotation(angle))
		.add(new Velocity(velocity.x, velocity.y, velocity.z))
		.add(new TracerTarget(target.x, target.y, target.z).setSpread(position.dst(target)*0.005f))
		.add(new SpriteID(spriteID))
		.add(new SpriteDrawMode().additiveBlend())
		.add(new SpriteLayer(SpriteLayer.OBJECTS0))
		.add(new SpriteFrame(0, root.getInt("frames", 1), root.getFloat("interval", 1000f/60f), false))
		.add(new CullDistance(
				root.getFloat("cullradius", 64f),
				root.getFloat("cullradius_offset_x", 0f),
				root.getFloat("cullradius_offset_y", 0f))
		);
		
		return e;
	}
	
	/**
	 * Adds an animated effect to the {@link World}
	 * @param name The type name of effect
	 * @param position The initial world position of this effect
	 * @param target The target position of this tracer
	 * @param world The world to add this object to
	 * @return The entity id or if failed -1
	 */
	public static int createAnimatedEffect(String name, Vector3 position, float rotation, boolean additive, World world) {
		
		JSONPropertiesHandler properties = new JSONPropertiesHandler("assets/data/fx/"+name+".json");
		
		if(!properties.exists())
			return -1;
		
		JsonValue root = properties.get();
		
		int spriteID = AtlasHandler.getIdFromName(root.getString("sprite"));
		int spriteArraySize = AtlasHandler.getSharedSpritesFromId(spriteID).size;
		
		int e = world.create();
		
		float velocity = 1f;
		
		Vector3 rot_offset = new Vector3(0f,-2.75f,1.2f).rotate(Vector3.Z, rotation);
		
		EntityEdit ed = world.edit(e)
		.add(new AbsolutePosition(position.x + rot_offset.x, position.y + rot_offset.y, position.z + rot_offset.z))
		.add(new Velocity(MathUtils.random(-velocity,velocity), MathUtils.random(-velocity,velocity), MathUtils.random(0f,0f)))
		.add(new AbsoluteRotation(0f))
		.add(new SpriteID(spriteID))
		.add(new SpriteLayer(SpriteLayer.OBJECTS0))
		.add(new SpriteFrame(0, spriteArraySize, root.getFloat("interval", 1000f/60f), false))
		.add(new CullDistance(
				root.getFloat("cullradius", 64f),
				root.getFloat("cullradius_offset_x", 0f),
				root.getFloat("cullradius_offset_y", 0f))
		);
		
		if(additive) {
			ed.add(new SpriteDrawMode().additiveBlend());
		}

		
		return e;
	}
}
