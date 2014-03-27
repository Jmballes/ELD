package com.jmbdh;


import com.newproyectjmb.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


public class PuntDialog extends DialogFragment {
	static PuntDialog newInstance(){
		PuntDialog mDialogFragment= new PuntDialog();
		mDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME,0);
		return mDialogFragment;
		}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_exit, container, false);
        

        // Watch for button clicks.
        Button buttonOk = (Button)v.findViewById(R.id.ok);
        buttonOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((MenuActivity)getActivity()).doExitOkClick();
                dismiss();
            }
        });
     // Watch for button clicks.
        Button buttonCancel = (Button)v.findViewById(R.id.cancel);
        buttonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((MenuActivity)getActivity()).doExitCancelClick();
                dismiss();
            }
        });

        return v;
    }

	    
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        
//        return new AlertDialog.Builder(getActivity())
//		.setIcon(R.drawable.icon)
//		.set
//		.setTitle(R.string.dialog_salir_titulo)
//		.setMessage(R.string.dialog_salir_texto)
//		.setPositiveButton(R.string.dialog_salir_ok,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,
//							int whichButton) {
//						((MenuActivity)getActivity()).doExitOkClick();
////						optionPressed = R.id.salir;
////						setAllViewsOut();
//						/* User clicked OK so do some stuff */
//					}
//				})
//		.setNegativeButton(R.string.dialog_salir_cancelar,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,
//							int whichButton) {
//
//						((MenuActivity)getActivity()).doExitCancelClick();
//					}
//				}).create();
//    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      Dialog dialog = super.onCreateDialog(savedInstanceState);

      // request a window without the title
      dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
      return dialog;
    }

}
