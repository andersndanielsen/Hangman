package s180475.android1.hioa;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Spilleregler extends Activity {

	private TextView text, overskrift;
	private Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spilleregler);
		
		font = Typeface.createFromAsset(getAssets(), "fonts/KGTenThousandReasons.ttf");
		text = (TextView) findViewById(R.id.reglementet);
		text.setTypeface(font);
		text.setTextColor(getResources().getColor(R.color.white));
		text.setMovementMethod(new ScrollingMovementMethod());
		
		overskrift = (TextView) findViewById(R.id.overskrift);
		overskrift.setTypeface(font,Typeface.BOLD);
		overskrift.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_spilleregler, menu);
		return true;
	}
	
	//Kalles på når en knapp på actionbar trykkes inn
	public boolean onOptionsItemSelected(MenuItem i){
		finish();
		return true;
	}
}
