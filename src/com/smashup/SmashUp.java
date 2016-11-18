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

public class SmashUp extends Activity {
	int width,height;
	AbsoluteLayout al;
	int cardWidth = 100;
	int cardHeight = 150;
	int leftMargin = 100;
	int topMargin = 50;
	int margin = 10;
	int numPlayers = 2;
	int playerWidth = 200;
	int listMargin = 150;
	ArrayList<ArrayList<ArrayList<Minions>>> playedMinions;
	ArrayList<ArrayList<ArrayList<ImageView>>> minionViews;
	ArrayList<Base> bases;
	ArrayList<Player> players;
	ArrayList<TextView> baseViews;
	ArrayList<TextView> playerViews;
	ArrayList<ImageView> listViews;

	Random random = new Random();
	Player.DeckType faction1,faction2;
	int currentPlayer;

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
		Collections.shuffle(Base.allBases);
		for (int i=0;i<numPlayers+1;i++) {
			ArrayList<ArrayList<Minions>> pm = new ArrayList<ArrayList<Minions>>();
			ArrayList<ArrayList<ImageView>> mv = new ArrayList<ArrayList<ImageView>>();
			for (int j=0;j<numPlayers;j++) {
				pm.add(new ArrayList<Minions>());
				mv.add(new ArrayList<ImageView>());
			}
			playedMinions.add(pm);
			minionViews.add(mv);
			Base b = Base.allBases.get(i);
			bases.add(b);
			TextView text = new TextView(this);
			text.setLayoutParams(new AbsoluteLayout.LayoutParams(leftMargin,cardHeight,0,topMargin+margin+(cardHeight+margin)*i));
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
			text.setText(p.name+"\n"+p.vp+" VP (hand:"+p.hand.size()+")");
			al.addView(text);
			playerViews.add(text);
			players.add(p);
		}
		currentPlayer = random.nextInt(numPlayers);
		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
//		playMinion(new Minions(0,Minions.Type.ZAPBOT),0);
	}

	public void playMinion(Minions m, int target) {
		final int i = target;
		final int j = m.playerID;
		if (playedMinions.get(i).get(j).size() > 0)
			minionViews.get(i).get(j).get(playedMinions.get(i).get(j).size()-1).setClickable(false);
		playedMinions.get(target).get(m.playerID).add(m);
		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		al.addView(iv);
		minionViews.get(target).get(m.playerID).add(iv);
		Resources res = getResources();
		int vid = res.getIdentifier(m.name, "drawable", getApplicationContext().getPackageName());
		iv.setImageResource(vid);
		iv.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				toggleDisplay();
				ArrayList<Card> list = new ArrayList<Card>();
				list.addAll(playedMinions.get(i).get(j));
				displayList(list);
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

	public void displayPlayedMinions() {
		int y = topMargin + margin;
		for (int i=0; i<numPlayers+1; i++) {
			int x = leftMargin + margin;
			for (int j=0; j<numPlayers; j++) {
				if (playedMinions.get(i).get(j).size() != 0) {
					for (ImageView m : minionViews.get(i).get(j)) {
						m.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,x,y));
						x += margin;
					}
					x += cardWidth;
				}
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
				}
			}
			for (TextView t : baseViews)
				t.setVisibility(View.VISIBLE);
			for (TextView t : playerViews)
				t.setVisibility(View.VISIBLE);
		}
	}

	public void displayList(ArrayList<Card> l) {
		int h = height - listMargin*2 - margin * 2;
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
			iv.setLayoutParams(new AbsoluteLayout.LayoutParams(w,h,x,listMargin + margin));
			x += w + margin;
		}
	}

	public void selectNumPlayers() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
				.setTitle("Select number of players:")
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
			.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String s = options.remove(id);
						if (n==1) {
							faction1 = Player.DeckType.valueOf(s);
							selectFaction(2);
						} else {
							faction2 = Player.DeckType.valueOf(s);
							setupBoard();
							displayPlayedMinions();
						}
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
				displayList(players.get(0).hand);
			}
			return true;
		case R.id.abilitymenu:
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
