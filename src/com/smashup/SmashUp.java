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

public class SmashUp extends Activity {
	int width,height;
	AbsoluteLayout al;
	int cardWidth = 100;
	int cardHeight = 150;
	int leftMargin = 100;
	int topMargin = 50;
	int margin = 10;
	int numPlayers = 2;
	ArrayList<ArrayList<Minions>> playedMinions;
	ArrayList<ArrayList<ImageView>> minionViews;
	ArrayList<Base> bases;
	ArrayList<Player> players;
	ArrayList<TextView> baseViews;
	ArrayList<TextView> playerViews;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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
		playedMinions = new ArrayList<ArrayList<Minions>>();
		minionViews = new ArrayList<ArrayList<ImageView>>();
		bases = new ArrayList<Base>();
		players = new ArrayList<Player>();
		baseViews = new ArrayList<TextView>();
		playerViews = new ArrayList<TextView>();
		Collections.shuffle(Base.allBases);
		for (int i=0;i<numPlayers+1;i++) {
			playedMinions.add(new ArrayList<Minions>());
			minionViews.add(new ArrayList<ImageView>());
			Base b = Base.allBases.get(i);
			bases.add(b);
			TextView text = new TextView(this);
			text.setLayoutParams(new AbsoluteLayout.LayoutParams(leftMargin,cardHeight,0,topMargin+margin+(cardHeight+margin)*i));
			text.setText(b.name+"\n"+b.breakpoint+" ("+b.vp1+","+b.vp2+","+b.vp3+")");
			al.addView(text);
			baseViews.add(text);
		}
		for (int i=0;i<numPlayers;i++) {
			Player p = new Player(i,Player.DeckType.ROBOTS,Player.DeckType.ALIENS);
			Collections.shuffle(p.deck);
			p.drawCards(5);
			TextView text = new TextView(this);
			text.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth*2,topMargin,leftMargin+margin+(cardWidth*2+margin)*i,0));
			text.setText(p.name+"\n"+p.vp+" VP (hand:"+p.hand.size()+")");
			al.addView(text);
			playerViews.add(text);
		}
		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(0,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(1,Minions.Type.ZAPBOT),1);
		playMinion(new Minions(0,Minions.Type.ZAPBOT),0);
	}

	public void playMinion(Minions m, int target) {
		playedMinions.get(target).add(m);
		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		al.addView(iv);
		minionViews.get(target).add(iv);
		Resources res = getResources();
		int vid = res.getIdentifier(m.name, "drawable", getApplicationContext().getPackageName());
		iv.setImageResource(vid);
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
				boolean skip = true;
				for (int k=0; k<playedMinions.get(i).size();k++) {
					Minions m= playedMinions.get(i).get(k);
					if (m.playerID == j) {
						minionViews.get(i).get(k).setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,x,y));
						x += margin;
						skip = false;
					}
				}
				if (!skip)
					x += cardWidth;
			}
			y += cardHeight + margin;
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
							setupBoard();
							displayPlayedMinions();
						}
				 });
			AlertDialog alert = builder.create();
			alert.show();
	}

	public void onBackPressed() {
//		if (radioIcons.getVisibility()==View.VISIBLE) {
//			radioIcons.setVisibility(View.GONE);
//			for (View v:previousIcons) v.setVisibility(View.VISIBLE);
//			for (View v:neighborCardViews) v.setVisibility(View.GONE);
//			for (View v:neighborDualResourceIcons) v.setVisibility(View.GONE);
//			if (cardDescriptionText.getVisibility()==View.VISIBLE||messageText.getVisibility()==View.VISIBLE) {
//				for (View v:resourceIcons) v.setVisibility(View.GONE);
//				for (View v:dualResourceIcons) v.setVisibility(View.GONE);
//			} else displayResources(p);
//		} else if (wonderDescription.getVisibility()==View.VISIBLE) {
//			for (View v:previousIcons) v.setVisibility(View.VISIBLE);
//			wonderDescription.setVisibility(View.GONE);
//		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.handmenu:
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
