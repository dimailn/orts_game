package de.vatterger.game.components.gameobject;

import com.artemis.Component;

public class Team extends Component {
	public byte team;

	public Team(byte team) {
		this.team = team;
	}
}
