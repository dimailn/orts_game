package de.vatterger.techdemo.network.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.IntArraySerializer;

import de.vatterger.techdemo.network.packets.client.EntityAckPacket;

public class EntityAckPaketSerializer extends Serializer<EntityAckPacket> {

	private IntArraySerializer ias = new IntArraySerializer();
	
	@Override
	public EntityAckPacket read(Kryo kryo, Input in, Class<EntityAckPacket> oclass) {
		EntityAckPacket crl = new EntityAckPacket(kryo.readObject(in, int[].class, ias));
		return crl;
	}

	@Override
	public void write(Kryo kryo, Output out, EntityAckPacket crl) {
		kryo.writeObject(out, crl.received, ias);
	}

}
