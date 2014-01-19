package s180475.android1.hioa;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

	private Button ny;
	private Button regler;
	private Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		font = Typeface.createFromAsset(getAssets(), "fonts/KGTenThousandReasons.ttf");
		ny = (Button) findViewById(R.id.nyttSpillKnapp);
		ny.setTypeface(font);
		regler = (Button) findViewById(R.id.lesReglerKnapp);
		regler.setTypeface(font);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//starter opp et nytt spill i ny aktivitet
	public void startSpill(View v){
		Intent intent = new Intent(this, SpillActivity.class);
		startActivity(intent);
	}
	
	//viser reglementet i ny aktivitet
	public void lesRegler(View v){
		Intent intent = new Intent(this, Spilleregler.class);
		startActivity(intent);
	}
	
	//Kalles på når en knapp på actionbar trykkes inn
	public boolean onOptionsItemSelected(MenuItem i){
		finish();
		return true;
	}
}
