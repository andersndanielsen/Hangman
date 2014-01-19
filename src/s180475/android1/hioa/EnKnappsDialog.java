package s180475.android1.hioa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EnKnappsDialog extends DialogFragment{
	
	static EnKnappsDialog newInstance(String titel, String message){
		EnKnappsDialog rfd = new EnKnappsDialog();
		Bundle args = new Bundle();
		args.putString("Tittel", titel);
		args.putString("Melding", message);
		rfd.setArguments(args);
		return rfd;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		String title = getArguments().getString("Tittel");
		String message = getArguments().getString("Melding");
		Dialog myDialog = new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((SpillActivity) getActivity()).okClicked(getArguments().getString("Tittel"));
			}
		})
		.create();
		
		return myDialog;
	}
}
