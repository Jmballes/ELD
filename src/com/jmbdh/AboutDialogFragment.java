package com.jmbdh;

import com.newproyectjmb.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 

public class AboutDialogFragment extends DialogFragment {
	static AboutDialogFragment newInstance(){
		AboutDialogFragment mDialogFragment = new AboutDialogFragment();
		mDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.MyCustomTheme);
	return mDialogFragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_ayuda, container, false);
        
        return v;
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
    	super.onActivityCreated(arg0);
        getDialog().getWindow()
        .getAttributes().windowAnimations = R.style.MyCustomTheme;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      Dialog dialog = super.onCreateDialog(savedInstanceState);
      
      dialog.getWindow()
      .getAttributes().windowAnimations = R.style.MyCustomTheme;
     // new AlertDialog.Builder( getActivity(), R.style.MyCustomTheme );
      //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//      setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent);
//      // request a window without the title
//      dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().PROGRESS_SECONDARY_END(new ColorDrawable(Color.TRANSPARENT));

      return dialog;
    }
}

