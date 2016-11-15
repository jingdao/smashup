package com.smashup;
import java.util.ArrayList;

public class Player {

	enum DeckType {
		ALIENS,
		DINOSAURS,
		NINJAS,
		PIRATES,
		ROBOTS,
		TRICKSTERS,
		WIZARDS,
		ZOMBIES
	}
	int playerID;
	int vp;
	ArrayList<Card> hand;
	ArrayList<Card> deck;
	ArrayList<Card> discard;
	String name;

	public Player(int id,DeckType d1,DeckType d2) {
		playerID = id;
		vp = 0;
		hand = new ArrayList<Card>();
		discard = new ArrayList<Card>();
		deck = new ArrayList<Card>();
		name = d1.name()+"+"+d2.name();
		switch (d1) {
			case ALIENS:
				deck.add(new Minions(id,Minions.Type.SUPREME_OVERLORD));
				deck.add(new Minions(id,Minions.Type.INVADER));
				deck.add(new Minions(id,Minions.Type.INVADER));
				deck.add(new Minions(id,Minions.Type.SCOUT));
				deck.add(new Minions(id,Minions.Type.SCOUT));
				deck.add(new Minions(id,Minions.Type.SCOUT));
				deck.add(new Minions(id,Minions.Type.COLLECTOR));
				deck.add(new Minions(id,Minions.Type.COLLECTOR));
				deck.add(new Minions(id,Minions.Type.COLLECTOR));
				deck.add(new Minions(id,Minions.Type.COLLECTOR));
				deck.add(new Actions(id,Actions.Type.ABDUCTION));
				deck.add(new Actions(id,Actions.Type.BEAM_UP));
				deck.add(new Actions(id,Actions.Type.BEAM_UP));
				deck.add(new Actions(id,Actions.Type.CROP_CIRCLES));
				deck.add(new Actions(id,Actions.Type.DISINTEGRATOR));
				deck.add(new Actions(id,Actions.Type.DISINTEGRATOR));
				deck.add(new Actions(id,Actions.Type.INVASION));
				deck.add(new Actions(id,Actions.Type.JAMMED_SIGNAL));
				deck.add(new Actions(id,Actions.Type.PROBE));
				deck.add(new Actions(id,Actions.Type.TERRAFORMING));
				break;
			case ROBOTS:
				deck.add(new Minions(id,Minions.Type.NUKEBOT));
				deck.add(new Minions(id,Minions.Type.WARBOT));
				deck.add(new Minions(id,Minions.Type.WARBOT));
				deck.add(new Minions(id,Minions.Type.HOVERBOT));
				deck.add(new Minions(id,Minions.Type.HOVERBOT));
				deck.add(new Minions(id,Minions.Type.HOVERBOT));
				deck.add(new Minions(id,Minions.Type.ZAPBOT));
				deck.add(new Minions(id,Minions.Type.ZAPBOT));
				deck.add(new Minions(id,Minions.Type.ZAPBOT));
				deck.add(new Minions(id,Minions.Type.ZAPBOT));
				deck.add(new Minions(id,Minions.Type.MICROBOT_FIXER));
				deck.add(new Minions(id,Minions.Type.MICROBOT_FIXER));
				deck.add(new Minions(id,Minions.Type.MICROBOT_GUARD));
				deck.add(new Minions(id,Minions.Type.MICROBOT_GUARD));
				deck.add(new Minions(id,Minions.Type.MICROBOT_RECLAIMER));
				deck.add(new Minions(id,Minions.Type.MICROBOT_RECLAIMER));
				deck.add(new Minions(id,Minions.Type.MICROBOT_ALPHA));
				deck.add(new Minions(id,Minions.Type.MICROBOT_ARCHIVE));
				deck.add(new Actions(id,Actions.Type.TECH_CENTER));
				deck.add(new Actions(id,Actions.Type.TECH_CENTER));
				break;
		}
	}

	public void drawCards(int num) {
		for (int i=0;i<num;i++) {
			Card card = deck.remove(deck.size() - 1);
			hand.add(card);
		}
	}
}
