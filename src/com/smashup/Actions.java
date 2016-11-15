package com.smashup;
import java.util.ArrayList;

public class Actions implements Card {

	int playerID;
	Type type;
	String name;
	enum Type {
		ABDUCTION,
		BEAM_UP,
		CROP_CIRCLES,
		DISINTEGRATOR,
		INVASION,
		JAMMED_SIGNAL,
		PROBE,
		TERRAFORMING,

		TECH_CENTER
	}

	public Actions(int id,Type type) {
		playerID = id;
		this.type = type;
		name = type.name().toLowerCase();
	}

	public String getName() {
		return name;
	}

}
