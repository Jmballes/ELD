package com.jmbdh;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.newproyectjmb.R;


public class OptionsDialogFragment extends DialogFragment {
	SharedPreferences app_preferences;
	CheckBox sonido=null;
	CheckBox vibracion=null;
	static OptionsDialogFragment newInstance(){
		OptionsDialogFragment mDialogFragment= new OptionsDialogFragment();
		mDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME,0);
		return mDialogFragment;
		}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        final View parent = inflater.inflate(R.layout.layout_opciones_pausa, container, false);
        Activity activity=(Activity)(getActivity());
        SharedPreferences app_preferences=activity.getSharedPreferences("test",Context.MODE_WORLD_WRITEABLE);
        
       
		boolean sonido_boolean = app_preferences.getBoolean(
				"preference_sonido", false);
		boolean vibracion_boolean = app_preferences.getBoolean(
				"preference_vibracion", false);
		

		
		sonido = (CheckBox) parent.findViewById(R.id.check4);
		vibracion = (CheckBox) parent.findViewById(R.id.check5);
		

		// Establecemos las preferencias cargadas, a nuestra interfaz
		sonido.setChecked(sonido_boolean);
		vibracion.setChecked(vibracion_boolean);

        // Watch for button clicks.
        Button buttonOk = (Button)parent.findViewById(R.id.ok2);
        buttonOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Activity activity=(Activity)(getActivity());
            	SharedPreferences app_preferences=activity.getSharedPreferences("test",Context.MODE_WORLD_WRITEABLE);
                    	SharedPreferences.Editor editor = app_preferences
						.edit();
            	sonido = (CheckBox) parent.findViewById(R.id.check4);
        		vibracion = (CheckBox) parent.findViewById(R.id.check5);
				editor.putBoolean("preference_sonido",
						sonido.isChecked());
				editor.putBoolean("preference_vibracion",
						vibracion.isChecked());
				
				editor.commit();
				dismiss();
            }
        });
     // Watch for button clicks.
        Button buttonCancel = (Button)parent.findViewById(R.id.cancel2);
        buttonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //((MenuActivity)getActivity()).doExitCancelClick();
                dismiss();
            }
        });

        return parent;
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

