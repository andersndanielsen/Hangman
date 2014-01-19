package s180475.android1.hioa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ToKnappsDialog extends DialogFragment{
	
	static ToKnappsDialog newInstance(String titel){
		ToKnappsDialog nrd = new ToKnappsDialog();
		Bundle args = new Bundle();
		args.putString("Tittel", titel);
		nrd.setArguments(args);
		return nrd;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		String title = getArguments().getString("Tittel");
		Dialog myDialog = new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setPositiveButton(getString(R.string.nyttOrdPKnapp), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((SpillActivity) getActivity()).okClicked(getArguments().getString("Tittel"));
			}
		})
		.setNegativeButton(getString(R.string.nyttOrdNKnapp), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((SpillActivity) getActivity()).cancelClicked();
			}
		})
		.create();
		
		return myDialog;
	}
}
