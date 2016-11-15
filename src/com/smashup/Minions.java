package com.smashup;

public class Minions implements Card{

	String name;
	Type type;
	int power;
	int powerCounters;
	int playerID;
	public enum Type {
		SUPREME_OVERLORD,
		INVADER,
		SCOUT,
		COLLECTOR,

		NUKEBOT,
		WARBOT,
		HOVERBOT,
		ZAPBOT,
		MICROBOT_FIXER,
		MICROBOT_GUARD,
		MICROBOT_RECLAIMER,
		MICROBOT_ALPHA,
		MICROBOT_ARCHIVE
	}

	public Minions(int id,Type type) {
		playerID = id;
		this.type = type;
		switch(type) {
			case MICROBOT_ARCHIVE:
			case MICROBOT_ALPHA:
			case MICROBOT_RECLAIMER:
			case MICROBOT_GUARD:
			case MICROBOT_FIXER:
				power = 1;
				break;
			case COLLECTOR:
			case ZAPBOT:
				power = 2;
				break;
			case INVADER:
			case SCOUT:
			case HOVERBOT:
				power = 3;
				break;
			case WARBOT:
				power = 4;
				break;
			case SUPREME_OVERLORD:
			case NUKEBOT:
				power = 5;
				break;
			default:
				power = 0;
		}
		name = type.name().toLowerCase();
	}

	public String getName() {
		return name;
	}

}
