package s180475.android1.hioa;

import java.util.Arrays;
import java.util.Random;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpillActivity extends FragmentActivity {

	public static final int antallOrdFraStart = 16;
	public static final int alfabetLengde = 29;
	private boolean iRestart;	//er true når vi kommer fra en omstart
	
	private String[] ordTabell;	//Inneholder alle ord som kan gjettes på
	private TextView[] bokstavArray;	//Inneholder feltene som bokstavene i ordet skal skrives ut i
	private TextView[] tastaturArray; //Inneholder alle bokstavene som kan trykkes på
	private char[] gjettedeBokstaver;	//Inneholder alle bokstaver i ordet som har blitt gjettet riktig
	private boolean[] trykkedeBokstaver;	//Inneholder status om hvilke knapper som er trykt inn/brukt
	
	private int pos;	//Inneholder posisjonen til det trukne ordet
	private int antallOrd; //Holder orden på antall ord som er igjen
	private int vunnet;
	private int tapt;
	private int ordLengde;	//holder orden på hvor mange bokstaver som ikke er gjettet
	private String ord;		//ord som har blitt tilfeldig plukket ut under spillets gang
	private int antFeilGjettet;
	private Random randomTall;
	private LinearLayout ll, bokstavLayout1, bokstavLayout2, bokstavLayout3;	//3 linjer med bokstaver/tastatur
	private LinearLayout.LayoutParams layoutParams;
	private LinearLayout.LayoutParams layoutParams2;
	private Typeface font;
	private TextView antTap, antSeire;
	private ImageView galge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spill);
		galge = (ImageView) findViewById(R.id.hm0);
		tastaturArray = new TextView[alfabetLengde]; //Plass til alle bokstaver i det norske alfabetet
		
		//Hvis det er lagret noe i Bundle savedInstanceState så ønsker vi å hente disse dataene
		if(savedInstanceState != null){
			ordTabell = savedInstanceState.getStringArray("ordTabell");
			gjettedeBokstaver = savedInstanceState.getCharArray("gjettedeBokstaver");
			ord= savedInstanceState.getString("gjeldendeOrd");
			iRestart = savedInstanceState.getBoolean("iRestart");
			pos = savedInstanceState.getInt("pos");
			ordLengde = savedInstanceState.getInt("ordLengde");
			antFeilGjettet = savedInstanceState.getInt("antFeilGjettet");
			vunnet = savedInstanceState.getInt("antSeire");
			tapt = savedInstanceState.getInt("antTap");
			antallOrd = savedInstanceState.getInt("antallOrd");
			
			switch(antFeilGjettet){
			case 1: galge.setImageResource(R.drawable.hm1);
					break;
			case 2: galge.setImageResource(R.drawable.hm2);
					break;
			case 3: galge.setImageResource(R.drawable.hm3);
					break;
			case 4: galge.setImageResource(R.drawable.hm4);
					break;
			case 5: galge.setImageResource(R.drawable.hm5);
					break;
			case 6: galge.setImageResource(R.drawable.hm6);
					break;
			case 7: galge.setImageResource(R.drawable.hm7);
			}
			
			trykkedeBokstaver = savedInstanceState.getBooleanArray("trykkedeBokstaver");
		}
		else{ //Da er det ikke lagret noe, og vi starter et nytt spill
			ordTabell = getResources().getStringArray(R.array.hangman);
			trykkedeBokstaver = new boolean[alfabetLengde];
			iRestart = false;
			vunnet = 0;
			tapt = 0;
			antFeilGjettet = 0;
			antallOrd = ordTabell.length;
		}
		
		font = Typeface.createFromAsset(getAssets(), "fonts/KGTenThousandReasons.ttf"); 
		
		ll = (LinearLayout) findViewById(R.id.tekstfelter);
		bokstavLayout1 = (LinearLayout) findViewById(R.id.bokstavLayout1);
		bokstavLayout2 = (LinearLayout) findViewById(R.id.bokstavLayout2);
		bokstavLayout3 = (LinearLayout) findViewById(R.id.bokstavLayout3);
		
		antTap = (TextView) findViewById(R.id.antallTapTekst);
		antTap.setTypeface(font);
		antSeire = (TextView) findViewById(R.id.antallSeireTekst);
		antSeire.setTypeface(font);
		randomTall = new Random();
		antTap.append(" " + tapt);
		antSeire.append(" " + vunnet);
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		//setter marginer til venstre for textview'ne
		layoutParams.setMargins(5, 0, 0, 0);
		layoutParams2.setMargins(20, 0, 0, 0);
		
		nyttOrd();
		
		//Oppretter tastaturet/bokstavene som skal kunne velges i mellom
		int j = 0;
		for(int i = 97; i <= 106; i++){				//Lager "tastaturet"s øverste linje
			char c = (char) i;
			TextView bokstav = new TextView(this);
			bokstav.setTypeface(font,Typeface.BOLD);
			bokstav.setText(Character.toString(c));
			bokstav.setTextSize(getResources().getDimension(R.dimen.p));
			bokstav.setTextColor(getResources().getColor(R.color.white));
			bokstav.setClickable(true);
			bokstav.setOnClickListener(bokstavLytter);
			if(i != 97)			//Ikke leftMargin på første bokstav
				bokstavLayout1.addView(bokstav,layoutParams2);
			else
				bokstavLayout1.addView(bokstav);
			tastaturArray[j++] = bokstav;
		}
		for(int i = 107; i <= 116; i++){				//Lager "tastaturet"s andre linje
			char c = (char) i;
			TextView bokstav = new TextView(this);
			bokstav.setTypeface(font,Typeface.BOLD);
			bokstav.setText(Character.toString(c));
			bokstav.setTextSize(getResources().getDimension(R.dimen.p));
			bokstav.setTextColor(getResources().getColor(R.color.white));
			bokstav.setGravity(Gravity.CENTER);
			bokstav.setClickable(true);
			bokstav.setOnClickListener(bokstavLytter);
			if(i != 107)
				bokstavLayout2.addView(bokstav,layoutParams2);
			else
				bokstavLayout2.addView(bokstav);
			tastaturArray[j++] = bokstav;
		}
		for(int i = 117; i <= 122; i++){				//Lager "tastaturet"s tredje linje
			char c = (char) i;
			TextView bokstav = new TextView(this);
			bokstav.setTypeface(font,Typeface.BOLD);
			bokstav.setText(Character.toString(c));
			bokstav.setTextSize(getResources().getDimension(R.dimen.p));
			bokstav.setTextColor(getResources().getColor(R.color.white));
			bokstav.setClickable(true);
			bokstav.setOnClickListener(bokstavLytter);
			if(i != 117)
				bokstavLayout3.addView(bokstav,layoutParams2);
			else
				bokstavLayout3.addView(bokstav);
			tastaturArray[j++] = bokstav;
		}
		
		TextView bokstav2 = new TextView(this);
		bokstav2.setTypeface(font,Typeface.BOLD);
		bokstav2.setText("æ");
		bokstav2.setTextSize(getResources().getDimension(R.dimen.p));
		bokstav2.setTextColor(getResources().getColor(R.color.white));
		bokstav2.setClickable(true);
		bokstav2.setOnClickListener(bokstavLytter);
		bokstavLayout3.addView(bokstav2,layoutParams2);
		tastaturArray[j++] = bokstav2;
		
		TextView bokstav3 = new TextView(this);
		bokstav3.setTypeface(font,Typeface.BOLD);
		bokstav3.setText("ø");
		bokstav3.setTextSize(getResources().getDimension(R.dimen.p));
		bokstav3.setTextColor(getResources().getColor(R.color.white));
		bokstav3.setClickable(true);
		bokstav3.setOnClickListener(bokstavLytter);
		bokstavLayout3.addView(bokstav3,layoutParams2);
		tastaturArray[j++] = bokstav3;
		
		TextView bokstav4 = new TextView(this);
		bokstav4.setTypeface(font,Typeface.BOLD);
		bokstav4.setText("å");
		bokstav4.setTextSize(getResources().getDimension(R.dimen.p));
		bokstav4.setTextColor(getResources().getColor(R.color.white));
		bokstav4.setClickable(true);
		bokstav4.setOnClickListener(bokstavLytter);
		bokstavLayout3.addView(bokstav4,layoutParams2);
		tastaturArray[j++] = bokstav4;
		
		for(int i = 0; i < alfabetLengde; i++){
			if(trykkedeBokstaver[i] == true){
				//merker bokstaven som brukt i "tastaturet"
				tastaturArray[i].setTextColor(getResources().getColor(R.color.roed));
				tastaturArray[i].setClickable(false);
			}
		}
	} // end of onCreate()	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_spill, menu);
		return true;
	}
	
	//Lagrer data om mobilen endrer status ved tilting av skjerm, innkommende anrop osv
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("antSeire", vunnet);
		savedInstanceState.putInt("antTap", tapt);
		savedInstanceState.putInt("antFeilGjettet",antFeilGjettet);
		savedInstanceState.putInt("antallOrd",antallOrd);
		savedInstanceState.putInt("ordLengde",ordLengde);
		savedInstanceState.putInt("pos",pos);
		savedInstanceState.putString("gjeldendeOrd",ord);
		savedInstanceState.putBoolean("iRestart", true);
		savedInstanceState.putStringArray("ordTabell",ordTabell);
		savedInstanceState.putCharArray("gjettedeBokstaver",gjettedeBokstaver);
		savedInstanceState.putBooleanArray("trykkedeBokstaver",trykkedeBokstaver);
	}
	
	//gjør klart spillet for ett nytt ord.
	public void resetSpill(){
		galge.setImageResource(R.drawable.hm0);		//tilbakestiller galgebildet
		antFeilGjettet = 0;
		for(int i = 0; i < trykkedeBokstaver.length; i++){
			if(trykkedeBokstaver[i] = true){	//Da er disse bokstavene trykket inn, og vi tilbakestiller
				tastaturArray[i].setTextColor(getResources().getColor(R.color.white));	//tilbakestiller "tastaturet"
				tastaturArray[i].setClickable(true);
				trykkedeBokstaver[i] = false;
			}
		}
		ll.removeAllViews();
	}
	
	//Finner ut om valgte bokstav var riktig eller feil, og event skriver denne/disse ut på tilhørende plass i ordet. Event tegner videre på galgen.
	public void validerGjetting(View v){
		TextView bokstav = (TextView) v;
		char tegn = bokstav.getText().charAt(0);
		int i = ord.indexOf(tegn);	//Finner posisjonen til bokstaven om den befinner seg i ordet
		
		if(i == -1){		//Hvis ikke bokstaven finnes i ordet
			antFeilGjettet++;
			switch(antFeilGjettet){
			case 1: galge.setImageResource(R.drawable.hm1);
					break;
			case 2: galge.setImageResource(R.drawable.hm2);
					break;
			case 3: galge.setImageResource(R.drawable.hm3);
					break;
			case 4: galge.setImageResource(R.drawable.hm4);
					break;
			case 5: galge.setImageResource(R.drawable.hm5);
					break;
			case 6: galge.setImageResource(R.drawable.hm6);
					break;
			case 7: galge.setImageResource(R.drawable.hm7); //siste bildet, altså hengt
					antTap.setText(getString(R.string.antallTap) + ++tapt);
					openEKDialog(getString(R.string.header_synd),getString(R.string.melding_synd,ord));
					lockGame();
					iRestart = false;
			}
		}
		while(i != -1){	//så lenge bokstaven finnes i ordet
			bokstavArray[i].setText(Character.toString(tegn));	//viser bokstaven på rett plass
			gjettedeBokstaver[i] = tegn;
			ordLengde--;
			
			if(ordLengde == 0){ //ordet er gjettet
				galge.setImageResource(R.drawable.hm8);
				openEKDialog(getString(R.string.header_gratulerer),getString(R.string.melding_gratulerer));
				antSeire.setText(getString(R.string.antallSeire) + ++vunnet);
				iRestart = false;
			}
			i = ord.indexOf(tegn, i+1); //søker videre i ordet etter flere forekomster av bokstaven
		}
		//merker bokstaven som brukt i "tastaturet"
		bokstav.setTextColor(getResources().getColor(R.color.roed));
		bokstav.setClickable(false);
		
		if((int)tegn ==230)
			trykkedeBokstaver[26] = true;
		else if((int)tegn == 248)
			trykkedeBokstaver[27] = true;
		else if((int)tegn == 229)
			trykkedeBokstaver[28] = true;
		else
			trykkedeBokstaver[(int)tegn - 97] = true;
	}
	
	//gjør klart nytt ord når spillet starter opp eller etter at forrige ord er ferdig
	public void nyttOrd(){
		boolean harMellomrom = false;
		if(antallOrd == 0 && iRestart || antallOrd != 0){ //Ved restart på siste ordet er antallOrd==0, og vi unngår det med denne
			if(!iRestart){ //Hvis vi er i en restart har vi allerede et ord, og må ikke trekke et nytt ett
				if(antallOrd < antallOrdFraStart) //hvis det ikke er første runde
					resetSpill();
				pos = randomTall.nextInt(antallOrd);		//Trekker et tilfeldig tall
				ord = ordTabell[pos];		//Plukker ut et ord som skal gjettes på
				fjernOrd(pos);
				ordLengde = ord.length();
				gjettedeBokstaver = new char[ordLengde];
			}
			
			bokstavArray = new TextView[ord.length()];
			for(int i = 0; i < ord.length(); i++){
				TextView tv = new TextView(this);
				tv.setTypeface(font);
				tv.setTextSize(getResources().getDimension(R.dimen.p));
				tv.setTextColor(getResources().getColor(R.color.white));
				tv.setGravity(Gravity.CENTER);
				char c = ord.charAt(i);
				if(Character.toString(c).equals(" ")){ //kan ikke lage understrek på et mellomrom, og spiller må ikke gjette på denne "bokstaven"
					harMellomrom = true;
					tv.setWidth(30);
				}
				else{
					tv.setBackgroundResource(R.drawable.understrek);
				}
				bokstavArray[i] = tv;
				for(int j = 0; j < ord.length(); j++){
					if(gjettedeBokstaver[i] != 0){
						bokstavArray[i].setText(Character.toString(gjettedeBokstaver[i]));
					}
				}
				ll.addView(tv, layoutParams);
			}
			if(harMellomrom && !iRestart) //har vi restartet har vi trukket fra ordlengden allerede før restart
				ordLengde--;
		}
		else{ //Ingen ord igjen i array
			openEKDialog(getResources().getString(R.string.header_tomt),getResources().getString(R.string.melding_tomt,vunnet,tapt));
		}
	}
	
	//Sletter ord i array for å unngå at samme ord blir trukket flere ganger
	public void fjernOrd(int p){
		ordTabell[p] = null;
		antallOrd--;
		
		//Fjerner null-punktet fra tabbelen, og kopierer til en ny array med ett mindre element
		if( antallOrd != 0){
			for(int i = p; i < antallOrd; i++){
				ordTabell[i] = ordTabell[i+1];
			}
			Arrays.copyOfRange(ordTabell, 0, antallOrd);
		}
	}
	
	//låser tastaturet
	public void lockGame(){
		for(int i = 0; i < alfabetLengde; i++){
			tastaturArray[i].setClickable(false);
		}
	}
	
	//Åpner dialogvinduet/pop-up
	public void openEKDialog(String t, String m){
		EnKnappsDialog ekd = EnKnappsDialog.newInstance(t, m);
		ekd.show(getSupportFragmentManager(),"EnKnappsDialog");
	}
	
	//Oppretter, og viser et dialogvindu med 2 knapper
	public void openTKDialog(String t){
		ToKnappsDialog tkd = ToKnappsDialog.newInstance(t);
		tkd.show(getSupportFragmentManager(), "ToKnappsDialog");
	}
	
	//Kalles på når en knapp på actionbar trykkes inn
	public boolean onOptionsItemSelected(MenuItem i){
		if(i.getItemId() == R.id.menu_nyttOrd)
			openTKDialog(getString(R.string.header_nyttOrd));
		else
			openTKDialog(getString(R.string.header_avslutte));
		return true;
	}
	
	//Utfører en handling når OK-knapp i RundeFerdigDialog- eller NyRundeDialog-vindu trykkes på
	public void okClicked(String tittel){
		if(tittel.compareTo(getString(R.string.header_tomt)) == 0){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish(); //avslutter activity
		}
		else if(tittel.compareTo(getString(R.string.header_avslutte)) == 0){
			finish();
		}
		else{
			iRestart = false;
			nyttOrd();
		}			
	}
	
	//Utfører en handling når cancel trykkes inn i NyRundeDialog
	public void cancelClicked(){
		return;
	}
	
	//Knappelytter for "tastaturet"
	View.OnClickListener bokstavLytter = new View.OnClickListener(){
		public void onClick(View v){
			validerGjetting(v);
		}
	};
	
} //class Spill