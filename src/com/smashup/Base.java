package com.smashup;
import java.util.ArrayList;

public class Base {

	enum Type {
		THE_HOMEWORLD,
		THE_MOTHERSHIP,
		JUNGLE_OASIS,
		TAR_PITS,
		NINJA_DOJO,
		TEMPLE_OF_GOJU,
		THE_GREY_OPAL,
		TORTUGA,
		FACTORY_436_1337,
		THE_CENTRAL_BRAIN,
		CAVE_OF_SHINIES,
		MUSHROOM_KINGDOM,
		SCHOOL_OF_WIZARDRY,
		THE_GREAT_LIBRARY,
		EVANS_CITY_CEMETARY,
		RHODES_PLAZA_MALL
	}
	Type type;
	String name;
	int breakpoint,vp1,vp2,vp3;
	static ArrayList<Base> allBases;

	static {
		allBases = new ArrayList<Base>();
		allBases.add(new Base(Base.Type.THE_HOMEWORLD,23,4,2,1));
		allBases.add(new Base(Base.Type.THE_MOTHERSHIP,20,4,2,1));
		allBases.add(new Base(Base.Type.JUNGLE_OASIS,12,2,0,0));
		allBases.add(new Base(Base.Type.TAR_PITS,16,4,3,2));
		allBases.add(new Base(Base.Type.NINJA_DOJO,18,2,3,2));
		allBases.add(new Base(Base.Type.TEMPLE_OF_GOJU,18,2,3,2));
		allBases.add(new Base(Base.Type.THE_GREY_OPAL,17,3,1,1));
		allBases.add(new Base(Base.Type.TORTUGA,21,4,3,2));
		allBases.add(new Base(Base.Type.FACTORY_436_1337,25,2,2,1));
		allBases.add(new Base(Base.Type.THE_CENTRAL_BRAIN,19,4,2,1));
		allBases.add(new Base(Base.Type.CAVE_OF_SHINIES,23,4,2,1));
		allBases.add(new Base(Base.Type.MUSHROOM_KINGDOM,20,5,3,2));
		allBases.add(new Base(Base.Type.SCHOOL_OF_WIZARDRY,20,3,2,1));
		allBases.add(new Base(Base.Type.THE_GREAT_LIBRARY,22,4,2,1));
		allBases.add(new Base(Base.Type.EVANS_CITY_CEMETARY,20,5,3,2));
		allBases.add(new Base(Base.Type.RHODES_PLAZA_MALL,24,0,0,0));
	}

	public Base(Type t,int bp,int v1,int v2,int v3) {
		type = t;
		name = t.name().replace('_',' ');
		breakpoint = bp;
		vp1 = v1;
		vp2 = v2;
		vp3 = v3;
	}
}
