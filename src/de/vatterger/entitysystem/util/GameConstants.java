package de.vatterger.entitysystem.util;

public class GameConstants {
	
	private GameConstants(){}

	/**The maximum x and y values that the playable area extends to from [0,0]*/
	public final static int XY_BOUNDS = 1000;

	public final static int SLIME_ENTITYCOUNT = 1000;
	
	public final static int EDIBLE_ENTITYCOUNT = 2000;

	public final static int EDIBLE_CREATE_PER_TICK = 0;

	public final static int EXPECTED_ENTITYCOUNT = EDIBLE_ENTITYCOUNT + SLIME_ENTITYCOUNT;

	public final static float SLIME_INITIAL_SIZE = 2f;

	public final static float SMALL_EDIBLE_SIZE = 0.5f;

	public final static float minZoom = 0.0001f, maxZoom = 100000f;

	/**Local server IP-Address*/
	public static final String LOCAL_SERVER_IP = "localhost";

	/**Internet server IP-Address*/
	public static final String NET_SERVER_IP = null;

	/**Output buffer size in bytes*/
	public static final int QUEUE_BUFFER_SIZE = 1600; // Bytes

	/**Object graph buffer size in bytes*/
	public static final int OBJECT_BUFFER_SIZE = 1400; // Bytes
	
	/**(Probably) Optimal packet-size to use over the internet*/
	public static final int PACKETSIZE_INTERNET = 1200;// Bytes
	
	/**Port to bind TCP and UDP*/
	public static final int NET_PORT = 26000;

	/**Packet count per tick per player*/
	public static final int PACKETS_PER_TICK = 1;
	
	/**After this time has elapsed without an update, the respective entity is deleted by the client*/
	public static final float ENTITY_UPDATE_TIMEOUT = 5f;// Seconds
	
	public static final float INACTIVE_DELETION_DELAY = 10f;
}
