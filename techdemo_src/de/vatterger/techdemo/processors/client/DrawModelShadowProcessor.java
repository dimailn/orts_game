package de.vatterger.techdemo.processors.client;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;

import de.vatterger.engine.handler.asset.ModelHandler;
import de.vatterger.engine.util.GameUtil;
import de.vatterger.techdemo.application.GameConstants;
import de.vatterger.techdemo.components.client.InterpolatedPosition;
import de.vatterger.techdemo.components.client.InterpolatedRotation;
import de.vatterger.techdemo.components.shared.G3DBModelId;
import de.vatterger.techdemo.components.shared.Inactive;
import de.vatterger.techdemo.components.shared.StaticModel;
import de.vatterger.techdemo.lights.DirectionalShadowLight;

public class DrawModelShadowProcessor extends IteratingSystem {

	private ComponentMapper<InterpolatedPosition>	cpm;
	private ComponentMapper<InterpolatedRotation>	crm;
	private ComponentMapper<G3DBModelId>	gmim;
	
	private ModelBatch shadowBatch;
	private DirectionalShadowLight shadowLight;
	private Camera cam;
	
	@SuppressWarnings("unchecked")
	public DrawModelShadowProcessor(DirectionalShadowLight shadowLight, Camera cam) {
		super(Aspect.all(InterpolatedPosition.class, G3DBModelId.class, InterpolatedRotation.class).exclude(Inactive.class, StaticModel.class));
		this.shadowLight = shadowLight;
		this.cam = cam;
		shadowBatch = new ModelBatch(new DepthShaderProvider());
	}
	
	@Override
	protected void begin() {
		shadowLight.begin(GameUtil.intersectMouseGroundPlane(cam, Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0).add(0f, 0f, 32f), shadowLight.direction);
		shadowBatch.begin(shadowLight.getCamera());
	}

	protected void process(int e) {
		if (cam.position.dst(cpm.get(e).getInterpolatedValue()) < GameConstants.NET_SYNC_THRESHOLD) {
			ModelInstance instance = ModelHandler.getSharedInstanceByID(gmim.get(e).id);
			Node node = instance.nodes.first();
			node.translation.set(cpm.get(e).getInterpolatedValue());
			node.rotation.set(new Vector3(0f, 0f, 1f), crm.get(e).getInterpolatedValue());
			instance.calculateTransforms();
			shadowBatch.render(instance.getRenderable(new Renderable(), node));
		}
	}
	
	@Override
	protected void end() {
		shadowBatch.end();
		shadowLight.end();
	}
}
