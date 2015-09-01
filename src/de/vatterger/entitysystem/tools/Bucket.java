package de.vatterger.entitysystem.tools;

import com.artemis.utils.Bag;

public class Bucket<T> extends Bag<T>{
	
	public Bucket() {
		this(32);
	}
	public Bucket(int capacity) {
		super(capacity);
	}
}