package com.smashup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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
	int minionsLeft;
	int actionsLeft;
	ArrayList<Card> hand;
	ArrayList<Card> deck;
	ArrayList<Card> discard;
	String name;
	Random random = new Random();

	public Player(int id,DeckType d1,DeckType d2) {
		playerID = id;
		vp = 0;
		hand = new ArrayList<Card>();
		discard = new ArrayList<Card>();
		deck = new ArrayList<Card>();
		name = d1.name()+"+"+d2.name();
		DeckType[] decks = {d1,d2};
		for (DeckType d : decks) {
			switch (d) {
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
				case DINOSAURS:
					deck.add(new Minions(id,Minions.Type.KING_REX));
					deck.add(new Minions(id,Minions.Type.LASERATOPS));
					deck.add(new Minions(id,Minions.Type.LASERATOPS));
					deck.add(new Minions(id,Minions.Type.ARMOR_STEGO));
					deck.add(new Minions(id,Minions.Type.ARMOR_STEGO));
					deck.add(new Minions(id,Minions.Type.ARMOR_STEGO));
					deck.add(new Minions(id,Minions.Type.WAR_RAPTOR));
					deck.add(new Minions(id,Minions.Type.WAR_RAPTOR));
					deck.add(new Minions(id,Minions.Type.WAR_RAPTOR));
					deck.add(new Minions(id,Minions.Type.WAR_RAPTOR));
					deck.add(new Actions(id,Actions.Type.AUGMENTATION));
					deck.add(new Actions(id,Actions.Type.AUGMENTATION));
					deck.add(new Actions(id,Actions.Type.HOWL));
					deck.add(new Actions(id,Actions.Type.HOWL));
					deck.add(new Actions(id,Actions.Type.NATURAL_SELECTION));
					deck.add(new Actions(id,Actions.Type.UPGRADE));
					deck.add(new Actions(id,Actions.Type.TOOTH_AND_CLAW_AND_GUNS));
					deck.add(new Actions(id,Actions.Type.SURVIVAL_OF_THE_FITTEST));
					deck.add(new Actions(id,Actions.Type.WILDLIFE_PRESERVE));
					deck.add(new Actions(id,Actions.Type.RAMPAGE));
					break;
				case NINJAS:
					deck.add(new Minions(id,Minions.Type.NINJA_MASTER));
					deck.add(new Minions(id,Minions.Type.TIGER_ASSASSIN));
					deck.add(new Minions(id,Minions.Type.TIGER_ASSASSIN));
					deck.add(new Minions(id,Minions.Type.SHINOBI));
					deck.add(new Minions(id,Minions.Type.SHINOBI));
					deck.add(new Minions(id,Minions.Type.SHINOBI));
					deck.add(new Minions(id,Minions.Type.NINJA_ACOLYTE));
					deck.add(new Minions(id,Minions.Type.NINJA_ACOLYTE));
					deck.add(new Minions(id,Minions.Type.NINJA_ACOLYTE));
					deck.add(new Minions(id,Minions.Type.NINJA_ACOLYTE));
					deck.add(new Actions(id,Actions.Type.ASSASINATION));
					deck.add(new Actions(id,Actions.Type.DISGUISE));
					deck.add(new Actions(id,Actions.Type.HIDDEN_NINJA));
					deck.add(new Actions(id,Actions.Type.INFILTRATE));
					deck.add(new Actions(id,Actions.Type.INFILTRATE));
					deck.add(new Actions(id,Actions.Type.POISON));
					deck.add(new Actions(id,Actions.Type.SEEING_STARS));
					deck.add(new Actions(id,Actions.Type.SEEING_STARS));
					deck.add(new Actions(id,Actions.Type.SMOKE_BOMB));
					deck.add(new Actions(id,Actions.Type.WAY_OF_DECEPTION));
					break;
				case PIRATES:
					deck.add(new Minions(id,Minions.Type.PIRATE_KING));
					deck.add(new Minions(id,Minions.Type.BUCCANEER));
					deck.add(new Minions(id,Minions.Type.BUCCANEER));
					deck.add(new Minions(id,Minions.Type.SAUCY_WENCH));
					deck.add(new Minions(id,Minions.Type.SAUCY_WENCH));
					deck.add(new Minions(id,Minions.Type.SAUCY_WENCH));
					deck.add(new Minions(id,Minions.Type.FIRST_MATE));
					deck.add(new Minions(id,Minions.Type.FIRST_MATE));
					deck.add(new Minions(id,Minions.Type.FIRST_MATE));
					deck.add(new Minions(id,Minions.Type.FIRST_MATE));
					deck.add(new Actions(id,Actions.Type.BROADSIDE));
					deck.add(new Actions(id,Actions.Type.BROADSIDE));
					deck.add(new Actions(id,Actions.Type.CANNON));
					deck.add(new Actions(id,Actions.Type.DINGHY));
					deck.add(new Actions(id,Actions.Type.DINGHY));
					deck.add(new Actions(id,Actions.Type.FULL_SAIL));
					deck.add(new Actions(id,Actions.Type.POWDERKEG));
					deck.add(new Actions(id,Actions.Type.SHANGHAI));
					deck.add(new Actions(id,Actions.Type.SEA_DOGS));
					deck.add(new Actions(id,Actions.Type.SWASHBUCKLING));
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
				case TRICKSTERS:
					deck.add(new Minions(id,Minions.Type.LEPRECHAUN));
					deck.add(new Minions(id,Minions.Type.BROWNIE));
					deck.add(new Minions(id,Minions.Type.BROWNIE));
					deck.add(new Minions(id,Minions.Type.GNOME));
					deck.add(new Minions(id,Minions.Type.GNOME));
					deck.add(new Minions(id,Minions.Type.GNOME));
					deck.add(new Minions(id,Minions.Type.GREMLIN));
					deck.add(new Minions(id,Minions.Type.GREMLIN));
					deck.add(new Minions(id,Minions.Type.GREMLIN));
					deck.add(new Minions(id,Minions.Type.GREMLIN));
					deck.add(new Actions(id,Actions.Type.BLOCK_THE_PATH));
					deck.add(new Actions(id,Actions.Type.DISENCHANT));
					deck.add(new Actions(id,Actions.Type.DISENCHANT));
					deck.add(new Actions(id,Actions.Type.ENSHROUDING_MIST));
					deck.add(new Actions(id,Actions.Type.ENSHROUDING_MIST));
					deck.add(new Actions(id,Actions.Type.FLAME_TRAP));
					deck.add(new Actions(id,Actions.Type.HIDEOUT));
					deck.add(new Actions(id,Actions.Type.MARK_OF_SLEEP));
					deck.add(new Actions(id,Actions.Type.PAY_THE_PIPER));
					deck.add(new Actions(id,Actions.Type.TAKE_THE_SHINIES));
					break;
				case WIZARDS:
					deck.add(new Minions(id,Minions.Type.ARCHMAGE));
					deck.add(new Minions(id,Minions.Type.CHRONOMAGE));
					deck.add(new Minions(id,Minions.Type.CHRONOMAGE));
					deck.add(new Minions(id,Minions.Type.ENCHANTRESS));
					deck.add(new Minions(id,Minions.Type.ENCHANTRESS));
					deck.add(new Minions(id,Minions.Type.ENCHANTRESS));
					deck.add(new Minions(id,Minions.Type.NEOPHYTE));
					deck.add(new Minions(id,Minions.Type.NEOPHYTE));
					deck.add(new Minions(id,Minions.Type.NEOPHYTE));
					deck.add(new Minions(id,Minions.Type.NEOPHYTE));
					deck.add(new Actions(id,Actions.Type.MASS_ENCHANTMENT));
					deck.add(new Actions(id,Actions.Type.MYSTIC_STUDIES));
					deck.add(new Actions(id,Actions.Type.MYSTIC_STUDIES));
					deck.add(new Actions(id,Actions.Type.PORTAL));
					deck.add(new Actions(id,Actions.Type.SACRIFICE));
					deck.add(new Actions(id,Actions.Type.SCRY));
					deck.add(new Actions(id,Actions.Type.SUMMON));
					deck.add(new Actions(id,Actions.Type.SUMMON));
					deck.add(new Actions(id,Actions.Type.TIME_LOOP));
					deck.add(new Actions(id,Actions.Type.WINDS_OF_CHANGE));
					break;
				case ZOMBIES:
					deck.add(new Minions(id,Minions.Type.ZOMBIE_LORD));
					deck.add(new Minions(id,Minions.Type.GRAVE_DIGGER));
					deck.add(new Minions(id,Minions.Type.GRAVE_DIGGER));
					deck.add(new Minions(id,Minions.Type.TENACIOUS_Z));
					deck.add(new Minions(id,Minions.Type.TENACIOUS_Z));
					deck.add(new Minions(id,Minions.Type.TENACIOUS_Z));
					deck.add(new Minions(id,Minions.Type.WALKER));
					deck.add(new Minions(id,Minions.Type.WALKER));
					deck.add(new Minions(id,Minions.Type.WALKER));
					deck.add(new Minions(id,Minions.Type.WALKER));
					deck.add(new Actions(id,Actions.Type.GRAVE_ROBBING));
					deck.add(new Actions(id,Actions.Type.GRAVE_ROBBING));
					deck.add(new Actions(id,Actions.Type.LEND_A_HAND));
					deck.add(new Actions(id,Actions.Type.MALL_CRAWL));
					deck.add(new Actions(id,Actions.Type.NOT_ENOUGH_BULLETS));
					deck.add(new Actions(id,Actions.Type.OUTBREAK));
					deck.add(new Actions(id,Actions.Type.OVERRUN));
					deck.add(new Actions(id,Actions.Type.THEY_KEEP_COMING));
					deck.add(new Actions(id,Actions.Type.THEY_KEEP_COMING));
					deck.add(new Actions(id,Actions.Type.THEYRE_COMING_TO_GET_YOU));
					break;
			}
		}
	}

	public void drawCards(int num) {
		for (int i=0;i<num && deck.size()>0;i++) {
			Card card = deck.remove(deck.size() - 1);
			hand.add(card);
		}
		sortHand();
	}

	public void sortHand() {
		Collections.sort(hand, new Comparator<Card>() {
			public int compare(Card c1, Card c2) {
				if (c1 instanceof Actions && c2 instanceof Minions)
					return -1;
				else if (c1 instanceof Minions && c2 instanceof Actions)
					return 1;
				else if (c1 instanceof Actions && c2 instanceof Actions)
					return c1.getName().compareTo(c2.getName());
				else {
					Minions m1 = (Minions) c1;
					Minions m2 = (Minions) c2;
					if (m1.power == m2.power)
						return m1.name.compareTo(m2.name);
					else
						return m1.power - m2.power;
				}
			}
		});
	}

	public Minions ai_playMinion() {
		Minions m = null;
		for (Card c : hand) {
			if (c instanceof Minions) {
				m = (Minions) c;
				break;
			}
		}
		hand.remove(m);
		return m;
	}

	public int ai_getBase(ArrayList<Base> bases) {
		return random.nextInt(bases.size());
	}

	public void ai_discard(int num) {
		for (int i=0;i<num;i++) {
			if (hand.size()==0)
				break;
			discard.add(hand.remove(0));
		}
	}
}
