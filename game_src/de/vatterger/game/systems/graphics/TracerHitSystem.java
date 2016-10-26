package de.vatterger.game.systems.graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import de.vatterger.engine.handler.unit.UnitHandler;
import de.vatterger.game.components.gameobject.AbsolutePosition;
import de.vatterger.game.components.gameobject.RemoveTimed;
import de.vatterger.game.components.gameobject.SpriteLayer;
import de.vatterger.game.components.gameobject.TracerTarget;
import de.vatterger.game.components.gameobject.Velocity;

public class TracerHitSystem extends IteratingSystem {

	private ComponentMapper<AbsolutePosition> pm;
	private ComponentMapper<TracerTarget> tm;
	private ComponentMapper<Velocity> vm;
	
	private Vector3 v0 = new Vector3();
	private Vector3 v1 = new Vector3();
	
	public TracerHitSystem() {
		super(Aspect.all(AbsolutePosition.class,Velocity.class, TracerTarget.class));
	}
	
	@Override
	protected void inserted(int e) {
		v0.set(pm.get(e).position);
		v1.set(tm.get(e).targetPos);
		tm.get(e).lastDist = v0.dst(v1);
	}
	
	@Override
	protected void process(int e) {
		TracerTarget tc = tm.get(e);

		v0.set(pm.get(e).position);
		v1.set(tc.targetPos);
		float distance = v0.dst(v1);
		
		if(distance > tc.lastDist || distance - vm.get(e).velocity.len()*world.delta < 0) {
			world.delete(e);
			v0.set(tc.targetPos).add(MathUtils.random(-tc.spreadX, tc.spreadX), MathUtils.random(-tc.spreadY, tc.spreadY), 0f);
			int mud_decal = UnitHandler.createStaticObject("mud_decal", v0, SpriteLayer.GROUND1, world);
			world.edit(mud_decal).add(new RemoveTimed(1f));
		} else {
			tc.lastDist = distance;
		}
	}
}