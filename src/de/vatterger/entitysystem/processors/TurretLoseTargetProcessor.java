package de.vatterger.entitysystem.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

import de.vatterger.entitysystem.components.server.ServerPosition;
import de.vatterger.entitysystem.components.server.ServerTurretRotation;
import de.vatterger.entitysystem.components.shared.Inactive;
import de.vatterger.entitysystem.components.shared.TurretIdle;
import de.vatterger.entitysystem.components.shared.TurretTarget;
import de.vatterger.entitysystem.components.shared.ViewRange;

@Wire
public class TurretLoseTargetProcessor extends EntityProcessingSystem {

	ComponentMapper<ServerPosition> spm;
	ComponentMapper<TurretTarget> ttm;
	ComponentMapper<ViewRange> vrm;

	@SuppressWarnings("unchecked")
	public TurretLoseTargetProcessor() {
		super(Aspect.getAspectForAll(ServerPosition.class, ServerTurretRotation.class, ViewRange.class, TurretTarget.class).exclude(Inactive.class, TurretIdle.class));
	}

	protected void process(Entity e) {
		ServerPosition spc = spm.get(e);
		ViewRange vrc = vrm.get(e);
		TurretTarget ttc = ttm.get(e);
		
		if(spc.pos.dst(spm.get(world.getEntity(ttc.target)).pos) > vrc.range)
			e.edit().remove(ttc).add(new TurretIdle());
	}
}