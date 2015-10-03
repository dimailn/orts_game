package de.vatterger.entitysystem.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;

import de.vatterger.entitysystem.components.DataBucket;
import de.vatterger.entitysystem.components.Flag;
import de.vatterger.entitysystem.components.RemoteMaster;
import de.vatterger.entitysystem.components.ViewFrustum;
import de.vatterger.entitysystem.gridmapservice.BitFlag;
import de.vatterger.entitysystem.gridmapservice.GridMapService;
import de.vatterger.entitysystem.networkmessages.RemoteMasterUpdate;

public class RemoteMasterSendProcessor extends EntityProcessingSystem {

	private ComponentMapper<DataBucket> dbm;
	private ComponentMapper<RemoteMaster> rmm;
	private ComponentMapper<ViewFrustum> vfm;
	private ComponentMapper<Flag> fm;
	
	private Bag<Integer> flyweightEntities = new Bag<Integer>(256);

	@SuppressWarnings("unchecked")
	public RemoteMasterSendProcessor() {
		super(Aspect.getAspectForAll(DataBucket.class, ViewFrustum.class));
	}

	@Override
	protected void initialize() {
		dbm = world.getMapper(DataBucket.class);
		rmm = world.getMapper(RemoteMaster.class);
		vfm = world.getMapper(ViewFrustum.class);
		fm = world.getMapper(Flag.class);
	}
	
	@Override
	protected void process(Entity e) {
		DataBucket bucket = dbm.get(e);
		ViewFrustum vf = vfm.get(e);
		
		if(bucket.isEmpty()) {
			GridMapService.getEntities(new BitFlag(BitFlag.NETWORKED), vf.rect, flyweightEntities);
			for (int i = 0; i < flyweightEntities.size(); i++) {
				Entity sendEntity = world.getEntity(flyweightEntities.get(i));
				if(fm.get(sendEntity).flag.isSuperSetOf(BitFlag.ACTIVE)) {
					RemoteMaster rm = rmm.get(sendEntity);
					rm.components.trim();
					RemoteMasterUpdate rmu = new RemoteMasterUpdate(sendEntity.id, true, rm.components.getData());
					bucket.addData(rmu, true);
				} else {
					RemoteMasterUpdate rmu = new RemoteMasterUpdate(sendEntity.id, true, new Object[0]); 
					bucket.addData(rmu, true);
				}
			}
			flyweightEntities.clear();
		}
	}
}
