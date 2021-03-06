package de.vatterger.techdemo.components.shared;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;

import de.vatterger.techdemo.components.shared.ChildGroup;

public final class ChildGroup extends Component {

	public Bag<Entity> children = new Bag<Entity>(1);

	public ChildGroup() {
	}
	
	public ChildGroup add(Entity e) {
		children.add(e);
		return this;
	}

	public ChildGroup remove(Entity e) {
		children.remove(e);
		return this;
	}
}
