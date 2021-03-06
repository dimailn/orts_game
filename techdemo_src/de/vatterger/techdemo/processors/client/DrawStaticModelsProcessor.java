package de.vatterger.techdemo.processors.client;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import de.vatterger.engine.handler.asset.ModelHandler;
import de.vatterger.techdemo.components.client.InterpolatedPosition;
import de.vatterger.techdemo.components.client.InterpolatedRotation;
import de.vatterger.techdemo.components.shared.G3DBModelId;
import de.vatterger.techdemo.components.shared.Inactive;
import de.vatterger.techdemo.components.shared.StaticModel;

@Wire
public class DrawStaticModelsProcessor extends EntityProcessingSystem {

	private ComponentMapper<InterpolatedPosition>	cpm;
	private ComponentMapper<InterpolatedRotation>	crm;
	private ComponentMapper<G3DBModelId>	gmim;
	
	private ModelBatch batch;
	private ModelCache cache;
	private Camera cam;
	private Environment env;

	private boolean needStaticModelRebuild = false;
	
	@SuppressWarnings("unchecked")
	public DrawStaticModelsProcessor(ModelBatch batch, Camera cam , Environment env) {
		super(Aspect.all(InterpolatedPosition.class, G3DBModelId.class, InterpolatedRotation.class, StaticModel.class).exclude(Inactive.class));
		
		this.batch = batch;
		this.cam = cam;
		this.env = env;

		cache = new ModelCache();
	}
	
	@Override
	public void inserted(Entity e) {
		needStaticModelRebuild = true;
	}
	
	@Override
	public void removed(Entity e) {
		needStaticModelRebuild = true;
	}
	
	@Override
	protected void begin() {
		if(needStaticModelRebuild)
			cache.begin(cam);
	}

	protected void process(Entity e) {
		if (needStaticModelRebuild) {
			ModelInstance instance = ModelHandler.getSharedInstanceByID(gmim.get(e).id);
			instance.nodes.first().translation.set(cpm.get(e).getInterpolatedValue());
			instance.nodes.first().rotation.set(new Vector3(0f, 0f, 1f), crm.get(e).getInterpolatedValue());
			instance.calculateTransforms();
			cache.add(instance);
		}
	}
	
	@Override
	protected void end() {
		if(needStaticModelRebuild){
			cache.end();
		}
		batch.begin(cam);
		batch.render(cache, env);
		batch.end();
		needStaticModelRebuild = false;
	}
}
