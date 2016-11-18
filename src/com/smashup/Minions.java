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

		KING_REX,
		LASERATOPS,
		ARMOR_STEGO,
		WAR_RAPTOR,

		NINJA_MASTER,
		TIGER_ASSASSIN,
		SHINOBI,
		NINJA_ACOLYTE,

		PIRATE_KING,
		BUCCANEER,
		SAUCY_WENCH,
		FIRST_MATE,

		NUKEBOT,
		WARBOT,
		HOVERBOT,
		ZAPBOT,
		MICROBOT_FIXER,
		MICROBOT_GUARD,
		MICROBOT_RECLAIMER,
		MICROBOT_ALPHA,
		MICROBOT_ARCHIVE,

		LEPRECHAUN,
		BROWNIE,
		GNOME,
		GREMLIN,

		ARCHMAGE,
		CHRONOMAGE,
		ENCHANTRESS,
		NEOPHYTE,

		ZOMBIE_LORD,
		GRAVE_DIGGER,
		TENACIOUS_Z,
		WALKER
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
			case WAR_RAPTOR:
			case NINJA_ACOLYTE:
			case FIRST_MATE:
			case ZAPBOT:
			case GREMLIN:
			case ENCHANTRESS:
			case NEOPHYTE:
			case TENACIOUS_Z:
			case WALKER:
				power = 2;
				break;
			case INVADER:
			case SCOUT:
			case ARMOR_STEGO:
			case SHINOBI:
			case SAUCY_WENCH:
			case HOVERBOT:
			case GNOME:
			case CHRONOMAGE:
				power = 3;
				break;
			case LASERATOPS:
			case TIGER_ASSASSIN:
			case BUCCANEER:
			case WARBOT:
			case BROWNIE:
			case ARCHMAGE:
			case GRAVE_DIGGER:
				power = 4;
				break;
			case SUPREME_OVERLORD:
			case NINJA_MASTER:
			case PIRATE_KING:
			case NUKEBOT:
			case LEPRECHAUN:
			case ZOMBIE_LORD:
				power = 5;
				break;
			case KING_REX:
				power = 7;
				break;
			default:
				power = 0;
		}
		name = type.name().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return name.toUpperCase().replace('_',' ');
	}

}
