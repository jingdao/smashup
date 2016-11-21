package com.smashup;
import java.util.ArrayList;
import android.widget.ImageView;

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

	public void onPlay(final SmashUp ui, Player player, int target) {
		ArrayList<Minions> options;
		switch (type) {
			case SUPREME_OVERLORD:
				options = new ArrayList<Minions>();
				for (ArrayList<ArrayList<Minions>> a : ui.playedMinions)
					for (ArrayList<Minions> b : a)
						for (Minions m : b)
							if (m!=this)
								options.add(m);
				if (options.size() == 0)
					break;
				options.add(null);
				returnToHand(ui,options);
				return;
			case COLLECTOR:
				options = new ArrayList<Minions>();
				for (ArrayList<Minions> b : ui.playedMinions.get(target))
					for (Minions m : b)
						if (m!=this && m.power + m.powerCounters <= 3)
							options.add(m);
				if (options.size() == 0)
					break;
				options.add(null);
				returnToHand(ui,options);
				return;
		}
		if (ui.currentPlayer!=0)
			ui.endTurn();
	}

	public void returnToHand(final SmashUp ui, ArrayList<Minions> options) {
		Runnable r = new Runnable() {
			public void run() {
				for (int i=0;i<ui.numPlayers+1;i++) {
					for (int j=0;j<ui.numPlayers;j++) {
						for (int k=0;k<ui.playedMinions.get(i).get(j).size();k++) {
							if (ui.playedMinions.get(i).get(j).get(k) == ui.targetMinion) {
								Minions m = ui.playedMinions.get(i).get(j).remove(k);
								ui.players.get(m.playerID).hand.add(m);
								ImageView iv = ui.minionViews.get(i).get(j).remove(k);
								ui.al.removeView(iv);
								ui.displayPlayedMinions();
								ui.alert(m.getDisplayName()+" is returned to hand",new Runnable() {
									public void run() {
										if (ui.currentPlayer!=0)
											ui.endTurn();
									}
								});
								return;
							}
						}
					}
				}
				if (ui.currentPlayer!=0)
					ui.endTurn();
			}
		};
		if (playerID==0) {
			ui.selectMinion(this,options,r);
		} else {
			int choice = ui.random.nextInt(options.size());
			ui.targetMinion = options.get(choice);
			r.run();
		}
	}

}
