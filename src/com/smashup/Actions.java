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

		AUGMENTATION,
		HOWL,
		NATURAL_SELECTION,
		UPGRADE,
		TOOTH_AND_CLAW_AND_GUNS,
		SURVIVAL_OF_THE_FITTEST,
		WILDLIFE_PRESERVE,
		RAMPAGE,

		ASSASINATION,
		DISGUISE,
		HIDDEN_NINJA,
		INFILTRATE,
		POISON,
		SEEING_STARS,
		SMOKE_BOMB,
		WAY_OF_DECEPTION,

		BROADSIDE,
		CANNON,
		DINGHY,
		FULL_SAIL,
		POWDERKEG,
		SHANGHAI,
		SEA_DOGS,
		SWASHBUCKLING,

		TECH_CENTER,

		BLOCK_THE_PATH,
		DISENCHANT,
		ENSHROUDING_MIST,
		FLAME_TRAP,
		HIDEOUT,
		MARK_OF_SLEEP,
		PAY_THE_PIPER,
		TAKE_THE_SHINIES,

		MASS_ENCHANTMENT,
		MYSTIC_STUDIES,
		PORTAL,
		SACRIFICE,
		SCRY,
		SUMMON,
		TIME_LOOP,
		WINDS_OF_CHANGE,

		GRAVE_ROBBING,
		LEND_A_HAND,
		MALL_CRAWL,
		NOT_ENOUGH_BULLETS,
		OUTBREAK,
		OVERRUN,
		THEY_KEEP_COMING,
		THEYRE_COMING_TO_GET_YOU
	}

	public Actions(int id,Type type) {
		playerID = id;
		this.type = type;
		name = type.name().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return name.toUpperCase().replace('_',' ');
	}
}
