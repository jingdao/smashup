package com.smashup;
import java.util.Collections;
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

	public int getPower(SmashUp ui, int baseID) {
		int p = power + powerCounters;
		if (type == Type.ARMOR_STEGO && ui.currentPlayer != playerID)
			p += 2;
		if (type == Type.WAR_RAPTOR) {
			for (Minions m : ui.playedMinions.get(baseID).get(playerID))
				if (m.type == Type.WAR_RAPTOR)
					p++;
		}
		if (type.name().startsWith("MICROBOT")) {
			int numMicrobotFixer = 0;
			for (ArrayList<ArrayList<Minions>> a : ui.playedMinions)
				for (ArrayList<Minions> b : a)
					for (Minions m : b)
						if (m.playerID == playerID && m.type == Type.MICROBOT_FIXER)
							numMicrobotFixer ++;
			p += numMicrobotFixer;
		}
		return p;
	}

	public void onPlay(final SmashUp ui, final Player player, int target) {
		final ArrayList<Minions> options = new ArrayList<Minions>();
		final ArrayList<String> stringOptions = new ArrayList<String>();
		switch (type) {
			case SUPREME_OVERLORD:
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
				for (ArrayList<Minions> b : ui.playedMinions.get(target))
					for (Minions m : b)
						if (m!=this && m.getPower(ui,target) <= 3)
							options.add(m);
				if (options.size() == 0)
					break;
				options.add(null);
				returnToHand(ui,options);
				return;
			case INVADER:
				player.vp += 1;
				ui.playerViews.get(ui.currentPlayer).setText(player.name+" "+player.vp+"VP (hand:"+player.hand.size()+")");
				ui.alert("Player "+ui.currentPlayer+" gain 1 VP",new Runnable() {
					public void run() {
						if (ui.currentPlayer!=0)
							ui.endTurn();
					}
				});
				return;
			case LASERATOPS:
				for (ArrayList<Minions> b : ui.playedMinions.get(target))
					for (Minions m : b)
						if (m!=this && m.getPower(ui,target) <= 2)
							options.add(m);
				if (options.size() == 0)
					break;
				destroyMinion(ui,options);
				return;
			case NINJA_MASTER:
				for (ArrayList<Minions> b : ui.playedMinions.get(target))
					for (Minions m : b)
						if (m!=this)
							options.add(m);
				if (options.size() == 0)
					break;
				options.add(null);
				destroyMinion(ui,options);
				return;
			case TIGER_ASSASSIN:
				for (ArrayList<Minions> b : ui.playedMinions.get(target))
					for (Minions m : b)
						if (m!=this && m.getPower(ui,target) <= 3)
							options.add(m);
				if (options.size() == 0)
					break;
				options.add(null);
				destroyMinion(ui,options);
				return;
			case HOVERBOT:
				final Card card = player.deck.get(0);
				final Runnable cancel = new Runnable() {
					public void run() {
						ui.alert(card.getDisplayName()+" is revealed",new Runnable() {
							public void run() {
								if (ui.currentPlayer!=0)
									ui.endTurn();
							}
						});
					}
				};
				if (card instanceof Minions) {
					Runnable r = new Runnable() {
						public void run() {
							if (ui.targetBase == ui.bases.size())
								cancel.run();
							else {
								Minions m = (Minions) player.deck.remove(0);
								ui.playMinion(m,ui.targetBase);
							}
						}
					};
					if (playerID==0) {
						ui.selectBase((Minions)card,true,r);
					} else {
						ui.targetBase = ui.random.nextInt(ui.bases.size());
						r.run();
					}
				} else
					cancel.run();
				return;
			case ZAPBOT:
				for (Card c : player.hand)
					if (c instanceof Minions && ((Minions)c).power <= 2)
						options.add((Minions)c);
				options.add(null);
				extraMinion(ui,options);
				return;
			case MICROBOT_RECLAIMER:
				ArrayList<Minions> microbots = new ArrayList<Minions>();
				for (Card c : player.discard)
					if (c instanceof Minions && ((Minions)c).type.name().startsWith("MICROBOT"))
						microbots.add((Minions)c);
				for (Card c : player.hand)
					if (c instanceof Minions)
						options.add((Minions)c);
				options.add(null);
				Runnable r1 = new Runnable() {
					public void run() {
						if (player.minionsPlayed == 1)
							extraMinion(ui,options);
					}
				};
				if (microbots.size() > 0) {
					player.discard.removeAll(microbots);
					player.deck.addAll(microbots);
					Collections.shuffle(player.deck);
					ui.alert("Microbots were shuffled into the deck",r1);
				} else
					r1.run();
				return;
			case MICROBOT_FIXER:
				if (player.minionsPlayed == 1) {
					for (Card c : player.hand)
						if (c instanceof Minions)
							options.add((Minions)c);
					options.add(null);
					extraMinion(ui,options);
				}
				return;
		}
		if (ui.currentPlayer!=0)
			ui.endTurn();
	}

	public void onBaseScored(final SmashUp ui) {
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
								ui.players.get(m.playerID).sortHand();
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
			int choice;
			do {
				choice = ui.random.nextInt(options.size());
				ui.targetMinion = options.get(choice);
			} while (options.size() > 1 && ui.targetMinion == null);
			r.run();
		}
	}

	public void destroyMinion(final SmashUp ui, ArrayList<Minions> options) {
		Runnable r = new Runnable() {
			public void run() {
				for (int i=0;i<ui.numPlayers+1;i++) {
					for (int j=0;j<ui.numPlayers;j++) {
						for (int k=0;k<ui.playedMinions.get(i).get(j).size();k++) {
							if (ui.playedMinions.get(i).get(j).get(k) == ui.targetMinion) {
								Minions m = ui.playedMinions.get(i).get(j).remove(k);
								ui.players.get(m.playerID).discard.add(m);
								ImageView iv = ui.minionViews.get(i).get(j).remove(k);
								ui.al.removeView(iv);
								ui.displayPlayedMinions();
								ui.alert(m.getDisplayName()+" is destroyed",new Runnable() {
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
			int choice;
			do {
				choice = ui.random.nextInt(options.size());
				ui.targetMinion = options.get(choice);
			} while (options.size() > 1 && ui.targetMinion == null);
			r.run();
		}
	}

	public void extraMinion(final SmashUp ui, ArrayList<Minions> options) {
		final Minions m = this;
		final Runnable q = new Runnable() {
			public void run() {
				ui.players.get(m.playerID).hand.remove(ui.targetMinion);
				ui.playMinion(ui.targetMinion,ui.targetBase);
			}
		};
		Runnable r = new Runnable() {
			public void run() {
				if (ui.targetMinion==null)
					return;
				if (ui.currentPlayer == 0)
					ui.selectBase(ui.targetMinion,false,q);
				else {
					ui.targetBase = ui.random.nextInt(ui.bases.size());
					q.run();
				}
			}
		};
		if (playerID==0) {
			ui.selectMinion(this,options,r);
		} else {
			int choice;
			do {
				choice = ui.random.nextInt(options.size());
				ui.targetMinion = options.get(choice);
			} while (options.size() > 1 && ui.targetMinion == null);
			r.run();
		}
	}
}
