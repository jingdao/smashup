package com.smashup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Gravity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Matrix;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.widget.AbsoluteLayout;
import android.os.Handler;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.lang.Runnable;

public class SmashUp extends Activity {
	int width,height;
	AbsoluteLayout al;
	int cardWidth = 100;
	int cardHeight = 150;
	int leftMargin = 100;
	int topMargin = 50;
	int margin = 10;
	int numPlayers = 2;
	int playerWidth = 150;
	int listMargin = 50;
	int textHeight = 20;
	ArrayList<ArrayList<ArrayList<Minions>>> playedMinions;
	ArrayList<ArrayList<ArrayList<ImageView>>> minionViews;
	ArrayList<Base> bases;
	ArrayList<Player> players;
	ArrayList<TextView> baseViews;
	ArrayList<TextView> playerViews;
	ArrayList<ImageView> listViews;
	ArrayList<ArrayList<TextView>> powerViews;

	Random random = new Random();
	Player.DeckType faction1,faction2;
	int currentPlayer,baseCounter, targetOption, targetBase;
	Minions targetMinion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		al = (AbsoluteLayout) findViewById(R.id.layout1);
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight()-getStatusBarHeight();
		selectNumPlayers();
    }

	public void setupBoard() {
		cardHeight = (height - topMargin - margin) / (numPlayers + 1) - margin;
		cardWidth = (int) (cardHeight / 1.5);
		playedMinions = new ArrayList<ArrayList<ArrayList<Minions>>>();
		minionViews = new ArrayList<ArrayList<ArrayList<ImageView>>>();
		bases = new ArrayList<Base>();
		players = new ArrayList<Player>();
		baseViews = new ArrayList<TextView>();
		playerViews = new ArrayList<TextView>();
		listViews = new ArrayList<ImageView>();
		powerViews = new ArrayList<ArrayList<TextView>>();
		Collections.shuffle(Base.allBases);
		for (int i=0;i<numPlayers+1;i++) {
			ArrayList<ArrayList<Minions>> pm = new ArrayList<ArrayList<Minions>>();
			ArrayList<ArrayList<ImageView>> mv = new ArrayList<ArrayList<ImageView>>();
			ArrayList<TextView> pv = new ArrayList<TextView>();
			for (int j=0;j<numPlayers;j++) {
				pm.add(new ArrayList<Minions>());
				mv.add(new ArrayList<ImageView>());
				TextView tv = new TextView(this);
				tv.setLayoutParams(new AbsoluteLayout.LayoutParams((int)(textHeight*1.5),textHeight,leftMargin+margin+(playerWidth+margin)*j,topMargin+(cardHeight+margin)*(i+1)-textHeight-margin));
				tv.setBackgroundResource(android.R.color.black);
				tv.setVisibility(View.GONE);
				al.addView(tv);
				pv.add(tv);
			}
			playedMinions.add(pm);
			minionViews.add(mv);
			powerViews.add(pv);
			Base b = Base.allBases.get(baseCounter);
			baseCounter++;
			bases.add(b);
			TextView text = new TextView(this);
			text.setLayoutParams(new AbsoluteLayout.LayoutParams(leftMargin,cardHeight,0,topMargin+(cardHeight+margin)*i));
			text.setText(b.name+"\n"+b.breakpoint+" ("+b.vp1+","+b.vp2+","+b.vp3+")");
			al.addView(text);
			baseViews.add(text);
		}
		ArrayList<Player.DeckType> decks = new ArrayList<Player.DeckType>(Arrays.asList(Player.DeckType.values()));
		Collections.shuffle(decks);
		for (int i=0;i<numPlayers;i++) {
			Player p;
			if (i==0) {
				p = new Player(i,faction1,faction2);
				decks.remove(faction1);
				decks.remove(faction2);
			} else {
				p = new Player(i,decks.get(0),decks.get(1));
				decks.remove(0);
				decks.remove(0);
			}
			Collections.shuffle(p.deck);
			p.drawCards(5);
			TextView text = new TextView(this);
			text.setLayoutParams(new AbsoluteLayout.LayoutParams(playerWidth,topMargin,leftMargin+margin+(playerWidth+margin)*i,0));
			text.setText(p.name+" "+p.vp+"VP (hand:"+p.hand.size()+")");
			al.addView(text);
			playerViews.add(text);
			players.add(p);
		}
		currentPlayer = random.nextInt(numPlayers);
		gameLoop();
	}

	public void gameLoop() {
		startTurn();
		if (currentPlayer==0) {
			new AlertDialog.Builder(this).setMessage("Your turn").create().show();
		} else {
			final Minions m = players.get(currentPlayer).ai_playMinion();
			if (m != null) {
				final int n = players.get(currentPlayer).ai_getBase(bases);
				playMinion(m,n);
			} else {
				endTurn();
			}
		}
	}

	public void startTurn() {
		Player p = players.get(currentPlayer);
		p.actionsLeft = 1;
		p.minionsLeft = 1;
		p.minionsPlayed = 0;
		displayPlayedMinions();
	}

	public void endTurn() {
		if (checkBreakPoint())
			return;
		if (checkWinner())
			return;
		final Player p = players.get(currentPlayer);
		if (p.hand.size() > 10) {
			Runnable r = new Runnable() {
				public void run() {
					playerViews.get(currentPlayer).setText(p.name+" "+p.vp+"VP (hand:"+p.hand.size()+")");
					currentPlayer = (currentPlayer + 1) % numPlayers;
					gameLoop();
				}
			};
			if (currentPlayer == 0) {
				selectDiscard(p.hand.size() - 10,r);
			} else {
				p.ai_discard(p.hand.size() - 10);
				alert("Player "+currentPlayer+" discards down to 10 cards",r);
			}
		} else {
			p.drawCards(2);
			playerViews.get(currentPlayer).setText(p.name+" "+p.vp+"VP (hand:"+p.hand.size()+")");
			currentPlayer = (currentPlayer + 1) % numPlayers;
			gameLoop();
		}
	}

	public boolean checkBreakPoint() {
		for (int i=0;i<numPlayers+1;i++) {
			int[] score = new int[numPlayers];
			int[] vp = new int[numPlayers];
			int sum = 0;
			for (int j=0;j<numPlayers;j++) {
				score[j] = getMinionPower(i,j);
				if (score[j] > 0)
					sum += score[j];
			}
			if (sum >= bases.get(i).breakpoint) {
				String s = bases.get(i).name+" has scored!\n";
				for (int j=0;j<numPlayers;j++) {
					Player p = players.get(j);
					p.discard.addAll(playedMinions.get(i).get(j));
					playedMinions.get(i).get(j).clear();
					for (ImageView iv : minionViews.get(i).get(j))
						al.removeView(iv);
					minionViews.get(i).get(j).clear();
					if (score[j] >= 0) {
						int place=0;
						for (int k=0;k<numPlayers;k++)
							if (k!=j && score[k] > score[j])
								place++;
						if (place==0) vp[j] = bases.get(i).vp1;
						else if (place==1) vp[j] = bases.get(i).vp2;
						else if (place==2) vp[j] = bases.get(i).vp3;
					}
					s += "Player "+j+": "+vp[j]+"vp\n";
					p.vp += vp[j];
					playerViews.get(j).setText(p.name+" "+p.vp+"VP (hand:"+p.hand.size()+")");
				}
				Base b = Base.allBases.get(baseCounter);
				baseCounter++;
				bases.set(i,b);
				baseViews.get(i).setText(b.name+"\n"+b.breakpoint+" ("+b.vp1+","+b.vp2+","+b.vp3+")");
				Runnable r = new Runnable() {
					public void run() {
						endTurn();
					}
				};
				displayPlayedMinions();
				alert(s,r);
				return true;
			}
		}
		return false;
	}

	public boolean checkWinner() {
		for (int i=0;i<numPlayers;i++) {
			if (players.get(i).vp >= 15) {
				alert("Player "+i+" is the winner!", new Runnable() {
					public void run() {
						Intent intent = getIntent();
						finish();
						startActivity(intent);
					}
				});
				return true;
			}
		}
		return false;
	}

	public void playMinion(final Minions m, final int target) {
		final int i = target;
		final int j = m.playerID;
		final SmashUp ui = this;
		playedMinions.get(target).get(m.playerID).add(m);
		players.get(m.playerID).minionsPlayed ++;

		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		al.addView(iv);
		minionViews.get(target).get(m.playerID).add(iv);
		Resources res = getResources();
		 int vid = res.getIdentifier(m.name, "drawable", getApplicationContext().getPackageName());
		iv.setImageResource(vid);
		displayPlayedMinions();
		alert("Player "+currentPlayer+" played "+m.getDisplayName()+" on "+bases.get(target).name,new Runnable() {
			public void run() {
				m.onPlay(ui,ui.players.get(ui.currentPlayer),target);
			}
		});
	}

	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
		      result = getResources().getDimensionPixelSize(resourceId);
		  } 
		  return result;
	}

	public int getMinionPower(int base, int playerID) {
		if (playedMinions.get(base).get(playerID).size()==0)
			return -1;
		int p = 0;
		for (Minions m : playedMinions.get(base).get(playerID))
			p += m.getPower(this,base);
		return p;
	}

	public void displayPlayedMinions() {
		int y = topMargin;
		for (int i=0; i<numPlayers+1; i++) {
			for (int j=0; j<numPlayers; j++) {
				int x = leftMargin + margin + (playerWidth + margin) * j;
				for (ImageView m : minionViews.get(i).get(j)) {
					m.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,x,y));
					m.setClickable(false);
					x += margin;
				}
				if (minionViews.get(i).get(j).size() > 0) {
					int power = getMinionPower(i,j);
					TextView tv = powerViews.get(i).get(j);
					tv.setText(power+"");
					tv.setVisibility(View.VISIBLE);
					tv.bringToFront();
					final int p = i;
					final int q = j;
					ImageView iv = minionViews.get(i).get(j).get(minionViews.get(i).get(j).size()-1);
					iv.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0) {
							toggleDisplay();
							ArrayList<Card> list = new ArrayList<Card>();
							list.addAll(playedMinions.get(p).get(q));
							displayList(list);
						}
					});
				} else
					powerViews.get(i).get(j).setVisibility(View.GONE);
			}
			y += cardHeight + margin;
		}
	}

	public void toggleDisplay() {
		if (listViews.size()==0) {
			for (int i=0;i<numPlayers+1;i++) {
				for (int j=0;j<numPlayers;j++) {
					for (ImageView m : minionViews.get(i).get(j))
						m.setVisibility(View.GONE);
					powerViews.get(i).get(j).setVisibility(View.GONE);
				}
			}
			for (TextView t : baseViews)
				t.setVisibility(View.GONE);
			for (TextView t : playerViews)
				t.setVisibility(View.GONE);
		} else {
			for (ImageView i : listViews)
				i.setVisibility(View.GONE);
			listViews.clear();
			for (int i=0;i<numPlayers+1;i++) {
				for (int j=0;j<numPlayers;j++) {
					for (ImageView m : minionViews.get(i).get(j))
						m.setVisibility(View.VISIBLE);
					if (minionViews.get(i).get(j).size() > 0)
						powerViews.get(i).get(j).setVisibility(View.VISIBLE);
				}
			}
			for (TextView t : baseViews)
				t.setVisibility(View.VISIBLE);
			for (TextView t : playerViews)
				t.setVisibility(View.VISIBLE);
		}
	}

	public void alert(String message, final Runnable r) {
		new AlertDialog.Builder(this).setMessage(message)
		.setCancelable(false)
		.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (r!=null)
					r.run();
			}
		}).create().show();
	}

	public void displayList(ArrayList<Card> l) {
		int h = (height - listMargin*2 - margin) / 2;
		int w = (int) (h / 1.5);
		int x = margin;
		for (Card m : l) {
			ImageView iv = new ImageView(this);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			al.addView(iv);
			listViews.add(iv);
			Resources res = getResources();
			int vid = res.getIdentifier(m.getName(), "drawable", getApplicationContext().getPackageName());
			iv.setImageResource(vid);
			iv.setLayoutParams(new AbsoluteLayout.LayoutParams(w,h,x,listMargin));
			x += w + margin;
		}
	}

	public void displayHand(ArrayList<Card> l) {
		int h = (height - listMargin*2 - margin) / 2;
		int w = (int) (h / 1.5);
		int x1 = margin;
		int x2 = margin;
		for (Card m : l) {
			ImageView iv = new ImageView(this);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			al.addView(iv);
			listViews.add(iv);
			Resources res = getResources();
			int vid = res.getIdentifier(m.getName(), "drawable", getApplicationContext().getPackageName());
			iv.setImageResource(vid);
			if (m instanceof Actions) {
				iv.setLayoutParams(new AbsoluteLayout.LayoutParams(w,h,x1,listMargin));
				x1 += w + margin;
			} else {
				iv.setLayoutParams(new AbsoluteLayout.LayoutParams(w,h,x2,listMargin+h+margin));
				x2 += w + margin;
			}
			final Card selectedCard = m;
			final SmashUp ui = this;
			iv.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) {
					if (selectedCard instanceof Minions && players.get(0).minionsLeft > 0) {
						ui.targetMinion = (Minions)selectedCard;
						Runnable r = new Runnable() {
							public void run() {
								Player p = ui.players.get(0);
								p.minionsLeft--;
								p.hand.remove(ui.targetMinion);
								ui.toggleDisplay();
								ui.playMinion(ui.targetMinion,ui.targetBase);
							}
						};
						selectBase((Minions)selectedCard,false,r);
					}
				}
			});
		}
	}

	public void selectNumPlayers() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
				.setTitle("Select number of players:")
				.setCancelable(false)
				.setItems(new CharSequence[] {"2","3","4"}, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							switch(id) {
								case 0:
									numPlayers=2;
									break;
								case 1:
									numPlayers=3;
									break;
								case 2:
									numPlayers=4;
									break;
							}
							selectFaction(1);
						}
				 });
			AlertDialog alert = builder.create();
			alert.show();
	}

	public void selectFaction(final int n) {
		final ArrayList<String> options = new ArrayList<String>();
		for (Player.DeckType d : Player.DeckType.values())
			if (n==1 || d!=faction1)
				options.add(d.name());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder .setTitle("Select faction:")
			.setCancelable(false)
			.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String s = options.remove(id);
						if (n==1) {
							faction1 = Player.DeckType.valueOf(s);
							selectFaction(2);
						} else {
							faction2 = Player.DeckType.valueOf(s);
							setupBoard();
						}
					}
			 });
			AlertDialog alert = builder.create();
			alert.show();
	}

	public void selectBase(final Minions m,boolean allowNone,final Runnable r) {
		final ArrayList<String> options = new ArrayList<String>();
		for (Base b : bases)
			options.add(b.name);
		if (allowNone)
			options.add("NONE");
		final SmashUp ui = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select base to play "+m.getDisplayName()+":")
		.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ui.targetBase = id;
					r.run();
				}
		 });
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void selectDiscard(final int num,final Runnable r) {
		final ArrayList<String> options = new ArrayList<String>();
		for (Card c : players.get(0).hand) {
			options.add(c.getDisplayName());	
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select card to discard: ")
		.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Player p = players.get(0);
					p.discard.add(p.hand.remove(id));
					if (num == 1) {
						r.run();
					} else {
						selectDiscard(num-1,r);
					}
				}
		 });
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void selectMinion(Card source, final ArrayList<Minions> list, final Runnable r) {
		final ArrayList<String> options = new ArrayList<String>();
		for (Minions m : list) {
			if (m!=null)
				options.add(m.getDisplayName());
			else
				options.add("NONE");
		}
		final SmashUp ui = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select minion: ("+source.getDisplayName()+")")
		.setCancelable(false)
		.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ui.targetMinion = list.get(id);
					r.run();
				}
		 });
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void selectOption(String instructions, ArrayList<String> list, final Runnable r) {
		final SmashUp ui = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(instructions)
		.setCancelable(false)
		.setItems(list.toArray(new String[0]), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ui.targetOption = id;
					r.run();
				}
		 });
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onBackPressed() {
		if (listViews.size() != 0)
			toggleDisplay();
	}

	public boolean onKeyDown(int keycode, KeyEvent event ) {
		 if(keycode == KeyEvent.KEYCODE_MENU){
			if (listViews.size() != 0) {
				return true;
			}
		 }
		 return super.onKeyDown(keycode,event);  
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.handmenu:
			if (players.get(0).hand.size()>0) {
				toggleDisplay();
				displayHand(players.get(0).hand);
			}
			return true;
		case R.id.abilitymenu:
			return true;
		case R.id.endturnmenu:
			endTurn();
			return true;
        case R.id.newgamemenu:
			Intent intent = getIntent();
			finish();
			startActivity(intent);
            return true;
        case R.id.quitmenu:
			finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
