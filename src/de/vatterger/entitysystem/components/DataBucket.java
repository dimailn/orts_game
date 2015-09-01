package de.vatterger.entitysystem.components;

import java.util.LinkedList;
import java.util.Queue;

import com.artemis.Component;
import com.artemis.utils.Bag;

import de.vatterger.entitysystem.networkmessages.PacketBundle;

public class DataBucket extends Component {
	private Queue<Object> msg = new LinkedList<Object>();
	private Queue<Integer> msgSize = new LinkedList<Integer>();

	public DataBucket addData(Object o, int size){
		msg.add(o);
		msgSize.add(size);
		return this;
	}
	
	public void clearData() {
		msg.clear();
		msgSize.clear();
	}
	
	public Bag<PacketBundle> getPacketBundles(int size, int maxPackets) {
		Bag<PacketBundle> bundles = new Bag<PacketBundle>(1);
		PacketBundle bundle = new PacketBundle(size);
		while(!msg.isEmpty() && bundles.size() < maxPackets) {
			if(bundle.hasFreeBytes()) {
				bundle.add(msg.poll(), msgSize.poll());
			} else {
				bundle.packets.trim();
				bundles.add(bundle);
				bundle = new PacketBundle(size);
			}
		}
		bundles.trim();
		return bundles;
	}
}